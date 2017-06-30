package com.duowei.kitchen_china.print;

/**
 * //
 * Created by Administrator on 2017-05-26.
 */

public interface IPrint {

    void connect();

    boolean isConnected();

    void close();

    boolean sendMsg(String msg);

    boolean sendMsg(byte[] msg);
}
