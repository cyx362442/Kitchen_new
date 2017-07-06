package com.duowei.kitchen_china.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.activity.PastRecordsActivity;
import com.duowei.kitchen_china.activity.SellOutActivity;
import com.duowei.kitchen_china.activity.SettingsActivity;
import com.duowei.kitchen_china.bean.Cfpb;
import com.duowei.kitchen_china.bean.Cfpb_item;
import com.duowei.kitchen_china.event.OutTimeFood;
import com.duowei.kitchen_china.event.Print;
import com.duowei.kitchen_china.event.SearchFood;
import com.duowei.kitchen_china.httputils.Net;
import com.duowei.kitchen_china.httputils.Post;
import com.duowei.kitchen_china.sound.KeySound;
import com.duowei.kitchen_china.uitls.ColorAnim;

import org.greenrobot.eventbus.EventBus;

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
    @BindView(R.id.img_print)
    ImageView mImgPrint;
    Unbinder unbinder;
    private Intent mIntent;

    private float tempNum = 0;
    private KeySound mSound;
    private boolean isOutTime = false;
    private AnimationDrawable mDrawable;

    public TopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_top, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        mSound = KeySound.getContext(getActivity());
        return inflate;
    }

    //待煮菜品
    public void setListCfpb(List<Cfpb> listCfpb) {
        mTvUncook.setText(listCfpb.size() + "种");
        float foodCount = 0;
        for (int i = 0; i < listCfpb.size(); i++) {
            List<Cfpb_item> list = listCfpb.get(i).getListCfpb();
            for (int j = 0; j < list.size(); j++) {
                foodCount += list.get(j).sl1;
            }
        }
        mTvCooked.setText(foodCount + "份");
        //新订单声音、动画
        if (foodCount > tempNum) {
            mSound.playSound('0', 0);
            ColorAnim.getInstacne(getActivity()).startColor(mTvCooked);
        }
        tempNum = foodCount;
    }

    public void startAnim(){
        mImgPrint.setImageResource(R.drawable.printanim);
        mDrawable = (AnimationDrawable) mImgPrint.getDrawable();
        mDrawable.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.img_print,R.id.btn_history, R.id.btn_search, R.id.btn_overtime, R.id.btn_saleout,
            R.id.btn_setting, R.id.btn_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_print:
                EventBus.getDefault().post(new Print());
                break;
            case R.id.btn_history:
                mIntent = new Intent(getActivity(), PastRecordsActivity.class);
                startActivity(mIntent);
                break;
            case R.id.btn_search:
                EventBus.getDefault().post(new SearchFood(getResources().getString(R.string.searchfood)));
                break;
            case R.id.btn_overtime:
                if (isOutTime == false) {
                    EventBus.getDefault().post(new OutTimeFood(getResources().getString(R.string.outtimefood)));
                    mBtnOvertime.setText("全部单品");
//                    mBtnOvertime.setTextColor(getResources().getColor(R.color.white));
                } else {
                    EventBus.getDefault().post(new OutTimeFood(getResources().getString(R.string.allfood)));
                    mBtnOvertime.setText("超时单品");
//                    mBtnOvertime.setTextColor(Color.RED);
                }
                isOutTime = !isOutTime;
                //马上发起服务器查询
                Post.getInstance().postCfpb(Net.sql_cfpb);
                break;
            case R.id.btn_saleout:
                mIntent = new Intent(getActivity(), SellOutActivity.class);
                startActivity(mIntent);
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
