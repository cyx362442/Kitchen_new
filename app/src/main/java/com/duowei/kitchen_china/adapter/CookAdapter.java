package com.duowei.kitchen_china.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.bean.Cfpb;
import com.duowei.kitchen_china.bean.Cfpb_item;

/**
 * Created by Administrator on 2017-08-29.
 */

public class CookAdapter extends RecyclerView.Adapter<CookAdapter.ViewHold>{
    private Cfpb mCfpb;
    private LayoutInflater mLayoutInflater;

    public CookAdapter(Cfpb cfpb,Context context) {
        this.mCfpb = cfpb;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = mLayoutInflater.inflate(R.layout.cook_item, parent, false);
        ViewHold viewHold = new ViewHold(inflate);
        viewHold.tvZh= (TextView) inflate.findViewById(R.id.tv_zh);
        viewHold.tvPz= (TextView) inflate.findViewById(R.id.tv_pz);
        viewHold.tvJcfs= (TextView) inflate.findViewById(R.id.tv_jcfs);
        viewHold.tvSl= (TextView) inflate.findViewById(R.id.tv_sl);
        return viewHold;
    }

    @Override
    public void onBindViewHolder(ViewHold holder, int position) {
        Cfpb_item cfpbItem = mCfpb.getListCfpb().get(position);
        holder.tvZh.setText(cfpbItem.czmc1);
        holder.tvPz.setText(cfpbItem.pz);
        holder.tvSl.setText(cfpbItem.sl1+"");
    }

    @Override
    public int getItemCount() {
        return mCfpb.getListCfpb().size();
    }

    class ViewHold extends RecyclerView.ViewHolder{
        public ViewHold(View itemView) {
            super(itemView);
        }
        TextView tvZh;
        TextView tvPz;
        TextView tvJcfs;
        TextView tvSl;
        RadioGroup rg;
    }
}
