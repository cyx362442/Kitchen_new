package com.duowei.kitchen_china.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.bean.Cfpb_item;
import com.duowei.kitchen_china.uitls.PreferenceUtils;

import java.util.List;

/**
 * Created by Administrator on 2017-06-23.
 */

public class RecAdapter_item extends RecyclerView.Adapter<RecAdapter_item.ViewHold>{
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Cfpb_item> listCfpb_item;
    private String mTextSize;

    public RecAdapter_item(Context context, List<Cfpb_item> listCfpb_item) {
        this.listCfpb_item = listCfpb_item;
        mContext=context;
        mLayoutInflater=LayoutInflater.from(context);
        mTextSize = PreferenceUtils.getInstance(context).getListSize("listSize", context.getString(R.string.normal));
    }

    public void setListCfpb_item(List<Cfpb_item> listCfpb_item){
        this.listCfpb_item=listCfpb_item;
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = mLayoutInflater.inflate(R.layout.recycle_item2, null);
        ViewHold viewHold = new ViewHold(inflate);
        viewHold.tvTable= (TextView) inflate.findViewById(R.id.tv_table);
        viewHold.tvPasstime= (TextView) inflate.findViewById(R.id.tv_passtime);

        if(mTextSize.equals(mContext.getString(R.string.large))){//大号字体
            viewHold.tvTable.setTextSize(40);
            viewHold.tvPasstime.setTextSize(40);
        }
        return viewHold;
    }

    @Override
    public void onBindViewHolder(ViewHold holder, int position) {
        Cfpb_item cfpb_item = listCfpb_item.get(position);
        holder.tvTable.setText(cfpb_item.czmc1);
        holder.tvPasstime.setText(cfpb_item.fzs+"分钟");
    }

    @Override
    public int getItemCount() {
        return listCfpb_item.size();
    }

    public class ViewHold extends RecyclerView.ViewHolder{
        public ViewHold(View itemView) {
            super(itemView);
        }
        TextView tvTable;
        TextView tvPasstime;
    }
}
