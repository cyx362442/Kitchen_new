package com.duowei.kitchen_china.dialog;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.duowei.kitchen_china.R;

/**
 * Created by Administrator on 2017-07-10.
 */

public class PopuShow {

    private static TextView tv;
    private PopuShow(Context context){}
    private static PopuShow ps=null;
    private static PopupWindow mPopupWindow;
    public static PopuShow getInstance(Context context){
        if(ps==null){
            ps=new PopuShow(context);
            View popView = LayoutInflater.from(context).inflate(R.layout.popu_item,null);
            tv = (TextView) popView.findViewById(R.id.tv_popu);
            mPopupWindow = new PopupWindow(popView, RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());                            // 指定 PopupWindow 的背景
            mPopupWindow.setFocusable(false);                   // 设定 PopupWindow 取的焦点，创建出来的 PopupWindow 默认无焦点
        }
        return ps;
    }
    public void showPopuWindow(View view,String name){
        if(mPopupWindow.isShowing()){
            mPopupWindow.dismiss();
        }
        mPopupWindow.showAsDropDown(view);
        tv.setText(name);
    }
    public boolean isShow(){
        return mPopupWindow.isShowing();
    }
    public void dissPopu(){
        mPopupWindow.dismiss();
    }
}
