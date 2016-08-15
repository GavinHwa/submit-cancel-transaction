package com.zakhuang.submitcanceltransaction.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created on 2016/8/9.
 */
public class TransactionContextItem implements Serializable {
    private static final long serialVersionUID = -1103088298773080618L;
    private Class cls;
    private String cancelMethod;
    private Object[] paramObjs;
    private Class[] paramCls;
    private int maxRetryCount;
    private int failCount;
    private TransactionStatus status;
    private boolean cancelable;
    private String confirmMethod;

    public String getCancelMethod() {
        return cancelMethod;
    }

    public TransactionContextItem setCancelMethod(String cancelMethod) {
        this.cancelMethod = cancelMethod;
        return this;
    }

    public String getConfirmMethod() {
        return confirmMethod;
    }

    public TransactionContextItem setConfirmMethod(String confirmMethod) {
        this.confirmMethod = confirmMethod;
        return this;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public TransactionContextItem setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public int getMaxRetryCount() {
        return maxRetryCount;
    }

    public TransactionContextItem setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
        return this;
    }

    public int getFailCount() {
        return failCount;
    }

    public TransactionContextItem setFailCount(int failCount) {
        this.failCount = failCount;
        return this;
    }

    public Object[] getParamObjs() {
        return paramObjs;
    }

    public TransactionContextItem setParamObjs(Object[] paramObjs) {
        this.paramObjs = paramObjs;
        return this;
    }

    public Class[] getParamCls() {
        return paramCls;
    }

    public TransactionContextItem setParamCls(Class[] paramCls) {
        this.paramCls = paramCls;
        return this;
    }

    public Class getCls() {
        return cls;
    }

    public TransactionContextItem setCls(Class cls) {
        this.cls = cls;
        return this;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public TransactionContextItem setStatus(TransactionStatus status) {
        this.status = status;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransactionContextItem)) return false;
        TransactionContextItem that = (TransactionContextItem) o;
        return getMaxRetryCount() == that.getMaxRetryCount() &&
                getFailCount() == that.getFailCount() &&
                isCancelable() == that.isCancelable() &&
                Objects.equals(getCls(), that.getCls()) &&
                Objects.equals(getCancelMethod(), that.getCancelMethod()) &&
                Arrays.equals(getParamObjs(), that.getParamObjs()) &&
                Arrays.equals(getParamCls(), that.getParamCls()) &&
                getStatus() == that.getStatus() &&
                Objects.equals(getConfirmMethod(), that.getConfirmMethod());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCls(), getCancelMethod(), getParamObjs(), getParamCls(), getMaxRetryCount(), getFailCount(), getStatus(), isCancelable(), getConfirmMethod());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TransactionContextItem{");
        sb.append("cls=").append(cls);
        sb.append(", cancelMethod='").append(cancelMethod).append('\'');
        sb.append(", paramObjs=").append(Arrays.toString(paramObjs));
        sb.append(", paramCls=").append(Arrays.toString(paramCls));
        sb.append(", maxRetryCount=").append(maxRetryCount);
        sb.append(", failCount=").append(failCount);
        sb.append(", status=").append(status);
        sb.append(", cancelable=").append(cancelable);
        sb.append(", confirmMethod='").append(confirmMethod).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
