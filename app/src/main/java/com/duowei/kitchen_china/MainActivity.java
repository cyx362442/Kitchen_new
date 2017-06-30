package com.duowei.kitchen_china;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.duowei.kitchen_china.bean.Cfpb2;
import com.duowei.kitchen_china.event.OrderFood;
import com.duowei.kitchen_china.event.StartProgress;
import com.duowei.kitchen_china.event.UpdateCfpb;
import com.duowei.kitchen_china.fragment.MainFragment;
import com.duowei.kitchen_china.fragment.TopFragment;
import com.duowei.kitchen_china.print.IPrint;
import com.duowei.kitchen_china.print.PrintHandler;
import com.duowei.kitchen_china.print.WifiPrint;
import com.duowei.kitchen_china.server.PollingService;
import com.duowei.kitchen_china.uitls.PreferenceUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.crud.DataSupport;

public class MainActivity extends AppCompatActivity {

    private Intent mIntent;
    private MainFragment mFragment;
    private ProgressBar mPb;
    private TopFragment mTopFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();

        startServer();

        initFragment();
        initPrint();
        DataSupport.findLast(Cfpb2.class);
    }


    private void startServer() {
        mIntent = new Intent(this, PollingService.class);
        startService(mIntent);
    }

    private void initFragment() {
        mFragment = new MainFragment();
        mTopFragment = new TopFragment();
        getFragmentManager().beginTransaction()
                 .replace(R.id.frame01, mTopFragment).commit();
        getFragmentManager().beginTransaction()
                .replace(R.id.frame02, mFragment).commit();
    }

    private void initUI() {
        mPb = (ProgressBar) findViewById(R.id.pb);
        mPb.setVisibility(View.VISIBLE);
    }
    /**初始化打印机*/
    private void initPrint() {
        final IPrint[] iPrint = {null};
        if (check(WifiPrint.class)) {
            String ip = PreferenceUtils.getInstance(this).getPrinterIp("printerIP","");
            if (!TextUtils.isEmpty(ip)) {
                iPrint[0] = new WifiPrint(ip);
            } else {
                Toast.makeText(this, "没有设置打印机IP地址!", Toast.LENGTH_SHORT).show();
            }
        }
        if (iPrint[0] != null) {
            PrintHandler.getInstance().setIPrint(iPrint[0]);
        }
    }

    private boolean check(Class<? extends IPrint> cls) {
        IPrint print = PrintHandler.getInstance().getIPrint();

        if (print == null) {
            return true;
        }

        if (cls.isInstance(print)) {
            return false;

        } else if (cls.isInstance(print)) {
            return false;

        } else if (cls.isInstance(print)) {
            return false;
        }

        return true;
    }

    @Subscribe
    public void getCfpb(OrderFood event){
        mFragment.setRecycleView(event.listCfpb);
        mTopFragment.setListCfpb(event.listCfpb);
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
