package com.duowei.kitchen_china.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.bean.Cfpb;
import com.duowei.kitchen_china.event.InputMsg;
import com.duowei.kitchen_china.event.OrderFood;
import com.duowei.kitchen_china.event.OutTimeFood;
import com.duowei.kitchen_china.event.PrintAmin;
import com.duowei.kitchen_china.event.PrintConnect;
import com.duowei.kitchen_china.event.SearchFood;
import com.duowei.kitchen_china.event.StartProgress;
import com.duowei.kitchen_china.event.Update;
import com.duowei.kitchen_china.event.UpdateCfpb;
import com.duowei.kitchen_china.event.UsbState;
import com.duowei.kitchen_china.fragment.MainFragment;
import com.duowei.kitchen_china.fragment.TopFragment;
import com.duowei.kitchen_china.fragment.TopFragment2;
import com.duowei.kitchen_china.fragment.UpdateFragment;
import com.duowei.kitchen_china.httputils.Net;
import com.duowei.kitchen_china.httputils.Post;
import com.duowei.kitchen_china.print.PrintHandler;
import com.duowei.kitchen_china.print.UsbPrint;
import com.duowei.kitchen_china.server.PollingService;
import com.duowei.kitchen_china.sound.KeySound;
import com.duowei.kitchen_china.uitls.DateTimes;
import com.duowei.kitchen_china.uitls.PreferenceUtils;
import com.gprinter.aidl.GpService;
import com.gprinter.service.GpPrintService;

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
    private KeySound mSound;
    private String mPrintStytle;
    private PreferenceUtils mPreferenceUtils;
    private PrintHandler mPrintHandler;
    private String mPrinterIP;

    public static final String CONNECT_STATUS = "connect.status";
    public  static GpService mGpService = null;
    private PrinterServiceConnection conn = null;

    public static final int REQUESTCODE=200;

    class PrinterServiceConnection implements ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mGpService = null;
        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mGpService = GpService.Stub.asInterface(service);
            mFragment.setGpService(mGpService);
            UsbPrint.getInstance().intiPrint(MainActivity.this,mGpService);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initFragment();
        mSound = KeySound.getContext(this);//初始化声音
        mPreferenceUtils = PreferenceUtils.getInstance(this);
        mPrintHandler=PrintHandler.getInstance();

        //记录登录时的本地时间
        long time =new Date(System.currentTimeMillis()).getTime();
        DateTimes.loginTime=time;
        //初始化打印机
        initPrint();
        //检测版本
        Post.getInstance().checkUpdate(this,true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUESTCODE&&resultCode==SettingsActivity.RESULTCODE){
            initPrint();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        getSql();
        //获取登录时的服务器时间、删除历史数据
        Post.getInstance().getServerTime();
        //开启轮询服务
        startServer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**去除底部导航栏*/
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    private void initPrint() {
        mPrintStytle = mPreferenceUtils.getPrintStytle("printStytle", getResources().getString(R.string.print_usb));
        ConnectivityManager mConnectivityManager = (ConnectivityManager)this
                .getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null&&mNetworkInfo.isAvailable()&&mPrintStytle.equals(getResources().getString(R.string.print_net))) {//网络打印机
            mPrinterIP = mPreferenceUtils.getPrinterIp("printerIP","");
            mPrintHandler.setIPrint(null);
            mPrintHandler.initPrint(this, mPrinterIP);
        }else if(mPrintStytle.equals(getResources().getString(R.string.print_usb))){//USB打印机
            connectionUsbPrint();
        }
    }

    /*开启网络轮询服务*/
    private void startServer() {
        mIntent = new Intent(this, PollingService.class);
        startService(mIntent);
    }

    private void getSql() {
        String serviceIP = mPreferenceUtils.getServiceIp("serviceIP", "");
        Net.url = "http://" + serviceIP + ":2233/server/ServerSvlt?";
        String ketchen = mPreferenceUtils.getKetchen("et_kitchenName", "");
        Net.sql_cfpb="select A.XH,A.xmbh,LTrim(A.xmmc)as xmmc,A.dw,(isnull(A.sl,0)-isnull(A.tdsl,0)-isnull(A.YWCSL,0))sl,\n" +
                "A.pz,CONVERT(varchar(100), a.xdsj, 120)as xdsj,A.BY1 as czmc,datediff(minute,A.xdsj,getdate())fzs,A.yhmc,A.ywcsl,j.py,isnull(j.by13,9999999)cssj,A.by9 from cfpb A LEFT JOIN JYXMSZ J ON A.XMBH=J.XMBH\n" +
                "where A.XDSJ BETWEEN DATEADD(mi,-180,GETDATE()) AND GETDATE() and (isnull(A.sl,0)-isnull(A.tdsl,0))>0 and a.pos='"+ketchen+"'\n" +
                "order by A.xdsj,A.xmmc|";
    }

    /*绑定USB打印机*/
    private void connectionUsbPrint() {
        conn = new PrinterServiceConnection();
        Intent intent = new Intent(this, GpPrintService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE); // bindService
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
        if(mStytle.equals(getResources().getString(R.string.searchfood))){//切换至查询菜品
            mTopFragment2 = new TopFragment2();
            getFragmentManager().beginTransaction()
                    .replace(R.id.frame01, mTopFragment2).commit();
            searchMsg="";
        }else if(mStytle.equals(getResources().getString(R.string.allfood))){//返回全部菜品
            getFragmentManager().beginTransaction()
                    .replace(R.id.frame01, mTopFragment).commit();
            mLoad.setVisibility(View.VISIBLE);
        }
    }

    /*打印机动画*/
    @Subscribe
    public void printAnim(PrintAmin event){
        mTopFragment.startAnim();
    }

    /*判断USB打印机连接状态*/
    @Subscribe
    public void usbPrintState(UsbState event){
        if(event.state.equals(getResources().getString(R.string.usb_connect))){
            UsbPrint.getInstance().intiPrint(this,mGpService);
        }else if(event.state.equals(getResources().getString(R.string.usb_disconnect))){
            mSound.playSound('1',0);
            Toast.makeText(this,"USB打印机己断开",Toast.LENGTH_SHORT).show();
        }else if(event.state.equals(getResources().getString(R.string.net_disconnect))){
            mSound.playSound('2',0);
            Toast.makeText(this,"网络己断开，请检查",Toast.LENGTH_LONG).show();
        }else if(event.state.equals(getResources().getString(R.string.reconnect))){
            mSound.playSound('3',0);
            connectionUsbPrint();
        }
    }

    /* 重新连接打印机*/
    @Subscribe
    public void reConnectPrint(PrintConnect event){
        initPrint();
    }

    @Subscribe
    public void appUpdate(Update event){
        UpdateFragment updateFragment = UpdateFragment.newInstance(event.url, event.name);
        updateFragment.show(getSupportFragmentManager(),getString(R.string.update));
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        stopService(mIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(conn!=null){
            unbindService(conn);
        }
        PrintHandler.getInstance().closePrint();
    }
}
