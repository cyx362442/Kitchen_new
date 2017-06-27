package com.duowei.kitchen_china;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.duowei.kitchen_china.event.OrderFood;
import com.duowei.kitchen_china.event.StartProgress;
import com.duowei.kitchen_china.event.UpdateCfpb;
import com.duowei.kitchen_china.fragment.MainFragment;
import com.duowei.kitchen_china.fragment.TopFragment;
import com.duowei.kitchen_china.server.PollingService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity {

    private Intent mIntent;
    private MainFragment mFragment;
    private ProgressBar mPb;
    private TopFragment mTopFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPb = (ProgressBar) findViewById(R.id.pb);
        mPb.setVisibility(View.VISIBLE);

        mIntent = new Intent(this, PollingService.class);
        startService(mIntent);

        mFragment = new MainFragment();
        mTopFragment = new TopFragment();
       getFragmentManager().beginTransaction()
                .replace(R.id.frame01, mTopFragment).commit();
        getFragmentManager().beginTransaction()
                .replace(R.id.frame02, mFragment).commit();
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
    protected void onResume() {
        super.onResume();
        /**去除底部导航栏*/
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        stopService(mIntent);
    }
}
