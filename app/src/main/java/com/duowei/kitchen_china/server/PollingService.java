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
        final String url = "http://192.168.1.78:2233/server/ServerSvlt?";
        final String sql="select A.XH,A.xmbh,LTrim(A.xmmc)as xmmc,A.dw,(isnull(A.sl,0)-isnull(A.tdsl,0)-isnull(A.YWCSL,0))sl,\n" +
                "A.pz,a.xdsj,A.BY1 as czmc,datediff(minute,A.xdsj,getdate())fzs from cfpb A LEFT JOIN JYXMSZ J ON A.XMBH=J.XMBH\n" +
                "where A.XDSJ BETWEEN DATEADD(mi,-180,GETDATE()) AND GETDATE() and (isnull(A.sl,0)-isnull(A.tdsl,0))>0\n" +
                "order by A.xdsj,A.xmmc|";

        if(mHandler!=null){
            mHandler.removeCallbacks(mRunnable);
        }
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable =new Runnable() {
            @Override
            public void run() {
                Post.getInstance().postCfpb(sql);
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
