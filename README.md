##Submit Cancel Transaction是什么
一个Try—Confirm—Cancel的实现，不过项目开始时是“直接提交，有错取消”的一阶式，所以第一个单词变成了“Submit”...不过思路、实现大致相同


##Try—Confirm—Cancel简介
TCC分为三个阶段：

* Try:
检查是否满足执行业务的条件，OK的话预先分配必要资源
* Confirm:
用Try阶段分配的资源完成业务，需保证幂等性
* Cancel:
释放Try阶段分配的资源，需保证幂等性


##Submit Cancel Transaction做了啥
SCT帮你：

* 自动在事务发生错误时调用取消方法，正确无误时调用确认方法；
* 记录了整个调用链的类、方法、入参，方便确认和取消阶段回溯；
* 实现了基于Redis的持久化接口，当然也可以自己实现TransactionRepository，扩充到JDBC之类的；

你还需要：

* 自行实现确认和取消的幂等性


##如何使用Submit Cancel Transaction
项目下的submit-cancel-transaction-example-customer示例，演示了SCT的使用方法：

1. 在需要TCC支持的方法上加上@CancelableMethod 注解，如果一个加了该注解的方法内调用未加该注解的方法，那么未加该注解的方法将不会被记录到调用链中；
2. 在@CancelableMethod注解中指明确认、取消方法和执行类，如果该方法无需确认和取消，但是要记录到调用链中，只需添加@CancelableMethod即可，无需指定方法和类；
3. 确认和取消方法的前几个入参要与标注@CancelableMethod的方法（后称"Try方法"）一致，但最后一个入参如果是TransactionContextItem或TransactionRootContext的话，可以不在Try方法的入参上标明。其中，TransactionRootContext记录了整个调用链的方法、入参信息，TransactionContextItem记录了Try方法的入参信息（其实也包含在TransactionRootContext内）。


##问题反馈
如果在使用过程中有任何问题，欢迎联系：

* Email：八六零523久久6 # qq.com (自行替换)
* QQ：八六零523久久6


