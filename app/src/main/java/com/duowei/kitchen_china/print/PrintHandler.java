package com.duowei.kitchen_china.print;


import android.util.Log;

import com.duowei.kitchen_china.bean.Cfpb2;
import com.duowei.kitchen_china.uitls.DateTimes;

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

    /**
     * 下单打印(打印未下单的数据)
     *
     */
    public void print(List<Cfpb2> list) {
        if (mIPrint == null) {
            return;
        }
        for(int i=0;i<list.size();i++){
            Cfpb2 cfpb2 = list.get(i);
            mIPrint.sendMsg(Command.INIT);
            mIPrint.sendMsg(Command.ALIGN_LEFT);
            mIPrint.sendMsg(Command.BOLD);
            Command.WEIGHT[2] = 0x11;
            mIPrint.sendMsg(Command.WEIGHT);
            mIPrint.sendMsg("桌号："+cfpb2.getCzmc()+"\n");
            Command.WEIGHT[2]=0x01;
            mIPrint.sendMsg(Command.WEIGHT);
            mIPrint.sendMsg("点单员："+cfpb2.getYhmc()+"    时间："+ DateTimes.getTime()+"\n");
            mIPrint.sendMsg("---------------------------------------\n");
            Command.WEIGHT[2] = 0x11;
            mIPrint.sendMsg(Command.WEIGHT);
            mIPrint.sendMsg(cfpb2.getYwcsl()+cfpb2.getDw()+"   "+cfpb2.getXmmc()+"\n");
            mIPrint.sendMsg("\n");
            mIPrint.sendMsg(cfpb2.getPz());

            mIPrint.sendMsg("\n\n\n");
            mIPrint.sendMsg(Command.KNIFE);
        }
    }
}
