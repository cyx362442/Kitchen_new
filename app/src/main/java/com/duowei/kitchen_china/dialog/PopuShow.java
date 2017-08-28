package com.duowei.kitchen_china.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.adapter.LvAdapter;
import com.duowei.kitchen_china.adapter.MaxListView;
import com.duowei.kitchen_china.bean.Cfpb;

/**
 * Created by Administrator on 2017-07-10.
 */

public class PopuShow {

    private static TextView tv;
    private static MaxListView lv;
    private static View popView;

    private PopuShow(Context context){}
    private static PopuShow ps=null;
    private static PopupWindow mPopupWindow;
    public static PopuShow getInstance(Context context){
        if(ps==null){
            ps=new PopuShow(context);
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();

            popView = LayoutInflater.from(context).inflate(R.layout.popu_item,null);
            mPopupWindow = new PopupWindow(popView, width/3+20, RecyclerView.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());                            // 指定 PopupWindow 的背景
            mPopupWindow.setFocusable(false);                   // 设定 PopupWindow 取的焦点，创建出来的 PopupWindow 默认无焦点
        }
        return ps;
    }
    public void showPopuWindow(Context context,View view,Cfpb cfpb){
        if(mPopupWindow.isShowing()){
            mPopupWindow.dismiss();
        }
        mPopupWindow.showAsDropDown(view);
        tv = (TextView) popView.findViewById(R.id.tv_popu);
        lv = (MaxListView) popView.findViewById(R.id.listView);
        lv.setDivider(new ColorDrawable(Color.parseColor("#ffffff")));
        lv.setDividerHeight(1);
        lv.setListViewHeight(450);
        LvAdapter lvAdapter = new LvAdapter(cfpb, context);
        lv.setAdapter(lvAdapter);
        tv.setText(cfpb.getXmmc());
    }
    public boolean isShow(){
        return mPopupWindow.isShowing();
    }
    public void dissPopu(){
        mPopupWindow.dismiss();
    }
}
