package com.duowei.kitchen_china.httputils;


import android.content.Context;

import com.android.volley.VolleyError;
import com.duowei.kitchen_china.bean.Cfpb2;
import com.duowei.kitchen_china.bean.Cfpb_item;
import com.duowei.kitchen_china.event.OrderFood;
import com.duowei.kitchen_china.event.UpdateCfpb;
import com.duowei.kitchen_china.uitls.PreferenceUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-06-23.
 */

public class Post {
    private String url;

    private Post(Context context) {
        String serviceIP = PreferenceUtils.getInstance(context).getServiceIp("serviceIP", "");
        url = "http://" + serviceIP + ":2233/server/ServerSvlt?";
    }

    private static Post post = null;

    public synchronized static Post getInstance(Context context) {
        if (post == null) {
            post = new Post(context);
        }
        return post;
    }

    private List<Cfpb2> listCfpb = new ArrayList<>();

    public synchronized void setPost7(String sql) {
        DownHTTP.postVolley7(url, sql, new VolleyResultListener() {
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
        DownHTTP.postVolley6(url, sql, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }

            @Override
            public void onResponse(String response) {
                listCfpb.clear();
                if (response.equals("]")) {

                }else{
                    Gson gson = new Gson();
                    Cfpb2[] cfpbs2 = gson.fromJson(response, Cfpb2[].class);
                    String str = "";
                    for (int i = 0; i < cfpbs2.length; i++) {
                        //抓取每个单品对应的餐桌数据集
                        for (int j = 0; j < cfpbs2.length; j++) {
                            if ((cfpbs2[j].getXmbh()).equals(cfpbs2[i].getXmbh())) {
                                Cfpb_item cfpb_item = new Cfpb_item(cfpbs2[j].getXmbh(), cfpbs2[j].getCzmc(), cfpbs2[j].getSl(), cfpbs2[j].getFzs(), cfpbs2[j].getXH(),cfpbs2[j].getPz());
                                List<Cfpb_item> list = cfpbs2[i].getListCfpb();
                                list.add(cfpb_item);
                                cfpbs2[i].setListCfpb(list);
                            }
                        }
                        //按单品编号不同进行分组
                        if (!str.contains(cfpbs2[i].getXmbh())) {
                            listCfpb.add(cfpbs2[i]);
                            str = str + cfpbs2[i].getXmbh() + "-";
                        }
                    }
                }
                EventBus.getDefault().post(new OrderFood(listCfpb));
            }
        });
    }
}
