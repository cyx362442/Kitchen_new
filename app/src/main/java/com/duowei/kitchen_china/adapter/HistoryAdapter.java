package com.duowei.kitchen_china.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.bean.Cfpb;
import com.duowei.kitchen_china.print.PrintHandler;

import java.util.List;

/**
 * Created by Administrator on 2017-07-01.
 */

public class HistoryAdapter extends BaseAdapter{

    private Context context;
    private LayoutInflater mLayoutInflater;
    private List<Cfpb> listCfpb;

    public HistoryAdapter(Context context, List<Cfpb> listCfpb) {
        this.context = context;
        this.listCfpb = listCfpb;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setList(List<Cfpb>listCfpb){
        this.listCfpb=listCfpb;
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
        ViewHold hold=null;
        Cfpb cfpb =null;
        if(convertView==null){
            convertView = mLayoutInflater.inflate(R.layout.history_item, null);
            hold = new ViewHold();
            hold.tvIndex= (TextView) convertView.findViewById(R.id.tv_index);
            hold.tvName= (TextView) convertView.findViewById(R.id.tv_name);
            hold.tvTable= (TextView) convertView.findViewById(R.id.tv_table);
            hold.tvNum= (TextView) convertView.findViewById(R.id.tv_num);
            hold.tvBeizhu= (TextView) convertView.findViewById(R.id.tv_beizhu);
            hold.tvOrderTime= (TextView) convertView.findViewById(R.id.tv_ordertime);
            hold.tvCompleteTime= (TextView) convertView.findViewById(R.id.tv_completetime);
            hold.llPrint= (RelativeLayout) convertView.findViewById(R.id.ll_print);
            convertView.setTag(hold);
        }else{
            hold= (ViewHold) convertView.getTag();
        }
        cfpb = listCfpb.get(position);
        hold.tvIndex.setText((position+1)+"");
        hold.tvName.setText(cfpb.getXmmc());
        hold.tvTable.setText(cfpb.getCzmc());
        hold.tvNum.setText(cfpb.getSl()+"");
        hold.tvBeizhu.setText(cfpb.getPz());
        hold.tvOrderTime.setText(cfpb.getXdsj());
        hold.tvCompleteTime.setText(cfpb.getWcsj());

        //打印
        final Cfpb finalCfpb = cfpb;
        hold.llPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrintHandler.getInstance().printSingle(finalCfpb);
            }
        });
        return convertView;
    }

    class ViewHold {
        TextView tvIndex;
        TextView tvName;
        TextView tvTable;
        TextView tvNum;
        TextView tvBeizhu;
        TextView tvOrderTime;
        TextView tvCompleteTime;
        RelativeLayout llPrint;
    }
}
