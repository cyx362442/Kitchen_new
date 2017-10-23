package com.duowei.kitchen_china.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.adapter.CallAdapter;
import com.duowei.kitchen_china.bean.Call;
import com.duowei.kitchen_china.event.ShowCall;
import com.duowei.kitchen_china.httputils.DownHTTP;
import com.duowei.kitchen_china.httputils.Net;
import com.duowei.kitchen_china.httputils.VolleyResultListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CallOutFragment extends Fragment {

    private List<Call> mCalls;
    private CallAdapter mAdapter;

    public CallOutFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_call_out, container, false);
        EventBus.getDefault().register(this);
        mCalls = new ArrayList<>();

        RecyclerView rv = (RecyclerView) inflate.findViewById(R.id.rv_out);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new CallAdapter(mCalls);
        rv.setAdapter(mAdapter);
        return inflate;
    }

    @Subscribe
    public void getCall(ShowCall event){
        if(event.show==1||event.show==2){
            String sql="select XH,TableNo,BillNo,YHJ from kdscall order by xdsj desc|";
            DownHTTP.postVolley6(Net.url, sql, new VolleyResultListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
                @Override
                public void onResponse(String response) {
                    if(response.equals("]")){
                        mCalls.clear();
                        mAdapter.setNewData(mCalls);
                        return;
                    }
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Call>>() {
                    }.getType();
                    mCalls = gson.fromJson(response, type);
                    mAdapter.setNewData(mCalls);
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
