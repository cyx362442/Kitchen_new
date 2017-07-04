package com.duowei.kitchen_china.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.event.SearchFood;

import org.greenrobot.eventbus.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopFragment2 extends Fragment implements View.OnClickListener {

    public TopFragment2() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_top_fragment2, container, false);
        inflate.findViewById(R.id.ll_return).setOnClickListener(this);
        return inflate;
    }

    @Override
    public void onClick(View view) {
        EventBus.getDefault().post(new SearchFood(false));
    }
}
