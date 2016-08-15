package com.zakhuang.submitcanceltransaction.utils;

import org.springframework.util.Assert;

import java.lang.reflect.Array;

/**
 * Created on 2016/8/15.
 */
public class SctArrayUtils {

    public static <T> T[] addLast(T[] arrs, T obj) {
        Assert.notNull(arrs);
        Assert.notNull(obj);
        Object o = Array.newInstance(obj.getClass(), arrs.length + 1);
        System.arraycopy(arrs, 0, o, 0, arrs.length);
        T[] result = (T[]) o;
        result[result.length - 1] = obj;
        return result;
    }
}
