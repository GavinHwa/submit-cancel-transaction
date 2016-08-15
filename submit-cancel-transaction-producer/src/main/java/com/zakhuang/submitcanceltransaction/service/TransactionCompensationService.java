package com.zakhuang.submitcanceltransaction.service;

import com.zakhuang.submitcanceltransaction.entity.TransactionContextItem;
import com.zakhuang.submitcanceltransaction.entity.TransactionRootContext;
import com.zakhuang.submitcanceltransaction.entity.TransactionStatus;
import com.zakhuang.submitcanceltransaction.repository.TransactionRepository;
import com.zakhuang.submitcanceltransaction.utils.SctArrayUtils;
import com.zakhuang.submitcanceltransaction.utils.SctBeanUtils;
import com.zakhuang.submitcanceltransaction.utils.SctReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created on 2016/8/10.
 */
@Component
public class TransactionCompensationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionCompensationService.class);
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * 批量执行补偿
     *
     * @param transactionRepositoryAllUnmodifiedSince
     */
    public void compensate(List<TransactionRootContext> transactionRepositoryAllUnmodifiedSince) {
        if (CollectionUtils.isEmpty(transactionRepositoryAllUnmodifiedSince)) {
            return;
        }
        for (TransactionRootContext transactionRootContext : transactionRepositoryAllUnmodifiedSince) {
            try {
                compensate(transactionRootContext);
            } catch (Throwable e) {
                LOGGER.error("Compensate Transaction Fail!", e);
            }
        }
    }

    /**
     * 执行补偿
     *
     * @param transactionRootContext
     */
    public void compensate(TransactionRootContext transactionRootContext) {
        LinkedList<TransactionContextItem> transactionContextItems = transactionRootContext.getTransactionContextItems();
        Iterator<TransactionContextItem> iterator = transactionContextItems.iterator();
        TransactionContextItem transactionContextItem;
        //无法取消或者FAIL的item的总数
        int irrevocableItemCount = 0;
        int failStatusItemCount = 0;
        Object result;
        Object invokedBean;
        while (iterator.hasNext()) {
            transactionContextItem = iterator.next();
            //跳过达到最大重试次数的
            if (transactionContextItem.getStatus() == TransactionStatus.FAIL) {
                failStatusItemCount++;
                continue;
            }
            //跳过不可取消的
            if (!transactionContextItem.isCancelable()) {
                irrevocableItemCount++;
                continue;
            }
            invokedBean = applicationContext.getBean(transactionContextItem.getCls());
            //可能在其它SOA上
            if (invokedBean == null) {
                LOGGER.warn("Can't Find Class {}", transactionContextItem.getCls());
                continue;
            }
            //根据TransactionRootContext状态决定是执行取消还是确认方法
            String methodName = transactionRootContext.getStatus() == TransactionStatus.NEED_TO_CONFIRM ? transactionContextItem.getConfirmMethod() : transactionContextItem.getCancelMethod();
            try {
                result = invokeMethod(transactionRootContext, transactionContextItem, invokedBean, methodName);
            } catch (Throwable e) {
                result = Boolean.FALSE;
                LOGGER.error("Invoke Compensation Method Fail!", e);
            }
            failStatusItemCount = updateTransactionItem(transactionRootContext, iterator, transactionContextItem, failStatusItemCount, result);
        }
        transactionRootContext.setUpdateTime(new Date());
        updateTransactionRootContext(transactionRootContext, irrevocableItemCount, failStatusItemCount);
    }

    private void updateTransactionRootContext(TransactionRootContext transactionRootContext, int irrevocableItemCount, int failStatusItemCount) {
        //根事务下的项只剩下不可取消的话，可算成功
        int itemSize = transactionRootContext.getTransactionContextItems().size();
        if (irrevocableItemCount == itemSize) {
            transactionRepository.delete(transactionRootContext);
        } else {
            //如果其下不是不可取消的，就是超过最大重试次数的，算是失败
            if ((failStatusItemCount + irrevocableItemCount) == itemSize) {
                transactionRootContext.setStatus(TransactionStatus.FAIL);
            }
            transactionRepository.update(transactionRootContext);
        }
    }

    private int updateTransactionItem(TransactionRootContext transactionRootContext, Iterator<TransactionContextItem> iterator, TransactionContextItem transactionContextItem, int failStatusItemCount, Object result) {
        int failCount;
        if (result != null && result.getClass() == Boolean.class) {
            //如果结果是OK的，那么可以移除
            if ((boolean) result) {
                iterator.remove();
            } else {
                failCount = transactionContextItem.getFailCount();
                //如果超过最大重试次数则失败
                if (++failCount == transactionContextItem.getMaxRetryCount()) {
                    transactionContextItem.setStatus(TransactionStatus.FAIL);
                    failStatusItemCount++;
                } else {
                    transactionContextItem.setFailCount(failCount);
                }
            }
        } else {
            //返回不为boolean的话，不清楚如何处理
            LOGGER.error("Not Support This Return Type :{}, Change To Boolean!", result);
        }
        transactionRepository.update(transactionRootContext, transactionContextItem);
        return failStatusItemCount;
    }

    private Object invokeMethod(TransactionRootContext rootContext, TransactionContextItem transactionContextItem, Object bean, String methodName) {
        Class<?>[] parameterTypes;
        Object result;
        Object[] objs = transactionContextItem.getParamObjs();
        Method method;
        List<Method> methodList = SctReflectionUtils.getMethodsByName(transactionContextItem.getCls(), methodName, false);
        if (methodList.size() != 1) {
            throw new RuntimeException("Found More Than Or Less Than One Cancel Method Or Confirm Method, We Don't Know Which Is Right, method Name : " + methodName);
        } else {
            method = methodList.get(0);
        }
        parameterTypes = method.getParameterTypes();
        List<Object> objects = null;
        if (parameterTypes.length > 0) {
            Class<?> lastestParam = parameterTypes[parameterTypes.length - 1];
            if (lastestParam == TransactionRootContext.class) {
                objs = SctArrayUtils.addLast(objs, SctBeanUtils.copy(rootContext, null));
            } else if (lastestParam == TransactionContextItem.class) {
                objs = SctArrayUtils.addLast(objs, SctBeanUtils.copy(transactionContextItem, null));
            }
            result = ReflectionUtils.invokeMethod(method, bean, objs);
        } else {
            result = ReflectionUtils.invokeMethod(method, bean);
        }
        return result;
    }
}
