package com.zakhuang.submitcanceltransaction.exception;

/**
 * Created on 2016/8/11.
 * 更新transaction异常
 */
public class SaveOrUpdateTransactionException extends RuntimeException {
    public SaveOrUpdateTransactionException(String message) {
        super(message);
    }

    public SaveOrUpdateTransactionException(String message, Throwable cause) {
        super(message, cause);
    }
}
