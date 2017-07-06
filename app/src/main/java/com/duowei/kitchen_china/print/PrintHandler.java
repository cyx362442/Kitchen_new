package com.duowei.kitchen_china.print;

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

            mIPrint.sendMsg("\n\n\n");
            mIPrint.sendMsg(Command.KNIFE);
        }
    }
}
