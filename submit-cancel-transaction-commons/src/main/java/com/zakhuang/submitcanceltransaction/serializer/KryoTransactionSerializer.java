package com.zakhuang.submitcanceltransaction.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.zakhuang.submitcanceltransaction.entity.TransactionContextItem;
import com.zakhuang.submitcanceltransaction.entity.TransactionRootContext;

/**
 * Created on 2016/8/9.
 */
public class KryoTransactionSerializer implements ObjectSerializer<TransactionRootContext> {

    private static final Kryo kryo;

    static {
        kryo = new Kryo();
        kryo.register(TransactionRootContext.class);
        kryo.register(TransactionContextItem.class);
    }

    @Override
    public byte[] serialize(TransactionRootContext transactionRootContext) {
        Output output = new Output(256, -1);
        kryo.writeObject(output, transactionRootContext);
        return output.toBytes();
    }

    @Override
    public TransactionRootContext deserialize(byte[] bytes) {
        Input input = new Input(bytes);
        TransactionRootContext transaction = kryo.readObject(input, TransactionRootContext.class);
        return transaction;
    }
}
