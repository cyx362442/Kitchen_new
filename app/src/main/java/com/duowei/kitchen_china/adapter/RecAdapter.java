package com.duowei.kitchen_china.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.bean.Cfpb;
import com.duowei.kitchen_china.bean.Cfpb_item;
import com.duowei.kitchen_china.dialog.DigitInput;
import com.duowei.kitchen_china.uitls.ColorAnim;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017-06-22.
 */

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.ViewHold> {
    private int index=0;
    private Context context;
    private LayoutInflater mLayoutInflater;
    private List<Cfpb> listCfpb;
    private List<Cfpb_item>listCfpb_item;

    private onItemClickListener itemListener;
    private onContinueClickListener continueLisener;

    public RecAdapter(Context context,List<Cfpb> listCfpb) {
        this.context = context;
        this.listCfpb = listCfpb;
        mLayoutInflater = LayoutInflater.from(context);
        listCfpb_item=new ArrayList<>();
    }

    public void setList(List<Cfpb> listCfpb){
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
        viewHold.mTvBeizhu= (TextView) inflate.findViewById(R.id.tv_beizhu);
        viewHold.mTvNum= (TextView) inflate.findViewById(R.id.tv_num);
        viewHold.mTvDw= (TextView) inflate.findViewById(R.id.tv_dw);
        viewHold.btnContinue= (Button) inflate.findViewById(R.id.btn_continue);
        return viewHold;
    }

    @Override
    public void onBindViewHolder(ViewHold holder, final int position) {
        float count=0;
        final Cfpb cfpb = listCfpb.get(position);
        holder.mTvName.setText(cfpb.getXmmc());
        List<Cfpb_item> listCfpb_item = cfpb.getListCfpb();

        //获取当前菜品对应第一个的口味备注
        if(!TextUtils.isEmpty(listCfpb_item.get(0).pz)){
            holder.mTvBeizhu.setVisibility(View.VISIBLE);
            holder.mTvBeizhu.setText(listCfpb_item.get(0).pz);
        }else if(TextUtils.isEmpty(listCfpb_item.get(0).pz)){
            holder.mTvBeizhu.setVisibility(View.INVISIBLE);
        }
        //获取每列单品总数量
        for(Cfpb_item cfpb_item: listCfpb_item){
            count+=cfpb_item.sl1;
        }

        holder.mTvNum.setText(count+"");
        holder.mTvDw.setText(cfpb.getDw());

        //刷新顶部子Recycleview
        holder.mRecAdapterItem.setListCfpb_item(listCfpb_item);
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
                if(finalCount <=1){//数量小于等于1，直接删删
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
        TextView mTvBeizhu;
        TextView mTvNum;
        TextView mTvDw;
        Button btnContinue;
        RecAdapter_item mRecAdapterItem;
    }
}
