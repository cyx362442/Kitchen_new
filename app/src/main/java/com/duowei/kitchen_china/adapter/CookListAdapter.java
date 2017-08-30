package com.duowei.kitchen_china.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.duowei.kitchen_china.R;

import com.duowei.kitchen_china.bean.Cfpb_item;

import java.util.List;

/**
 * Created by Administrator on 2017-08-29.
 */

public class CookListAdapter extends BaseAdapter {
    private List<Cfpb_item> listCfpb;
    private LayoutInflater mLayoutInflater;

    public CookListAdapter( List<Cfpb_item> listCfpb,Context context) {
        this.listCfpb = listCfpb;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setListCfpb(List<Cfpb_item> listCfpb){
        this.listCfpb=listCfpb;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listCfpb.size();
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHold viewHold=null;
        if(convertView==null){
            convertView=mLayoutInflater.inflate(R.layout.cook_item, null);
            viewHold = new ViewHold();
            viewHold.tvZh= (TextView) convertView.findViewById(R.id.tv_zh);
            viewHold.tvPz= (TextView) convertView.findViewById(R.id.tv_pz);
            viewHold.tvJcfs= (TextView) convertView.findViewById(R.id.tv_jcfs);
            viewHold.tvSl= (TextView) convertView.findViewById(R.id.tv_sl);
            viewHold.btnDone= (Button) convertView.findViewById(R.id.btn_done);
            viewHold.btnComplete= (Button) convertView.findViewById(R.id.btn_complete);
            convertView.setTag(viewHold);
        }else{
            viewHold= (ViewHold) convertView.getTag();
        }
        final Cfpb_item cfpbItem = listCfpb.get(position);
        viewHold.tvZh.setText(cfpbItem.czmc1);
        viewHold.tvPz.setText(cfpbItem.pz.replaceAll("&lt;","<").replaceAll("&gt;",">"));
        viewHold.tvJcfs.setText(cfpbItem.xszt);
        viewHold.tvSl.setText(cfpbItem.sl1+"");
        if("1".equals(cfpbItem.by10)){
            viewHold.btnDone.setBackgroundResource(R.drawable.shape_confirm);
        }else{
            viewHold.btnDone.setBackgroundResource(R.drawable.shape_cancel);
        }
        if("1".equals(cfpbItem.wc)){
            viewHold.btnComplete.setBackgroundResource(R.drawable.shape_confirm);
        }else{
            viewHold.btnComplete.setBackgroundResource(R.drawable.shape_cancel);
        }
        viewHold.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("1".equals(cfpbItem.getBy10())){
                    cfpbItem.setBy10("0");
                }else{
                    cfpbItem.setBy10("1");
                }
                cfpbItem.setWc("0");
                notifyDataSetChanged();
            }
        });
        viewHold.btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("1".equals(cfpbItem.getWc())){
                    cfpbItem.setWc("0");
                }else{
                    cfpbItem.setWc("1");
                }
                cfpbItem.setBy10("0");
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
    class ViewHold{
        TextView tvZh;
        TextView tvPz;
        TextView tvJcfs;
        TextView tvSl;
        Button btnDone;
        Button btnComplete;
    }
}
