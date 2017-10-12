package com.duowei.kitchen_china.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.bean.Cfpb;
import com.duowei.kitchen_china.bean.Cfpb_item;
import com.duowei.kitchen_china.uitls.PreferenceUtils;

/**
 * Created by Administrator on 2017-08-19.
 */

public class LvAdapter extends BaseAdapter {
    private Cfpb mCfpb;
    private final LayoutInflater mLayoutInflater;

    public LvAdapter(Cfpb cfpb, Context context) {
        mCfpb = cfpb;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mCfpb.getListCfpb().size();
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
        ViewHold hold;
        if(convertView==null){
            convertView=mLayoutInflater.inflate(R.layout.listview_item,null);
            hold=new ViewHold();
            hold.tvZh= (TextView) convertView.findViewById(R.id.tv_zh);
            hold.tvPz= (TextView) convertView.findViewById(R.id.tv_pz);
            hold.tvSl= (TextView) convertView.findViewById(R.id.tv_sl);
            convertView.setTag(hold);
        }else{
            hold= (ViewHold) convertView.getTag();
        }
        Cfpb_item cfpbItem = mCfpb.getListCfpb().get(position);
        hold.tvZh.setText(cfpbItem.czmc1);
        if(cfpbItem.pz.contains("&lt;")&&cfpbItem.pz.contains("&gt;")){
            cfpbItem.pz=cfpbItem.pz.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
        }
        hold.tvPz.setText(cfpbItem.pz);
        hold.tvSl.setText("×"+cfpbItem.sl1);
        return convertView;
    }
    class ViewHold{
        public TextView tvZh;
        public TextView tvPz;
        public TextView tvSl;
    }
}
