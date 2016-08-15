package com.zakhuang.submitcanceltransaction;

import com.zakhuang.submitcanceltransaction.annotation.CancelableMethod;
import com.zakhuang.submitcanceltransaction.entity.TransactionContextItem;
import com.zakhuang.submitcanceltransaction.entity.TransactionRootContext;
import com.zakhuang.submitcanceltransaction.entity.TransactionStatus;
import com.zakhuang.submitcanceltransaction.repository.TransactionRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created on 2016/8/9.
 * 将Method信息转为TransactionRootContext，
 * 其commit、rollback等方法更新TransactionRootContext状态，并交由TransactionRepository持久化
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TransactionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionManager.class);
    @Autowired
    private TransactionRepository transactionRepository;
    private TransactionRootContext transactionRootContext;

    public TransactionContextItem begin(ProceedingJoinPoint pjp) {
        TransactionContextItem transactionContextItem = initTransactionContext(pjp);
        transactionRootContext.setStatus(TransactionStatus.COMMITTING);
        transactionRootContext.setUpdateTime(new Date());
        transactionRepository.create(transactionRootContext);
        return transactionContextItem;
    }

    public void commit() {
        transactionRootContext.setStatus(TransactionStatus.NEED_TO_CONFIRM);
        updateTransactionStatus(null);
    }

    public void rollback() {
        transactionRootContext.setStatus(TransactionStatus.NEED_FOR_COMPENSATION);
        updateTransactionStatus(null);
    }

    private void updateTransactionStatus(TransactionContextItem transactionContextItem) {
        transactionRootContext.setUpdateTime(new Date());
        if (transactionContextItem != null) {
            transactionRepository.update(transactionRootContext, transactionContextItem);
        } else {
            transactionRepository.update(transactionRootContext);
        }
    }

    public void updateSubTransactionStatus(TransactionContextItem transactionContextItem, boolean success) {
        transactionContextItem.setStatus(success ? TransactionStatus.NEED_TO_CONFIRM : TransactionStatus.NEED_FOR_COMPENSATION);
        updateTransactionStatus(transactionContextItem);
    }

    /**
     * 入参转bean
     *
     * @param pjp
     */
    private TransactionContextItem initTransactionContext(ProceedingJoinPoint pjp) {
        if (transactionRootContext == null) {
            transactionRootContext = new TransactionRootContext();
        }
        TransactionContextItem transactionContextItem = new TransactionContextItem();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        CancelableMethod cancelableMethod = method.getAnnotation(CancelableMethod.class);
        Class handlClass = cancelableMethod.handlClass();
        String cancelMethodName = cancelableMethod.cancelMethod();
        String confirmMethodName = cancelableMethod.confirmMethod();
        //不支持补偿的
        if (handlClass == null || StringUtils.isEmpty(cancelMethodName)) {
            transactionContextItem.setCancelable(false);
        } else {
            transactionContextItem.setMaxRetryCount(cancelableMethod.maxRetryCount());
            transactionContextItem.setCls(handlClass);
            transactionContextItem.setCancelMethod(cancelMethodName);
            transactionContextItem.setCancelable(true);
        }
        transactionContextItem.setConfirmMethod(confirmMethodName);
        transactionContextItem.setParamCls(method.getParameterTypes());
        transactionContextItem.setParamObjs(pjp.getArgs());
        transactionContextItem.setStatus(TransactionStatus.INIT);
        transactionRootContext.getTransactionContextItems().addLast(transactionContextItem);
        return transactionContextItem;
    }
}
