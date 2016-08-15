package com.zakhuang.submitcanceltransaction.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

/**
 * Created on 2016/8/11.
 */
public class SctBeanUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(SctBeanUtils.class);

    public static <T> T copy(T src, T target) {
        if (target == null) {
            //编码阶段的问题，不抛上去了
            try {
                target = ((T) src.getClass().newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                LOGGER.error("Create Instance Fail!", e);
                return null;
            }
        }
        BeanUtils.copyProperties(src, target);
        return target;
    }
}
