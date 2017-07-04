package com.duowei.kitchen_china.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.bean.Cfpb;
import com.duowei.kitchen_china.broadcast.MyReceiver;
import com.duowei.kitchen_china.event.OrderFood;
import com.duowei.kitchen_china.event.SearchFood;
import com.duowei.kitchen_china.event.StartProgress;
import com.duowei.kitchen_china.event.UpdateCfpb;
import com.duowei.kitchen_china.fragment.MainFragment;
import com.duowei.kitchen_china.fragment.TopFragment;
import com.duowei.kitchen_china.fragment.TopFragment2;
import com.duowei.kitchen_china.httputils.Net;
import com.duowei.kitchen_china.httputils.Post;
import com.duowei.kitchen_china.print.IPrint;
import com.duowei.kitchen_china.print.PrintHandler;
import com.duowei.kitchen_china.print.WifiPrint;
import com.duowei.kitchen_china.server.PollingService;
import com.duowei.kitchen_china.uitls.DateTimes;
import com.duowei.kitchen_china.uitls.PreferenceUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.crud.DataSupport;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Intent mIntent;
    private MainFragment mFragment;
    private TopFragment mTopFragment;
    private View mLoad;
    private boolean isSearch=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();

        initFragment();
//        DataSupport.findLast(Cfpb.class);
        PrintHandler.getInstance().setIPrint(null);
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
        //开启轮询服务
        String serviceIP = PreferenceUtils.getInstance(this).getServiceIp("serviceIP", "");
        Net.url = "http://" + serviceIP + ":2233/server/ServerSvlt?";
        startServer();
        //初始化打印机
        initPrint();

        long time =new Date(System.currentTimeMillis()).getTime();
        DateTimes.loginTime=time;
        Post.getInstance().getServerTime();
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
        mLoad = findViewById(R.id.loading);
        mLoad.setVisibility(View.VISIBLE);
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
        if(isSearch==false){
            mFragment.setRecycleView(event.listCfpb);
            mTopFragment.setListCfpb(event.listCfpb);
        }
        mLoad.setVisibility(View.GONE);
    }

    @Subscribe
    public void updateCfpb(UpdateCfpb event){
        mFragment.updateSuccess();
    }

    @Subscribe
    public void startProgress(StartProgress event){
        mLoad.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void toSearch(SearchFood event){
        isSearch=event.search;
        if(event.search==true){
            TopFragment2 fragment = new TopFragment2();
            getFragmentManager().beginTransaction()
                    .replace(R.id.frame01, fragment).commit();
        }else{
            getFragmentManager().beginTransaction()
                    .replace(R.id.frame01, mTopFragment).commit();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        stopService(mIntent);
    }
}
