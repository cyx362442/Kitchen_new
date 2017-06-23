package com.duowei.kitchen_china.server;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;

import com.android.volley.VolleyError;
import com.duowei.kitchen_china.bean.Cfpb;
import com.duowei.kitchen_china.bean.Cfpb_item;
import com.duowei.kitchen_china.event.OrderFood;
import com.duowei.kitchen_china.httputils.DownHTTP;
import com.duowei.kitchen_china.httputils.VolleyResultListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class PollingService extends Service {
    private Handler mHandler;
    private Runnable mRunnable;
    private List<Cfpb>listCfpb=new ArrayList<>();
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
        final String sql = "select * from cfpb|";

        if(mHandler!=null){
            mHandler.removeCallbacks(mRunnable);
        }
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable =new Runnable() {
            @Override
            public void run() {
                HttpCfpb(url, sql);
                mHandler.postDelayed(mRunnable,10000);
            }
        }, 1000);
    }

    private synchronized void HttpCfpb(String url, String sql) {
        DownHTTP.postVolley6(url, sql, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
//                List<Cfpb> listCfpb = gson.fromJson(response, new TypeToken<List<Cfpb>>() {
//                }.getType());
                listCfpb.clear();
                Cfpb[] cfpbs = gson.fromJson(response, Cfpb[].class);
                String str="";
                for(int i=0;i<cfpbs.length;i++){
                        for(int j=0;j<cfpbs.length;j++){
                            if((cfpbs[j].getXMBH()).equals(cfpbs[i].getXMBH())){
                                cfpbs[i].setZjsl(cfpbs[i].getZjsl()+cfpbs[j].getSL());
                                Cfpb_item cfpb_item = new Cfpb_item(cfpbs[j].getXMBH(), cfpbs[j].getBY1(), cfpbs[j].getSL(), cfpbs[j].getXDSJ());
                                List<Cfpb_item> list = cfpbs[i].getList();
                                list.add(cfpb_item);
                                cfpbs[i].setList(list);
                            }
                        }
                    if(!str.contains(cfpbs[i].getXMBH())){

                        listCfpb.add(cfpbs[i]);
                        str=str+cfpbs[i].getXMBH()+"-";
                    }
                }

                EventBus.getDefault().post(new OrderFood(listCfpb));
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
    }
}
