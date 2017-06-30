package com.duowei.kitchen_china.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.SettingsActivity;
import com.duowei.kitchen_china.bean.Cfpb2;
import com.duowei.kitchen_china.bean.Cfpb_item;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopFragment extends Fragment {
    @BindView(R.id.tv_uncook)
    TextView mTvUncook;
    @BindView(R.id.tv_cooked)
    TextView mTvCooked;
    @BindView(R.id.btn_history)
    Button mBtnHistory;
    @BindView(R.id.btn_search)
    Button mBtnSearch;
    @BindView(R.id.btn_overtime)
    Button mBtnOvertime;
    @BindView(R.id.btn_saleout)
    Button mBtnSaleout;
    @BindView(R.id.btn_setting)
    Button mBtnSetting;
    @BindView(R.id.btn_exit)
    Button mBtnExit;
    Unbinder unbinder;
    private Intent mIntent;

    public TopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_top, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        return inflate;
    }
    //待煮菜品
    public void setListCfpb(List<Cfpb2>listCfpb){
        mTvUncook.setText(listCfpb.size()+"种");
        float foodCount=0;
        for(int i=0;i<listCfpb.size();i++){
            List<Cfpb_item> list = listCfpb.get(i).getListCfpb();
            for(int j=0;j<list.size();j++){
                foodCount+=list.get(j).sl1;
            }
        }
        mTvCooked.setText(foodCount+"份");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_history, R.id.btn_search, R.id.btn_overtime, R.id.btn_saleout, R.id.btn_setting, R.id.btn_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_history:
                break;
            case R.id.btn_search:
                break;
            case R.id.btn_overtime:
                break;
            case R.id.btn_saleout:
                break;
            case R.id.btn_setting:
                mIntent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(mIntent);
                break;
            case R.id.btn_exit:
                getActivity().finish();
                break;
        }
    }
}
