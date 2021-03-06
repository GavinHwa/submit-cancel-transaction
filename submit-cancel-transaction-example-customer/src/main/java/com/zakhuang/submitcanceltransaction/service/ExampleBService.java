package com.zakhuang.submitcanceltransaction.service;

import com.zakhuang.submitcanceltransaction.annotation.CancelableMethod;
import com.zakhuang.submitcanceltransaction.entity.TransactionRootContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created on 2016/8/10.
 */
@Service
@Transactional
public class ExampleBService {

    @CancelableMethod(handlClass = ExampleBService.class, cancelMethod = "cancelServer", maxRetryCount = 10, confirmMethod = "confirmServer")
    public void server() {
        System.out.println("进入服务" + this.getClass().getName());
    }

    public boolean cancelServer(TransactionRootContext transactionRootContext) {
        System.out.println("获取到的TransactionRootContext：" + transactionRootContext);
        System.out.println("取消服务" + this.getClass().getName());
        return true;
    }

    public boolean confirmServer(TransactionRootContext transactionRootContext) {
        System.out.println("确认服务获取到的TransactionRootContext：" + transactionRootContext);
        System.out.println("确认服务" + this.getClass().getName());
        return true;
    }

}
