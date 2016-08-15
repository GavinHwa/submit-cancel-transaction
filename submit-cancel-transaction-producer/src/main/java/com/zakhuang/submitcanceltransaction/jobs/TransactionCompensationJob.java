package com.zakhuang.submitcanceltransaction.jobs;

import com.zakhuang.submitcanceltransaction.entity.TransactionRootContext;
import com.zakhuang.submitcanceltransaction.repository.TransactionRepository;
import com.zakhuang.submitcanceltransaction.service.TransactionCompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * Created on 2016/8/10.
 * 简单的
 */
@Component
public class TransactionCompensationJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionCompensationJob.class);
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionCompensationService transactionCompensationService;

    //先获取之前全部没处理的事务
    private Date lastestUpdateTime = new Date(1470925305682L);

    @Scheduled(initialDelay = 10000L, fixedDelay = 5000L)
    public void startCompensation() {
        Date now = new Date();
        List<TransactionRootContext> transactionRepositoryAllUnmodifiedSince = transactionRepository.findAllUnmodifiedSince(lastestUpdateTime);
        lastestUpdateTime = now;
        if (CollectionUtils.isEmpty(transactionRepositoryAllUnmodifiedSince)) {
            return;
        }
        transactionCompensationService.compensate(transactionRepositoryAllUnmodifiedSince);
    }
}
