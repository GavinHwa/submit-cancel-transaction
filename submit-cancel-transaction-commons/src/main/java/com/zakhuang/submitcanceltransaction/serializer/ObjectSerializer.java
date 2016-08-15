package com.zakhuang.submitcanceltransaction.serializer;

/**
 * Created on 2016/8/9.
 */
public interface ObjectSerializer<T> {
    byte[] serialize(T t);

    T deserialize(byte[] bytes);
}
