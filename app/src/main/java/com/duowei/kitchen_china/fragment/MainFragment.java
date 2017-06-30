package com.duowei.kitchen_china.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.duowei.kitchen_china.print.PrintHandler;
import com.duowei.kitchen_china.uitls.DateTimes;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements RecAdapter.onItemClickListener, RecAdapter.onContinueClickListener {

    private RecAdapter mRecAdapter;
    private List<Cfpb2> listCfpb;
    private List<Cfpb2>listCfpb2;//己完成
    private int tempList;
    private int currentPosition=0;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View inflate = inflater.inflate(R.layout.fragment_main, container, false);
        listCfpb=new ArrayList<>();
        listCfpb2=new ArrayList<>();
        RecyclerView rv = (RecyclerView) inflate.findViewById(R.id.recycleView);
        rv.setLayoutManager(new GridLayoutManager(getActivity(),4));//gridview布局,4列
        rv.addItemDecoration(new SpacesItemDecoration(10));//设置item边距
        rv.setItemAnimator(new DefaultItemAnimator());
        mRecAdapter = new RecAdapter(getActivity(), listCfpb);
        rv.setAdapter(mRecAdapter);
        mRecAdapter.setOnItemClickListener(this);
        mRecAdapter.setOnContinueClickListener(this);
        return inflate;
    }
    public void setRecycleView(List<Cfpb2>list){
        mRecAdapter.setList(listCfpb=list);
        if(list.size()==tempList||list.size()==0){
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
        Post.getInstance(getActivity()).postCfpb(Net.sql_cfpb);
        DataSupport.saveAll(listCfpb2);
        PrintHandler.getInstance().print(listCfpb2);
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

        float tempNum=0;
        Cfpb2 cfpb21=null;
        listCfpb2.clear();
        EventBus.getDefault().post(new StartProgress());
        String sql="";
        mRecAdapter.setIndex(index);
        Cfpb2 cfpb2 = listCfpb.get(index);
        List<Cfpb_item> listCfpb = cfpb2.getListCfpb();
        for(int i=0;i<listCfpb.size();i++){
            if(num>0){
                Cfpb_item cfpbItem = listCfpb.get(i);
                if(cfpbItem.sl1<=num){//当前桌号待删除的单品数量<=num,直接删除这行
                    sql+="delete from cfpb where xh='"+cfpbItem.xh+"'|";
                    tempNum=cfpbItem.sl1;
                }else {//当前桌号待删除的单品数量>num,更新己用数量字段
                    sql+="update cfpb set ywcsl=isnull(ywcsl,0)+"+num+" where xh='"+cfpbItem.xh+"'|";
                    tempNum=num;
                }
                //己完成
                cfpb21 = new Cfpb2(cfpb2.getXH(), cfpb2.getXmbh(), cfpb2.getXmmc(), cfpb2.getDw(),
                            cfpbItem.sl1, cfpbItem.pz, cfpb2.getXdsj(), cfpbItem.czmc1,
                            cfpbItem.fzs, cfpb2.getYhmc(), tempNum, DateTimes.getTime2());
                listCfpb2.add(cfpb21);
                num=num-cfpbItem.sl1;
            }else{
                break;
            }
        }
        Post.getInstance(getActivity()).setPost7(sql);
    }
}
