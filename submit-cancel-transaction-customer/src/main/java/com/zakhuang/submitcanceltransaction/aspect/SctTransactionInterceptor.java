package com.zakhuang.submitcanceltransaction.aspect;

import com.zakhuang.submitcanceltransaction.TransactionManager;
import com.zakhuang.submitcanceltransaction.entity.TransactionContextItem;
import com.zakhuang.submitcanceltransaction.exception.SaveOrUpdateTransactionException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created on 2016/8/9.
 */
@Component
public class SctTransactionInterceptor {
    static final Logger LOGGER = LoggerFactory.getLogger(SctTransactionInterceptor.class);
    @Autowired
    private ApplicationContext context;
    private ThreadLocal<TransactionManager> transactionManagerThl = new ThreadLocal<>();

    public Object interceptTransactionMethod(ProceedingJoinPoint pjp) throws Throwable {
        TransactionManager transactionManager;
        boolean isRootTransaction;
        if (transactionManagerThl.get() == null) {
            transactionManager = context.getBean(TransactionManager.class);
            transactionManagerThl.set(transactionManager);
            isRootTransaction = true;
        } else {
            isRootTransaction = false;
            transactionManager = transactionManagerThl.get();
        }
        TransactionContextItem transactionContextItem = transactionManager.begin(pjp);
        Object proceed = null;
        try {
            proceed = pjp.proceed();
            if (isRootTransaction) {
                transactionManager.commit();
            } else {
                transactionManager.updateSubTransactionStatus(transactionContextItem, true);
            }
        } catch (SaveOrUpdateTransactionException e) {
            transactionProceedFail(transactionManager, transactionContextItem, isRootTransaction);
            LOGGER.error("Update Transaction Fail!", e);
        } catch (Throwable throwable) {
            transactionProceedFail(transactionManager, transactionContextItem, isRootTransaction);
            throw throwable;
        } finally {
            if (isRootTransaction) {
                transactionManagerThl.remove();
            }
        }
        return proceed;
    }

    private void transactionProceedFail(TransactionManager transactionManager, TransactionContextItem transactionContextItem, boolean isRootTransaction) {
        try {
            if (isRootTransaction) {
                transactionManager.rollback();
            } else {
                transactionManager.updateSubTransactionStatus(transactionContextItem, false);
            }
        } catch (SaveOrUpdateTransactionException e) {
            LOGGER.error("Update Transaction Fail!", e);
        }
    }
}
