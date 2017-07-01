package com.duowei.kitchen_china.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.VolleyError;
import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.bean.Jyxmsz;
import com.duowei.kitchen_china.fragment.MainFragment;
import com.duowei.kitchen_china.fragment.SelloutFragment;
import com.duowei.kitchen_china.fragment.TopFragment;
import com.duowei.kitchen_china.fragment.UnSellOutFragment;
import com.duowei.kitchen_china.httputils.DownHTTP;
import com.duowei.kitchen_china.httputils.Net;
import com.duowei.kitchen_china.httputils.Post;
import com.duowei.kitchen_china.httputils.VolleyResultListener;
import com.duowei.kitchen_china.uitls.DateTimes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;

import java.util.List;

public class SellOutActivity extends AppCompatActivity {

    private SelloutFragment mSelloutFragment;
    private UnSellOutFragment mUnSellOutFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_out);
        initFragment();
        getJyxmsz();

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
                Gson gson = new Gson();
                List<Jyxmsz> listJyxmsz = gson.fromJson(response, new TypeToken<List<Jyxmsz>>() {
                }.getType());
                DataSupport.saveAll(listJyxmsz);
                List<Jyxmsz> list = DataSupport.where("gq=?", "1").find(Jyxmsz.class);
                mSelloutFragment.notifyAdapter(list);
            }
        });
    }
}
