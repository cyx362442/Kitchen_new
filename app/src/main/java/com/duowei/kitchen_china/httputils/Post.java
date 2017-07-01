package com.duowei.kitchen_china.httputils;


import android.util.Log;

import com.android.volley.VolleyError;
import com.duowei.kitchen_china.bean.Cfpb;
import com.duowei.kitchen_china.bean.Cfpb_item;
import com.duowei.kitchen_china.bean.Jyxmsz;
import com.duowei.kitchen_china.event.OrderFood;
import com.duowei.kitchen_china.event.UpdateCfpb;
import com.duowei.kitchen_china.uitls.DateTimes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

    private List<Cfpb> listCfpb = new ArrayList<>();

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
                    Cfpb[] cfpb = gson.fromJson(response, Cfpb[].class);
                    String str = "";
                    for (int i = 0; i < cfpb.length; i++) {
                        //抓取每个单品对应的餐桌数据集
                        for (int j = 0; j < cfpb.length; j++) {
                            if ((cfpb[j].getXmbh()).equals(cfpb[i].getXmbh())) {
                                Cfpb_item cfpb_item = new Cfpb_item(cfpb[j].getXmbh(), cfpb[j].getCzmc(), cfpb[j].getSl(), cfpb[j].getFzs(), cfpb[j].getXH(), cfpb[j].getPz());
                                List<Cfpb_item> list = cfpb[i].getListCfpb();
                                list.add(cfpb_item);
                                cfpb[i].setListCfpb(list);
                            }
                        }
                        //按单品编号不同进行分组
                        if (!str.contains(cfpb[i].getXmbh())) {
                            listCfpb.add(cfpb[i]);
                            str = str + cfpb[i].getXmbh() + "-";
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
