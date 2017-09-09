package com.duowei.kitchen_china.server;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.duowei.kitchen_china.httputils.Net;
import com.duowei.kitchen_china.httputils.Post;

public class PollingService extends Service {
    private Handler mHandler;
    private Runnable mRunnable;
    public PollingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(mHandler!=null){
            mHandler.removeCallbacks(mRunnable);
        }
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable =new Runnable() {
            @Override
            public void run() {
                Post.getInstance().postCfpb(Net.sql_cfpb);
                mHandler.postDelayed(mRunnable,10000);
            }
        }, 1000);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
    }
}
