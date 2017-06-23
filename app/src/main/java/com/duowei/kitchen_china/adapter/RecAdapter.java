package com.duowei.kitchen_china.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.bean.Cfpb;
import com.duowei.kitchen_china.bean.Cfpb_item;

import java.util.List;

/**
 * Created by Administrator on 2017-06-22.
 */

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.ViewHold> {
    private Context context;
    private LayoutInflater mLayoutInflater;
    private List<Cfpb> listCfpb;

    public RecAdapter(Context context,List<Cfpb> listCfpb) {
        this.context = context;
        this.listCfpb = listCfpb;
        mLayoutInflater = LayoutInflater.from(context);
//        Cfpb cfpb = listCfpb.get(0);
//        List<Cfpb_item> list = cfpb.getList();
//        Cfpb_item cfpb_item = list.get(0);
//        Log.e("=====",cfpb_item.toString());
    }

    public void setList(List<Cfpb> listCfpb){
        this.listCfpb=listCfpb;
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = mLayoutInflater.inflate(R.layout.recycle_item, null);
        ViewHold viewHold = new ViewHold(inflate);
        viewHold.mRecyclerView = (RecyclerView) inflate.findViewById(R.id.recycleView_item);
        viewHold.mTvName= (TextView) inflate.findViewById(R.id.tv_name);
        viewHold.mTvNum= (TextView) inflate.findViewById(R.id.tv_num);
        return viewHold;
    }

    @Override
    public void onBindViewHolder(ViewHold holder, int position) {
        Cfpb cfpb = listCfpb.get(position);
        holder.mTvName.setText(cfpb.getXMMC());
        holder.mTvNum.setText(cfpb.getZjsl()+cfpb.getDW());

        List<Cfpb_item> list = cfpb.getList();
        Cfpb_item cfpb_item = list.get(0);
        Log.e("=====",cfpb_item.toString());
    }

    @Override
    public int getItemCount() {
        return listCfpb.size();
    }

    public class ViewHold extends RecyclerView.ViewHolder{
        public ViewHold(View itemView) {
            super(itemView);
        }
        RecyclerView mRecyclerView;
        TextView mTvName;
        TextView mTvNum;
    }
}
