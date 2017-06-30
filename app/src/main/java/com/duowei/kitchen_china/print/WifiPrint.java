package com.duowei.kitchen_china.print;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.duowei.kitchen_china.application.MyApplication;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * IP地址打印
 *
 * Created by Administrator on 2017-05-25.
 */

public class WifiPrint implements IPrint {
    private static final String TAG = "WifiPrint";

    private String host;
    private Socket socket;
    private OutputStream os;
    private Handler mHandler = new Handler();

    public WifiPrint(String host) {
        this.host = host;
        connect();
    }

    @Override
    public void connect() {
        close();

        Toast.makeText(MyApplication.getContext(), "打印机连接中...", Toast.LENGTH_SHORT).show();
        new ConnectThread().start();
    }

    @Override
    public boolean isConnected() {
        if (socket != null
                && !socket.isClosed()
                && socket.isConnected())
            return true;
        else
            return false;
    }

    @Override
    public void close() {
        if (socket != null
                && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean sendMsg(String msg) {
        if (isConnected()) {
            try {
                os.write(msg.getBytes("gbk"));
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        } else {
            Log.d(TAG, "wifi socket lost connection");
            return false;
        }
    }

    @Override
    public boolean sendMsg(byte[] msg) {
        if (isConnected()) {
            try {
                os.write(msg);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        } else {
            Log.d(TAG, "wifi socket lost connection");
            return false;
        }
    }

    private class ConnectThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                socket = new Socket(host, 9100);
                os = socket.getOutputStream();

                if (socket.isConnected()) {
                    Log.d(TAG, "wifi socket connected");
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyApplication.getContext(), "打印机连接成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "wifi socket exception");
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyApplication.getContext(), "打印机连接错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
