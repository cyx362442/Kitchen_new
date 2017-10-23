package com.duowei.kitchen_china.httputils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.bean.Cfpb;
import com.duowei.kitchen_china.bean.Cfpb_item;
import com.duowei.kitchen_china.event.OrderFood;
import com.duowei.kitchen_china.event.ShowCall;
import com.duowei.kitchen_china.event.Update;
import com.duowei.kitchen_china.event.UpdateCfpb;
import com.duowei.kitchen_china.uitls.DateTimes;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

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

    public synchronized void postCall7(String sql){
        DownHTTP.postVolley7(Net.url, sql, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
            @Override
            public void onResponse(String response) {
                if(response.contains("richado")){
                    EventBus.getDefault().post(new ShowCall(2));
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
                        //抓取每个单品所包含的餐桌数据集
                        for (int j = 0; j < cfpb.length; j++) {
                            if ((cfpb[j].getXmbh()).equals(cfpb[i].getXmbh())) {
                                Cfpb_item cfpb_item = new Cfpb_item(cfpb[j].getXmbh(), cfpb[j].getCzmc(),
                                        cfpb[j].getSl(), cfpb[j].getFzs(),
                                        cfpb[j].getXH(), cfpb[j].getPz(),
                                        cfpb[j].getXszt(),cfpb[j].getBy10());
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
                    long currentServerTime = datetime.getTime();
                    DateTimes.serverTime=currentServerTime;

                    /*删除2天之前的历史数据*/
                    long l = currentServerTime - 2*24*60*60*1000;
                    DataSupport.deleteAll(Cfpb.class,"time<'"+l+"'");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void checkUpdate(final Context context, final boolean auto){
        String sql="http://ouwtfo4eg.bkt.clouddn.com/kitchen_china.txt";
        final int version = getAPPVersionCode(context);
        DownHTTP.getVolley(sql, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String versionCode = jsonObject.getString("versionCode");
                    int currentVersion = Integer.parseInt(versionCode);
                    if(currentVersion >version){
                        final String url = jsonObject.getString("url");
                        final String name = jsonObject.getString("name");
                        String msg = jsonObject.getString("msg");
                        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("发现新版本");
                        builder.setIcon(R.mipmap.logo_48);
                        builder.setMessage(msg);
                        builder.setNegativeButton("暂不升级",null);
                        builder.setPositiveButton("立即升级", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                EventBus.getDefault().post(new Update(url,name));
                            }
                        });
                        builder.create().show();
                    }else{
                        if(auto==false){
                            Toast.makeText(context,"当前己是最新版本",Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    //当前APP版本号
    public int getAPPVersionCode(Context ctx) {
        int currentVersionCode = 0;
        PackageManager manager = ctx.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
//            String appVersionName = info.versionName; // 版本名
            currentVersionCode = info.versionCode; // 版本号
//            System.out.println(currentVersionCode + " " + appVersionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return currentVersionCode;
    }
}
