package com.duowei.kitchen_china.server;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;

import com.android.volley.VolleyError;
import com.duowei.kitchen_china.bean.Cfpb;
import com.duowei.kitchen_china.bean.Cfpb2;
import com.duowei.kitchen_china.bean.Cfpb_item;
import com.duowei.kitchen_china.event.OrderFood;
import com.duowei.kitchen_china.httputils.DownHTTP;
import com.duowei.kitchen_china.httputils.Net;
import com.duowei.kitchen_china.httputils.Post;
import com.duowei.kitchen_china.httputils.VolleyResultListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

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
                mHandler.postDelayed(mRunnable,5000);
            }
        }, 1000);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
    }
}
