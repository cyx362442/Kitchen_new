package com.duowei.kitchen_china;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.duowei.kitchen_china.event.OrderFood;
import com.duowei.kitchen_china.fragment.MainFragment;
import com.duowei.kitchen_china.server.PollingService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity {

    private Intent mIntent;
    private MainFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIntent = new Intent(this, PollingService.class);
        startService(mIntent);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        mFragment = new MainFragment();
        ft.replace(R.id.frame, mFragment).commit();
    }

    @Subscribe
    public void getCfpb(OrderFood event){
        mFragment.setRecycleView(event.listCfpb);
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
