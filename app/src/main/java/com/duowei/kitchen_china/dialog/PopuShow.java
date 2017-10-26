package com.duowei.kitchen_china.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.NumberFormat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.activity.MainActivity;
import com.duowei.kitchen_china.adapter.LvAdapter;
import com.duowei.kitchen_china.adapter.MaxListView;
import com.duowei.kitchen_china.bean.Cfpb;
import com.duowei.kitchen_china.bean.Plxx;
import com.duowei.kitchen_china.httputils.DownHTTP;
import com.duowei.kitchen_china.httputils.Net;
import com.duowei.kitchen_china.httputils.VolleyResultListener;
import com.duowei.kitchen_china.uitls.NumUtil;
import com.duowei.kitchen_china.uitls.PreferenceUtils;
import com.google.gson.Gson;

/**
 * Created by Administrator on 2017-07-10.
 */

public class PopuShow {

    private static TextView tv;
    private static MaxListView lv;
    private static View popView;
    private static boolean recipes;
    private TextView mTvRecipes;

    private PopuShow(){}
    private static PopuShow ps=null;
    private static PopupWindow mPopupWindow;
    public static PopuShow getInstance(Context context,String colums){
        recipes = PreferenceUtils.getInstance(context).getRecipes("spf_recipes", false);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        if(ps==null){
            ps=new PopuShow();
            popView = LayoutInflater.from(context).inflate(R.layout.popu_item,null);
            mPopupWindow = new PopupWindow(popView, width/3, RecyclerView.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());                            // 指定 PopupWindow 的背景
            mPopupWindow.setFocusable(false);                   // 设定 PopupWindow 取的焦点，创建出来的 PopupWindow 默认无焦点
        }
        int colum = Integer.parseInt(colums);
        if(recipes){//显示配方信息
            mPopupWindow.setWidth(width*9/10);
            mPopupWindow.setHeight(height*9/10);
        }else{
            mPopupWindow.setWidth(width/colum);
            mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        }
        return ps;
    }
    public void showPopuWindow(Context context,View view,Cfpb cfpb){
        if(mPopupWindow.isShowing()){
            mPopupWindow.dismiss();
        }
        tv = (TextView) popView.findViewById(R.id.tv_popu);
        tv.setText(cfpb.getXmmc());
        lv = (MaxListView) popView.findViewById(R.id.listView);
        mTvRecipes = (TextView) popView.findViewById(R.id.tv_recipes);
        if(recipes){//显示配方信息
            mPopupWindow.showAtLocation(MainActivity.mWindow.getDecorView(), Gravity.CENTER, 0, 0);
            Http_Plxx(cfpb);
            mTvRecipes.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);
        }else{
            mPopupWindow.showAsDropDown(view);
            lv.setVisibility(View.VISIBLE);
            mTvRecipes.setVisibility(View.GONE);
            lv.setDivider(new ColorDrawable(Color.parseColor("#ffffff")));
            lv.setDividerHeight(1);
            lv.setListViewHeight(450);
            LvAdapter lvAdapter = new LvAdapter(cfpb, context);
            lv.setAdapter(lvAdapter);
        }
    }
    //显示配方信息
    private void Http_Plxx(Cfpb cfpb) {
        String sql="select yclmc,syzjldw,sl from plxx where xmbh='"+cfpb.getXmbh()+"'|";
        DownHTTP.postVolley6(Net.url, sql, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTvRecipes.setText("配方信息获取失败");
            }
            @Override
            public void onResponse(String response) {
                if("]".equals(response)){
                    mTvRecipes.setText("无配方信息");
                }else{
                    String str="";
                    Gson gson = new Gson();
                    Plxx[] plxxes = gson.fromJson(response, Plxx[].class);
                    for(int i=0;i<plxxes.length;i++){
                        str=str+(i+1)+"、"+plxxes[i].getYclmc()+"\t\t\t\t\t\t\t"+
                                NumUtil.clearZero(plxxes[i].getSl())+plxxes[i].getSyzjldw()+"\n";
                    }
                    mTvRecipes.setText(str);
                }
            }
        });
    }

    public boolean isShow(){
        return mPopupWindow.isShowing();
    }
    public void dissPopu(){
        mPopupWindow.dismiss();
    }
}
