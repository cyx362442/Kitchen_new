package com.duowei.kitchen_china.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.android.volley.VolleyError;
import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.bean.Jyxmsz;
import com.duowei.kitchen_china.event.SaleOut;
import com.duowei.kitchen_china.event.StartAnim;
import com.duowei.kitchen_china.event.StopAnim;
import com.duowei.kitchen_china.fragment.MainFragment;
import com.duowei.kitchen_china.fragment.SelloutFragment;
import com.duowei.kitchen_china.fragment.TopFragment;
import com.duowei.kitchen_china.fragment.UnSellOutFragment;
import com.duowei.kitchen_china.httputils.DownHTTP;
import com.duowei.kitchen_china.httputils.Net;
import com.duowei.kitchen_china.httputils.Post;
import com.duowei.kitchen_china.httputils.VolleyResultListener;
import com.duowei.kitchen_china.sound.KeySound;
import com.duowei.kitchen_china.uitls.DateTimes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class SellOutActivity extends AppCompatActivity {

    private SelloutFragment mSelloutFragment;
    private UnSellOutFragment mUnSellOutFragment;
    private List<Jyxmsz> mList;
    private View mLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_out);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);
        EventBus.getDefault().register(this);
        initFragment();
        mLoad = findViewById(R.id.loading);
        anim();
        getJyxmsz();
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    private void initFragment() {
        mSelloutFragment = new SelloutFragment();
        mUnSellOutFragment = new UnSellOutFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.frame01, mSelloutFragment).commit();
        getFragmentManager().beginTransaction()
                .replace(R.id.frame02, mUnSellOutFragment).commit();
    }

    public synchronized void getJyxmsz(){
        DownHTTP.postVolley6(Net.url, Net.sql_jyxmsz, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
            @Override
            public void onResponse(String response) {
                DataSupport.deleteAll(Jyxmsz.class);
                Gson gson = new Gson();
                List<Jyxmsz> listJyxmsz = gson.fromJson(response, new TypeToken<List<Jyxmsz>>() {
                }.getType());
                DataSupport.saveAll(listJyxmsz);
//                Gson gson = new Gson();
//                Jyxmsz[] jyxmszs = gson.fromJson(response, Jyxmsz[].class);
//                for(int i=0;i<jyxmszs.length;i++){
//                    jyxmszs[i].saveOrUpdate("xmbh=?",jyxmszs[i].getXmbh());
//                }
                mList = DataSupport.where("gq=?", "1").find(Jyxmsz.class);
                mSelloutFragment.notifyAdapter(mList);
                mLoad.setVisibility(View.GONE);
            }
        });
    }

    @Subscribe
    public void SaleOut(SaleOut event){
        mSelloutFragment.addGuqing(event.mJyxmsz);
        mLoad.setVisibility(View.GONE);
    }

    @Subscribe
    public void startAnim(StartAnim event){
        anim();
    }

    private void anim() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLoad.setVisibility(View.VISIBLE);
            }
        });
    }

    @Subscribe
    public void stopAnim(StopAnim event){
        mLoad.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
