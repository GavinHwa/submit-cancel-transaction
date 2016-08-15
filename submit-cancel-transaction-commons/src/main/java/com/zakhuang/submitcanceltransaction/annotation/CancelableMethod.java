package com.zakhuang.submitcanceltransaction.annotation;

import java.lang.annotation.*;

/**
 * Created on 2016/8/10.
 * 用于方法上，如果不写明处理补偿的类、方法名的话，表明此方法会调用其它支持补偿的service；
 * 如果写明补偿处理类、方法的话，失败后会调用指定的类、方法来处理补偿问题
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface CancelableMethod {

    /**
     * service失败后，处理此失败的Class
     */
    Class handlClass() default void.class;

    /**
     * service失败后，处理此失败的Class下的方法名
     */
    String cancelMethod() default "";

    String confirmMethod() default "";

    /**
     * 最多自动重试次数
     *
     * @return
     */
    int maxRetryCount() default 10;
}
