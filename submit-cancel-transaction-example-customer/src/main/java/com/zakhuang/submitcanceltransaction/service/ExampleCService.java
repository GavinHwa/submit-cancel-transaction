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
public class ExampleCService {
    @CancelableMethod(handlClass = ExampleCService.class, cancelMethod = "cancelServer", maxRetryCount = 10, confirmMethod = "confirmServer")
    public void server() {
        System.out.println("进入服务:" + ExampleCService.class.getName());
    }

    public boolean cancelServer() {
        System.out.println("取消服务" + ExampleCService.class.getName());
        return true;
    }

    public boolean confirmServer(TransactionRootContext transactionRootContext) {
        System.out.println("确认服务获取到的TransactionRootContext：" + transactionRootContext);
        System.out.println("确认服务" + this.getClass().getName());
        return true;
    }
}
