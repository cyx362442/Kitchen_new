package com.duowei.kitchen_china.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.bean.Cfpb2;
import com.duowei.kitchen_china.bean.Cfpb_item;
import com.duowei.kitchen_china.dialog.DigitInput;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-06-22.
 */

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.ViewHold> {
    private int index=0;
    private Context context;
    private LayoutInflater mLayoutInflater;
    private List<Cfpb2> listCfpb;
    private List<Cfpb_item>listCfpb_item;

    private onItemClickListener itemListener;
    private onContinueClickListener continueLisener;

    public RecAdapter(Context context,List<Cfpb2> listCfpb) {
        this.context = context;
        this.listCfpb = listCfpb;
        mLayoutInflater = LayoutInflater.from(context);
        listCfpb_item=new ArrayList<>();
    }

    public void setList(List<Cfpb2> listCfpb){
        this.listCfpb=listCfpb;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public interface onItemClickListener{
        void setOnItemClickListener(int index);
    }
    public interface onContinueClickListener{
        void setOnContinueClickListener(int index,float num);
    }

    public void setOnItemClickListener(onItemClickListener itemListener){
        this.itemListener=itemListener;
    }
    public void setOnContinueClickListener(onContinueClickListener continueLisener){
        this.continueLisener=continueLisener;
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = mLayoutInflater.inflate(R.layout.recycle_item, null);
        ViewHold viewHold = new ViewHold(inflate);
        //顶部子RecycleView
        viewHold.mRecyclerView = (RecyclerView) inflate.findViewById(R.id.recycleView_item);
        viewHold.mRecyclerView.addItemDecoration(new SpacesItemDecoration(10));//设置顶部子item边距
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//横向滑动
        viewHold.mRecyclerView.setLayoutManager(linearLayoutManager);
        viewHold.mRecAdapterItem = new RecAdapter_item(context, listCfpb_item);
        viewHold.mRecyclerView.setAdapter(viewHold.mRecAdapterItem);

        viewHold.mLl= (LinearLayout) inflate.findViewById(R.id.linearLayout);
        viewHold.mTvName= (TextView) inflate.findViewById(R.id.tv_name);
        viewHold.mTvNum= (TextView) inflate.findViewById(R.id.tv_num);
        viewHold.mTvDw= (TextView) inflate.findViewById(R.id.tv_dw);
        viewHold.btnContinue= (Button) inflate.findViewById(R.id.btn_continue);
        viewHold.mTvPosition= (TextView) inflate.findViewById(R.id.tv_position);
        return viewHold;
    }

    @Override
    public void onBindViewHolder(ViewHold holder, final int position) {
        float count=0;
        final Cfpb2 cfpb = listCfpb.get(position);
        holder.mTvName.setText(cfpb.getXmmc());
        //获取单品总数量
        for(Cfpb_item cfpb_item:cfpb.getListCfpb()){
            count+=cfpb_item.sl1;
        }
        holder.mTvNum.setText(count+"");
        holder.mTvDw.setText(cfpb.getDw());
        holder.mTvPosition.setText((position+1)+"");
        if(position==index){
            holder.mLl.setBackgroundResource(R.drawable.shape_blue);
        }else{
            holder.mLl.setBackgroundResource(R.drawable.shape_green);
        }

        //刷新顶部子Recycleview
        holder.mRecAdapterItem.setListCfpb_item(cfpb.getListCfpb());
        holder.mRecAdapterItem.notifyDataSetChanged();

        holder.mLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemListener.setOnItemClickListener(position);
            }
        });
        //继续按键点击事件
        final float finalCount = count;
        holder.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(finalCount <=1){//数量小于等于1，直接删创造
                    continueLisener.setOnContinueClickListener(position,cfpb.getSl());
                }else{//数量大于1，修改数量
                    final DigitInput digitInput = DigitInput.instance();
                    digitInput.show(context,"请输入数量","数量：",finalCount);
                    digitInput.setOnconfirmClick(new DigitInput.OnconfirmClick() {
                        @Override
                        public void confirmListener(String title, String inputMsg) {
                            continueLisener.setOnContinueClickListener(position,Float.parseFloat(inputMsg));
                            digitInput.cancel();
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listCfpb.size();
    }

    public class ViewHold extends RecyclerView.ViewHolder{
        public ViewHold(View itemView) {
            super(itemView);
        }
        LinearLayout mLl;
        RecyclerView mRecyclerView;
        TextView mTvName;
        TextView mTvNum;
        TextView mTvDw;
        TextView mTvPosition;
        Button btnContinue;
        RecAdapter_item mRecAdapterItem;
    }
}
