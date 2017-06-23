package com.duowei.kitchen_china.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.adapter.RecAdapter;
import com.duowei.kitchen_china.adapter.SpacesItemDecoration;
import com.duowei.kitchen_china.bean.Cfpb2;
import com.duowei.kitchen_china.bean.Cfpb_item;
import com.duowei.kitchen_china.httputils.DownHTTP;
import com.duowei.kitchen_china.httputils.Net;
import com.duowei.kitchen_china.httputils.Post;
import com.duowei.kitchen_china.httputils.VolleyResultListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements RecAdapter.onItemClickListener, RecAdapter.onContinueClickListener {

    private RecAdapter mRecAdapter;

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
        rv.setLayoutManager(new GridLayoutManager(getActivity(),4));//gridview布局
        rv.addItemDecoration(new SpacesItemDecoration(5));//设置item边距
        mRecAdapter = new RecAdapter(getActivity(), listCfpb);
        rv.setAdapter(mRecAdapter);
        mRecAdapter.setOnItemClickListener(this);
        mRecAdapter.setOnContinueClickListener(this);
        return inflate;
    }
    public void setRecycleView(List<Cfpb2>list){
        mRecAdapter.setList(listCfpb=list);
        mRecAdapter.notifyDataSetChanged();
    }

    public void updateSuccess(){
        Post.getInstance().postCfpb(Net.sql_cfpb);
    }

    @Override
    public void setOnItemClickListener(int index) {
        mRecAdapter.setIndex(index);
        mRecAdapter.notifyDataSetChanged();
    }
    /**继续点击事件*/
    @Override
    public void setOnContinueClickListener(int index) {
        mRecAdapter.setIndex(index);
        Cfpb2 cfpb = listCfpb.get(index);
        Cfpb_item cfpb_item = cfpb.getListCfpb().get(0);
        String sql="delete from cfpb where by1='"+cfpb_item.czmc1+"' and xmbh='"+cfpb_item.xmbh1+"'|";
        Post.getInstance().setPost7(sql);
    }
}
