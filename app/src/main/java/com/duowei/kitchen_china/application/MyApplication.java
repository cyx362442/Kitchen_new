package com.duowei.kitchen_china.application;

import android.content.Context;

import com.duowei.kitchen_china.httputils.MyVolley;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

/**
 * Created by Administrator on 2017-03-21.
 */

public class MyApplication extends LitePalApplication{
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        MyVolley.init(this);
        this.mContext=this;
    }
    public static Context getContext() {
        return mContext;
    }
}
