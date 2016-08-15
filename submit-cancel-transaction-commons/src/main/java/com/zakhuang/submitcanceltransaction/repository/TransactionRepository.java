package com.zakhuang.submitcanceltransaction.repository;


import com.zakhuang.submitcanceltransaction.entity.TransactionContextItem;
import com.zakhuang.submitcanceltransaction.entity.TransactionRootContext;

import java.util.Date;
import java.util.List;

/**
 * Created on 2016/8/9.
 * 持久化TransactionRootContext的接口
 */
public interface TransactionRepository {


    void create(TransactionRootContext transaction);

    int update(TransactionRootContext transaction);

    /**
     * 方便关系型数据库实现单独更新TransactionContextItem
     *
     * @param transaction
     * @param transactionContextItem 需要更新的TransactionContextItem
     * @return
     */
    int update(TransactionRootContext transaction, TransactionContextItem transactionContextItem);

    int delete(TransactionRootContext transaction);

    /**
     * 默认获取状态为COMPENSATION_NEEDED的TransactionRootContext
     *
     * @param date
     * @return
     */
    List<TransactionRootContext> findAllUnmodifiedSince(Date date);
}
