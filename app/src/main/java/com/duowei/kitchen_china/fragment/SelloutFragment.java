package com.duowei.kitchen_china.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.adapter.SellOutAdapter;
import com.duowei.kitchen_china.bean.Jyxmsz;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelloutFragment extends Fragment {
    List<Jyxmsz> listJyxmsz;
    private SellOutAdapter mAdapter;

    public SelloutFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_sellout, container, false);
        listJyxmsz=new ArrayList<>();
        ListView lv = (ListView) inflate.findViewById(R.id.listView);
        mAdapter = new SellOutAdapter(getActivity(), listJyxmsz);
        lv.setAdapter(mAdapter);
        return inflate;
    }
    public void notifyAdapter(List<Jyxmsz>listJyxmsz){
        mAdapter.setList(listJyxmsz);
        mAdapter.notifyDataSetChanged();
    }
}
