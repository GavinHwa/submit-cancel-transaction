package com.zakhuang.submitcanceltransaction.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.Objects;
import java.util.UUID;

/**
 * Created on 2016/8/9.
 */
public class TransactionRootContext extends TransactionContextItem implements Serializable {
    private static final long serialVersionUID = -8131954762397498847L;
    private String transactionID;
    /**
     * TransactionRootContext的状态：
     * FAIL：其下Item全部失败，需要人工处理
     * NEED_FOR_COMPENSATION：事务失败，需要补偿
     * COMMITTING：正在提交
     */
    private TransactionStatus status = TransactionStatus.INIT;
    private Date createTime;
    private Date updateTime;

    public TransactionRootContext() {
        transactionID = UUID.randomUUID().toString().replace("-", "");
        createTime = new Date();
    }

    public TransactionRootContext setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public TransactionRootContext setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    private LinkedList<TransactionContextItem> transactionContextItems = new LinkedList<>();


    public String getTransactionID() {
        return transactionID;
    }

    public TransactionRootContext setTransactionID(String transactionID) {
        this.transactionID = transactionID;
        return this;
    }

    public LinkedList<TransactionContextItem> getTransactionContextItems() {
        return transactionContextItems;
    }

    public TransactionRootContext setTransactionContextItems(LinkedList<TransactionContextItem> transactionContextItems) {
        this.transactionContextItems = transactionContextItems;
        return this;
    }

    @Override
    public TransactionStatus getStatus() {
        return status;
    }

    public TransactionRootContext setStatus(TransactionStatus status) {
        this.status = status;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransactionRootContext)) return false;
        if (!super.equals(o)) return false;
        TransactionRootContext that = (TransactionRootContext) o;
        return Objects.equals(getTransactionID(), that.getTransactionID()) &&
                getStatus() == that.getStatus() &&
                Objects.equals(getCreateTime(), that.getCreateTime()) &&
                Objects.equals(getUpdateTime(), that.getUpdateTime()) &&
                Objects.equals(getTransactionContextItems(), that.getTransactionContextItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getTransactionID(), getStatus(), getCreateTime(), getUpdateTime(), getTransactionContextItems());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TransactionRootContext{");
        sb.append("transactionID='").append(transactionID).append('\'');
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", transactionContextItems=").append(transactionContextItems);
        sb.append('}');
        return sb.toString();
    }
}
