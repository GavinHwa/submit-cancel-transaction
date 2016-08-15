package com.zakhuang.submitcanceltransaction.repository;

import com.zakhuang.submitcanceltransaction.constants.RedisConstant;
import com.zakhuang.submitcanceltransaction.entity.TransactionContextItem;
import com.zakhuang.submitcanceltransaction.entity.TransactionRootContext;
import com.zakhuang.submitcanceltransaction.entity.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created on 2016/8/9.
 * 持久化TransactionRootContext的Redis实现
 */
@Repository
public class RedisTransactionRepository implements TransactionRepository {
    @Autowired
    private RedisTemplate<String, TransactionRootContext> redisTemplate;


    @Override
    public void create(TransactionRootContext transaction) {
        redisTemplate.opsForHash().put(RedisConstant.PRE_STR, transaction.getTransactionID(), transaction);
    }

    @Override
    public int update(TransactionRootContext transaction) {
        redisTemplate.opsForHash().put(RedisConstant.PRE_STR, transaction.getTransactionID(), transaction);
        return 1;
    }

    @Override
    public int update(TransactionRootContext transaction, TransactionContextItem transactionContextItem) {
        redisTemplate.opsForHash().put(RedisConstant.PRE_STR, transaction.getTransactionID(), transaction);
        return 1;
    }

    @Override
    public int delete(TransactionRootContext transaction) {
        redisTemplate.opsForHash().delete(RedisConstant.PRE_STR, transaction.getTransactionID(), transaction);
        return 1;
    }

    @Override
    public List<TransactionRootContext> findAllUnmodifiedSince(Date date) {
        List<Object> values = redisTemplate.opsForHash().values(RedisConstant.PRE_STR);
        LinkedList<TransactionRootContext> transactionRootContexts = new LinkedList<>();
        TransactionRootContext transactionRootContext;
        for (Object value : values) {
            transactionRootContext = (TransactionRootContext) value;
            if (transactionRootContext.getUpdateTime().after(date) &&
                    (transactionRootContext.getStatus() == TransactionStatus.NEED_FOR_COMPENSATION ||
                            transactionRootContext.getStatus() == TransactionStatus.NEED_TO_CONFIRM)) {
                transactionRootContexts.add(transactionRootContext);
            }
        }
        return transactionRootContexts;
    }

}
