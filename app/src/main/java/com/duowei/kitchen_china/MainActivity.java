package com.duowei.kitchen_china;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.duowei.kitchen_china.event.OrderFood;
import com.duowei.kitchen_china.event.StartProgress;
import com.duowei.kitchen_china.event.UpdateCfpb;
import com.duowei.kitchen_china.fragment.MainFragment;
import com.duowei.kitchen_china.server.PollingService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity {

    private Intent mIntent;
    private MainFragment mFragment;
    private ProgressBar mPb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPb = (ProgressBar) findViewById(R.id.pb);
        mPb.setVisibility(View.VISIBLE);
        mIntent = new Intent(this, PollingService.class);
        startService(mIntent);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        mFragment = new MainFragment();
        ft.replace(R.id.frame, mFragment).commit();
    }

    @Subscribe
    public void getCfpb(OrderFood event){
        mFragment.setRecycleView(event.listCfpb);
        mPb.setVisibility(View.GONE);
    }

    @Subscribe
    public void updateCfpb(UpdateCfpb event){
        mFragment.updateSuccess();
    }

    @Subscribe
    public void startProgress(StartProgress event){
        mPb.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        stopService(mIntent);
    }
}
