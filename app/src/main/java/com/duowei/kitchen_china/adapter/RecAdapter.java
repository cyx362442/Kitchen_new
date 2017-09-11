package com.duowei.kitchen_china.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.duowei.kitchen_china.dialog.PopuShow;
import com.duowei.kitchen_china.uitls.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;


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
    private  PopuShow mPopuShow;//点击菜品名称弹出popuwindow
    private  boolean mMakeModel;

    public RecAdapter(Context context,List<Cfpb> listCfpb) {
        this.context = context;
        this.listCfpb = listCfpb;
        mLayoutInflater = LayoutInflater.from(context);
        listCfpb_item=new ArrayList<>();
        mPopuShow = PopuShow.getInstance(context);
        mMakeModel = PreferenceUtils.getInstance(context).getMakeModel("spf_makeModel", false);
    }

    public void setList(List<Cfpb> listCfpb){
        this.listCfpb=listCfpb;
        if(mPopuShow.isShow()){
            mPopuShow.dissPopu();
        }
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
        viewHold.mLlMake= (LinearLayout) inflate.findViewById(R.id.ll_make);
        if(mMakeModel==true){//是否启用制作模式
            viewHold.mLlMake.setVisibility(View.VISIBLE);
        }else{
            viewHold.mLlMake.setVisibility(View.GONE);
        }

        viewHold.mTvName= (TextView) inflate.findViewById(R.id.tv_name);
        viewHold.mTvBeizhu= (TextView) inflate.findViewById(R.id.tv_beizhu);
        viewHold.mTvNum= (TextView) inflate.findViewById(R.id.tv_num);
        viewHold.mTvDw= (TextView) inflate.findViewById(R.id.tv_dw);
        viewHold.mTvCooking= (TextView) inflate.findViewById(R.id.tv_cooking);
        viewHold.mTvUnCook= (TextView) inflate.findViewById(R.id.tv_uncook);
        viewHold.btnContinue= (Button) inflate.findViewById(R.id.btn_continue);
        return viewHold;
    }

    @Override
    public void onBindViewHolder(final ViewHold holder, final int position) {
        float count=0;//总数
        float done=0;//制作中
        final Cfpb cfpb = listCfpb.get(position);
        holder.mTvName.setText(cfpb.getXmmc());
        List<Cfpb_item> listCfpb_item = cfpb.getListCfpb();
        //获取每列单品总数量
        for(Cfpb_item cfpb_item: listCfpb_item){
            count+=cfpb_item.sl1;
            if("1".equals(cfpb_item.getBy10())){
                done+=cfpb_item.sl1;
            }
        }

        if(mMakeModel==true){
            holder.mTvBeizhu.setVisibility(View.GONE);
        }else{
            String pz = listCfpb_item.get(0).pz;
            if(!TextUtils.isEmpty(pz)){
                holder.mTvBeizhu.setVisibility(View.VISIBLE);
                if(pz.contains("&lt;")&&pz.contains("&gt;")){
                    pz=pz.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
                }
                holder.mTvBeizhu.setText(pz);
            }else if(TextUtils.isEmpty(pz)){
                holder.mTvBeizhu.setVisibility(View.INVISIBLE);
            }
        }

        holder.mTvNum.setText(count+"");
        holder.mTvDw.setText(cfpb.getDw());
        holder.mTvCooking.setText(done+"");
        holder.mTvUnCook.setText((count-done)+"");

        //刷新顶部子Recycleview
        holder.mRecAdapterItem.setListCfpb_item(listCfpb_item);
        holder.mRecAdapterItem.notifyDataSetChanged();

        holder.mLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemListener.setOnItemClickListener(position);
            }
        });

        //最后一次点继续的单品，还显示在列表中的（数量》=1），置为蓝色
        if(mMakeModel==false){
            if("1".equals(cfpb.getBy9())){
                holder.mTvName.setTextColor(context.getResources().getColor(R.color.colorBlue));
            }else{
                holder.mTvName.setTextColor(context.getResources().getColor(R.color.gray_dark));
            }
        }else if(mMakeModel==true){//制作模式
            if("1".equals(cfpb.getBy10())){
                holder.mTvName.setTextColor(context.getResources().getColor(R.color.colorBlue));
            }else{
                holder.mTvName.setTextColor(context.getResources().getColor(R.color.gray_dark));
            }
        }

        //超时单品
        String cssj = cfpb.getCssj();
        if(!TextUtils.isEmpty(cssj)&&cfpb.getFzs()>Integer.parseInt(cssj)){
            holder.btnContinue.setBackgroundResource(R.drawable.shape_contiune_outtime);
            holder.mRecyclerView.setBackgroundResource(R.drawable.cook_top_outtime);
        }else{
            holder.btnContinue.setBackgroundResource(R.drawable.shape_continue);
            holder.mRecyclerView.setBackgroundResource(R.drawable.cook_top_greennormal);
        }

        //继续按键点击事件
        final float finalCount = count;
        holder.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMakeModel==true){//启用了制作模式
                    continueLisener.setOnContinueClickListener(position,0);
                    return;
                }
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
        //点击菜品名称弹出popuwindow
        holder.mTvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopuShow.showPopuWindow(context,view,cfpb);
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
        LinearLayout mLlMake;
        RecyclerView mRecyclerView;
        TextView mTvName;
        TextView mTvBeizhu;
        TextView mTvNum;
        TextView mTvDw;
        TextView mTvCooking;
        TextView mTvUnCook;
        Button btnContinue;
        RecAdapter_item mRecAdapterItem;
    }
}
