package com.zakhuang.submitcanceltransaction.utils;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * Created on 2016/8/11.
 */
public class SctReflectionUtils {
    /**
     * 获取同名的全部方法
     *
     * @param cls
     * @param methodName
     * @param onlyDeclared
     * @return
     */
    public static List<Method> getMethodsByName(Class cls, String methodName, boolean onlyDeclared) {
        Method[] methods;
        List<Method> result = new LinkedList<>();
        if (onlyDeclared) {
            methods = cls.getDeclaredMethods();
        } else {
            methods = cls.getMethods();
        }
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                result.add(method);
            }
        }
        return result;
    }
}
