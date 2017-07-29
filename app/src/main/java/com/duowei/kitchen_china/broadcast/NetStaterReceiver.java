package com.duowei.kitchen_china.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.application.MyApplication;
import com.duowei.kitchen_china.event.UsbState;
import com.duowei.kitchen_china.httputils.Post;
import com.duowei.kitchen_china.print.PrintHandler;
import com.duowei.kitchen_china.sound.KeySound;
import com.duowei.kitchen_china.uitls.PreferenceUtils;

import org.greenrobot.eventbus.EventBus;

public class NetStaterReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String action = intent.getAction();
        // 监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            //获取联网状态的NetworkInfo对象
            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (info != null) {
                //如果当前的网络连接成功并且网络连接可用
                if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
                    if (info.getType() == ConnectivityManager.TYPE_WIFI
                            || info.getType() == ConnectivityManager.TYPE_MOBILE) {
                        //获取登录时的服务器时间、删除历史数据
                        Post.getInstance().getServerTime();
                        //初始化打印机
                        String printStytle = PreferenceUtils.getInstance(context).getPrintStytle("printStytle", "");
                        if(printStytle.equals(context.getResources().getString(R.string.print_net))){
                            String printerIP = PreferenceUtils.getInstance(context).getPrinterIp("printerIP", "");
                            PrintHandler.getInstance().setIPrint(null);
                            PrintHandler.getInstance().initPrint(context,printerIP);
                        }
                    }
                } else {
                    EventBus.getDefault().post(new UsbState(context.getResources().getString(R.string.net_disconnect)));
                }
            }
        }
    }
}
