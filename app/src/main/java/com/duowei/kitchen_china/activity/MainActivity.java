package com.duowei.kitchen_china.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.bean.Cfpb;
import com.duowei.kitchen_china.event.InputMsg;
import com.duowei.kitchen_china.event.OrderFood;
import com.duowei.kitchen_china.event.OutTimeFood;
import com.duowei.kitchen_china.event.Print;
import com.duowei.kitchen_china.event.PrintAmin;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Intent mIntent;
    private MainFragment mFragment;
    private TopFragment mTopFragment;
    private View mLoad;
    private String mStytle;
    private List<Cfpb>mCfpbList;
    private List<Cfpb>tempList=new ArrayList<>();
    private TopFragment2 mTopFragment2;
    private String searchMsg="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        initFragment();
        PrintHandler.getInstance().setIPrint(null);
        //记录登录时的本地时间
        long time =new Date(System.currentTimeMillis()).getTime();
        DateTimes.loginTime=time;
        //获取登录时的服务器时间、删除历史数据
        Post.getInstance().getServerTime();
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*开启网络轮询服务*/
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
        mStytle=getResources().getString(R.string.allfood);
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

    /*菜品查询*/
    private void setSearchFood(String searchMsg) {
        tempList.clear();
        for(int i=0;i<mCfpbList.size();i++){
            if(searchMsg.matches("[0-9]+")){//按编号检索
                if(mCfpbList.get(i).getXmbh().contains(searchMsg)){
                    tempList.add(mCfpbList.get(i));
                }
            }else{//按拼音检索
                if(mCfpbList.get(i).getPy().contains(searchMsg.toUpperCase())){
                    tempList.add(mCfpbList.get(i));
                }
            }
        }
        mFragment.setRecycleView2(tempList);
    }

    @Subscribe
    public void getCfpb(OrderFood event){
        mCfpbList=event.listCfpb;
        if(mStytle.equals(getResources().getString(R.string.allfood))){//正常显示所有菜品
            mTopFragment.setListCfpb(event.listCfpb);
            mFragment.setRecycleView(event.listCfpb);
        }else if(mStytle.equals(getResources().getString(R.string.searchfood))){//查询菜品界面
            setSearchFood(searchMsg);
        }else if(mStytle.equals(getResources().getString(R.string.outtimefood))){//超时菜品
            tempList.clear();
            for(int i=0;i<mCfpbList.size();i++){
                Cfpb cfpb = mCfpbList.get(i);
                if(cfpb.getFzs()>cfpb.getCssj()){
                    tempList.add(cfpb);
                }
            }
            mTopFragment.setListCfpb(tempList);
            mFragment.setRecycleView2(tempList);
        }
        mLoad.setVisibility(View.GONE);
    }
    /*超时单品*/
    @Subscribe
    public void setOutTimeFood(OutTimeFood event){
        mStytle=event.stytle;
        mLoad.setVisibility(View.VISIBLE);
    }

     /*菜品查询*/
    @Subscribe
    public void serachOrderFood(InputMsg event){
        searchMsg=event.msg;
        setSearchFood(event.msg);
    }

    @Subscribe
    public void updateCfpb(UpdateCfpb event){
        mFragment.updateSuccess();
    }

    @Subscribe
    public void startProgress(StartProgress event){
        mLoad.setVisibility(View.VISIBLE);
    }
   /* 顶部窗口切换*/
    @Subscribe
    public void toSearch(SearchFood event){
        mStytle=event.stytle;
        if(mStytle.equals(getResources().getString(R.string.searchfood))){//查询菜品
            mTopFragment2 = new TopFragment2();
            getFragmentManager().beginTransaction()
                    .replace(R.id.frame01, mTopFragment2).commit();
            tempList=new ArrayList<>();
        }else if(mStytle.equals(getResources().getString(R.string.allfood))){//全部菜品
            getFragmentManager().beginTransaction()
                    .replace(R.id.frame01, mTopFragment).commit();
            mLoad.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe
    public void printConnect(Print event){
        PrintHandler.getInstance().setIPrint(null);
        initPrint();
    }

    @Subscribe
    public void printAnim(PrintAmin event){
        mTopFragment.startAnim();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        stopService(mIntent);
    }
}
