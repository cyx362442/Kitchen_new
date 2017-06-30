package com.duowei.kitchen_china.httputils;


import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.duowei.kitchen_china.bean.Cfpb2;
import com.duowei.kitchen_china.bean.Cfpb_item;
import com.duowei.kitchen_china.event.OrderFood;
import com.duowei.kitchen_china.event.UpdateCfpb;
import com.duowei.kitchen_china.uitls.DateTimes;
import com.duowei.kitchen_china.uitls.PreferenceUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017-06-23.
 */

public class Post {

    private Post() {}

    private static Post post = null;

    public synchronized static Post getInstance() {
        if (post == null) {
            post = new Post();
        }
        return post;
    }

    private List<Cfpb2> listCfpb = new ArrayList<>();

    public synchronized void setPost7(String sql) {
        DownHTTP.postVolley7(Net.url, sql, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }

            @Override
            public void onResponse(String response) {
                if (response.contains("richado")) {
                    EventBus.getDefault().post(new UpdateCfpb());
                }
            }
        });
    }

    public synchronized void postCfpb(String sql) {
        DownHTTP.postVolley6(Net.url, sql, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(String response) {
                listCfpb.clear();
                if (response.equals("]")) {

                }else{
                    Gson gson = new Gson();
                    Cfpb2[] cfpb2 = gson.fromJson(response, Cfpb2[].class);
                    String str = "";
                    for (int i = 0; i < cfpb2.length; i++) {
                        //抓取每个单品对应的餐桌数据集
                        for (int j = 0; j < cfpb2.length; j++) {
                            if ((cfpb2[j].getXmbh()).equals(cfpb2[i].getXmbh())) {
                                Cfpb_item cfpb_item = new Cfpb_item(cfpb2[j].getXmbh(), cfpb2[j].getCzmc(), cfpb2[j].getSl(), cfpb2[j].getFzs(), cfpb2[j].getXH(),cfpb2[j].getPz());
                                List<Cfpb_item> list = cfpb2[i].getListCfpb();
                                list.add(cfpb_item);
                                cfpb2[i].setListCfpb(list);
                            }
                        }
                        //按单品编号不同进行分组
                        if (!str.contains(cfpb2[i].getXmbh())) {
                            listCfpb.add(cfpb2[i]);
                            str = str + cfpb2[i].getXmbh() + "-";
                        }
                    }
                }
                EventBus.getDefault().post(new OrderFood(listCfpb));
            }
        });
    }
    public void getServerTime(){
        String sql="select CONVERT(varchar(100),getdate(),120) as fwqsj from cfpb|";
        DownHTTP.postVolley6(Net.url, sql, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    String fwqsj = jsonArray.getJSONObject(0).getString("fwqsj");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date datetime = dateFormat.parse(fwqsj);
                    long time = datetime.getTime();
                    DateTimes.serverTime=time;
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
