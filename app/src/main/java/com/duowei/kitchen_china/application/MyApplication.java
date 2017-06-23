package com.duowei.kitchen_china.application;

import android.app.Application;

import com.duowei.kitchen_china.httputils.MyVolley;

/**
 * Created by Administrator on 2017-03-21.
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        MyVolley.init(this);
    }
}
