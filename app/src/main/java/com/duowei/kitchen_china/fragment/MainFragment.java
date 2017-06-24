package com.duowei.kitchen_china.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.adapter.RecAdapter;
import com.duowei.kitchen_china.adapter.SpacesItemDecoration;
import com.duowei.kitchen_china.bean.Cfpb2;
import com.duowei.kitchen_china.bean.Cfpb_item;
import com.duowei.kitchen_china.event.StartProgress;
import com.duowei.kitchen_china.httputils.Net;
import com.duowei.kitchen_china.httputils.Post;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements RecAdapter.onItemClickListener, RecAdapter.onContinueClickListener {

    private RecAdapter mRecAdapter;
    private int tempList;
    private int currentPosition=0;

    public MainFragment() {
        // Required empty public constructor
    }

    private List<Cfpb2> listCfpb;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_main, container, false);
        listCfpb=new ArrayList<>();
        RecyclerView rv = (RecyclerView) inflate.findViewById(R.id.recycleView);
        rv.setLayoutManager(new GridLayoutManager(getActivity(),4));//gridview布局,4列
        rv.addItemDecoration(new SpacesItemDecoration(3));//设置item边距
        rv.setItemAnimator(new DefaultItemAnimator());
        mRecAdapter = new RecAdapter(getActivity(), listCfpb);
        rv.setAdapter(mRecAdapter);
        mRecAdapter.setOnItemClickListener(this);
        mRecAdapter.setOnContinueClickListener(this);
        return inflate;
    }
    public void setRecycleView(List<Cfpb2>list){
        mRecAdapter.setList(listCfpb=list);
        if(list.size()==tempList){
            mRecAdapter.notifyDataSetChanged();
        }else if(list.size()<tempList){//list变小，启用删除动画
            mRecAdapter.notifyItemRemoved(currentPosition);
            mRecAdapter.notifyItemRangeChanged(currentPosition,mRecAdapter.getItemCount());
        }else if(list.size()>tempList){//list变大，启用增加动画
            mRecAdapter.notifyItemInserted(list.size()-1);
        }
        tempList=list.size();
    }

    public void updateSuccess(){
        Post.getInstance().postCfpb(Net.sql_cfpb);
    }

    @Override
    public void setOnItemClickListener(int index) {
        mRecAdapter.setIndex(index);
        mRecAdapter.notifyDataSetChanged();
    }
    /**继续按键点击事件*/
    @Override
    public void setOnContinueClickListener(int index,float num) {
        currentPosition=index;
        EventBus.getDefault().post(new StartProgress());
        String sql="";
        mRecAdapter.setIndex(index);
        List<Cfpb_item> listCfpb = this.listCfpb.get(index).getListCfpb();
        for(int i=0;i<listCfpb.size();i++){
            if(num>0){
                Cfpb_item cfpbItem = listCfpb.get(i);
                if(cfpbItem.sl1<=num){//当前桌号待删除的单品数量<=num,直接删除这行
                    sql+="delete from cfpb where xh='"+cfpbItem.xh+"'|";
                }else {//当前桌号待删除的单品数量>num,更新己用数量字段
                    sql+="update cfpb set ywcsl=isnull(ywcsl,0)+"+num+" where xh='"+cfpbItem.xh+"'|";
                }
                num=num-cfpbItem.sl1;
            }else{
                break;
            }
        }
        Post.getInstance().setPost7(sql);
    }
}
