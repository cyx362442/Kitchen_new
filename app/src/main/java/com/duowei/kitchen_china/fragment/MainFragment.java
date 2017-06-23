package com.duowei.kitchen_china.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.adapter.RecAdapter;
import com.duowei.kitchen_china.bean.Cfpb;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private RecAdapter mRecAdapter;

    public MainFragment() {
        // Required empty public constructor
    }

    private List<Cfpb> listCfpb;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_main, container, false);
        listCfpb=new ArrayList<>();
        RecyclerView rv = (RecyclerView) inflate.findViewById(R.id.recycleView);
        rv.setLayoutManager(new GridLayoutManager(getActivity(),4));
        mRecAdapter = new RecAdapter(getActivity(), listCfpb);
        rv.setAdapter(mRecAdapter);
        return inflate;
    }
    public void setRecycleView(List<Cfpb>list){
        mRecAdapter.setList(list);
        mRecAdapter.notifyDataSetChanged();
    }
}
