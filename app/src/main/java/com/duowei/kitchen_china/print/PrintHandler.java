package com.duowei.kitchen_china.print;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.duowei.kitchen_china.bean.Cfpb;
import com.duowei.kitchen_china.event.PrintAmin;
import com.duowei.kitchen_china.uitls.DateTimes;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 打印相关
 * <p>
 * Created by zjn on 2017-05-31.
 */

public class PrintHandler {
    private static final String TAG = "PrintHandler";

    private static PrintHandler sPrintHandler;
    private IPrint mIPrint;
    private IPrint[] mPrints;

    public static PrintHandler getInstance() {
        if (sPrintHandler == null) {
            sPrintHandler = new PrintHandler();
        }
        return sPrintHandler;
    }

    public void setIPrint(IPrint IPrint) {
        mIPrint = IPrint;
    }

    public IPrint getIPrint() {
        return mIPrint;
    }

    /**初始化打印机*/
    public void initPrint(Context context,String ip) {
        if(mPrints!=null&&mPrints[0]!=null){
            mPrints[0].close();
        }
        mPrints = new IPrint[]{null};
        if (check(WifiPrint.class)) {
            if (!TextUtils.isEmpty(ip)) {
                mPrints[0] = new WifiPrint(ip);
            } else {
                Toast.makeText(context, "没有设置打印机IP地址!", Toast.LENGTH_SHORT).show();
            }
        }
        if (mPrints[0] != null) {
            PrintHandler.getInstance().setIPrint(mPrints[0]);
        }
    }

    private boolean check(Class<? extends IPrint> cls) {
        if (mIPrint == null) {
            return true;
        }

        if (cls.isInstance(mIPrint)) {
            return false;

        } else if (cls.isInstance(mIPrint)) {
            return false;

        } else if (cls.isInstance(mIPrint)) {
            return false;
        }

        return true;
    }

    public void closePrint(){
        mPrints[0].close();
    }

    /**
     * 下单打印(打印未下单的数据)
     *
     */
    public void print(List<Cfpb> list) {
        if (mIPrint == null) {
            return;
        }

        EventBus.getDefault().post(new PrintAmin());
        for(int i=0;i<list.size();i++){
            Cfpb cfpb = list.get(i);
            mIPrint.sendMsg(Command.INIT);
            mIPrint.sendMsg(Command.ALIGN_LEFT);
            mIPrint.sendMsg(Command.BOLD);
            Command.WEIGHT[2] = 0x11;
            mIPrint.sendMsg(Command.WEIGHT);
            mIPrint.sendMsg("桌号："+ cfpb.getCzmc()+"\n");
            Command.WEIGHT[2]=0x01;
            mIPrint.sendMsg(Command.WEIGHT);
            mIPrint.sendMsg("点单员："+ cfpb.getYhmc()+"    时间："+ DateTimes.getTime()+"\n");
            mIPrint.sendMsg("---------------------------------------\n");
            Command.WEIGHT[2] = 0x11;
            mIPrint.sendMsg(Command.WEIGHT);
            mIPrint.sendMsg(cfpb.getYwcsl()+ cfpb.getDw()+"   "+ cfpb.getXmmc()+"\n");
            mIPrint.sendMsg("\n");
            mIPrint.sendMsg(cfpb.getPz());

            mIPrint.sendMsg("\n\n");
            mIPrint.sendMsg(Command.KNIFE);
        }
    }
    public void printSingle(Cfpb cfpb){
        mIPrint.sendMsg(Command.ALIGN_CENTER);
        mIPrint.sendMsg("复打小票\n");

        mIPrint.sendMsg(Command.INIT);
        mIPrint.sendMsg(Command.ALIGN_LEFT);
        mIPrint.sendMsg(Command.BOLD);
        Command.WEIGHT[2] = 0x11;
        mIPrint.sendMsg(Command.WEIGHT);
        mIPrint.sendMsg("桌号："+ cfpb.getCzmc()+"\n");
        Command.WEIGHT[2]=0x01;
        mIPrint.sendMsg(Command.WEIGHT);
        mIPrint.sendMsg("点单员："+ cfpb.getYhmc()+"    时间："+ DateTimes.getTime()+"\n");
        mIPrint.sendMsg("---------------------------------------\n");
        Command.WEIGHT[2] = 0x11;
        mIPrint.sendMsg(Command.WEIGHT);
        mIPrint.sendMsg(cfpb.getYwcsl()+ cfpb.getDw()+"   "+ cfpb.getXmmc()+"\n");
        mIPrint.sendMsg("\n");
        mIPrint.sendMsg(cfpb.getPz());

        mIPrint.sendMsg("\n\n");
        mIPrint.sendMsg(Command.KNIFE);
    }
}
