package com.duowei.kitchen_china.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.bean.Jyxmsz;

import java.util.List;

/**
 * Created by Administrator on 2017-07-01.
 */

public class SellOutAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mLayoutInflater;
    private List<Jyxmsz> listJyxmsz;
    private int index=0;

    public SellOutAdapter(Context context, List<Jyxmsz> listJyxmsz) {
        this.context = context;
        this.listJyxmsz = listJyxmsz;
        mLayoutInflater=LayoutInflater.from(context);
    }

    public void setList(List<Jyxmsz>listJyxmsz){
        this.listJyxmsz=listJyxmsz;
    }

    public void setIndex(int index){
        this.index=index;
    }

    @Override
    public int getCount() {
        return listJyxmsz.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int postion, View convertView, ViewGroup viewGroup) {
        ViewHold hold=null;
        Jyxmsz jyxmsz=null;
        if(convertView==null){
            convertView=mLayoutInflater.inflate(R.layout.sellout_item,null);
            hold=new ViewHold();
            hold.linearLayout= (LinearLayout) convertView.findViewById(R.id.linearLayout);
            hold.tvName= (TextView) convertView.findViewById(R.id.tv_name);
            hold.tvBianma= (TextView) convertView.findViewById(R.id.tv_bianma);
            hold.tvZhujifu= (TextView) convertView.findViewById(R.id.tv_zhujifu);
            convertView.setTag(hold);
        }else{
            hold= (ViewHold) convertView.getTag();
        }
        jyxmsz = listJyxmsz.get(postion);
        hold.tvName.setText(jyxmsz.getXmmc());
        hold.tvBianma.setText(jyxmsz.getXmbh());
        hold.tvZhujifu.setText(jyxmsz.getPy());
        if(postion==index){
            hold.linearLayout.setBackgroundResource(R.color.colorBlue);
        }else{
            hold.linearLayout.setBackgroundResource(R.color.white);
        }
        return convertView;
    }
    class ViewHold{
        LinearLayout linearLayout;
        TextView tvName;
        TextView tvBianma;
        TextView tvZhujifu;
    }
}
