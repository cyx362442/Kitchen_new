package com.duowei.kitchen_china.print;

import android.content.Context;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Toast;

import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.application.MyApplication;
import com.duowei.kitchen_china.bean.Cfpb;
import com.duowei.kitchen_china.event.PrintAmin;
import com.duowei.kitchen_china.event.UsbState;
import com.duowei.kitchen_china.uitls.DateTimes;
import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
import com.gprinter.command.GpCom;
import com.gprinter.command.GpUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Vector;

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
        if(mPrints!=null){
            mPrints[0].close();
        }
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
            mIPrint.sendMsg("点单员："+ cfpb.getYhmc()+"    时间："+ cfpb.getXdsj()+"\n");
            mIPrint.sendMsg("---------------------------------------\n");
            Command.WEIGHT[2] = 0x11;
            mIPrint.sendMsg(Command.WEIGHT);
            mIPrint.sendMsg(cfpb.getYwcsl()+ cfpb.getDw()+"   "+ cfpb.getXmmc()+"\n");
            mIPrint.sendMsg("\n");
            String pz = cfpb.getPz();
            if(!TextUtils.isEmpty(pz)){
                if(pz.contains("&lt;")&&pz.contains("&gt;")){
                    pz=pz.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
                }
            }
            mIPrint.sendMsg(pz);
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
        mIPrint.sendMsg("点单员："+ cfpb.getYhmc()+"    时间："+ cfpb.getXdsj()+"\n");
        mIPrint.sendMsg("---------------------------------------\n");
        Command.WEIGHT[2] = 0x11;
        mIPrint.sendMsg(Command.WEIGHT);
        mIPrint.sendMsg(cfpb.getYwcsl()+ cfpb.getDw()+"   "+ cfpb.getXmmc()+"\n");
        mIPrint.sendMsg("\n");
        String pz = cfpb.getPz();
        if(!TextUtils.isEmpty(pz)){
            if(pz.contains("&lt;")&&pz.contains("&gt;")){
                pz=pz.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
            }
        }
        mIPrint.sendMsg(pz);
        mIPrint.sendMsg("\n\n");
        mIPrint.sendMsg(Command.KNIFE);
    }

    public void printUsb(GpService mGpService,List<Cfpb> listCfpb){
        EscCommand esc = new EscCommand();
        for(int i=0;i<listCfpb.size();i++){
            Cfpb cfpb = listCfpb.get(i);
            esc.addInitializePrinter();
            esc.addPrintAndFeedLines((byte) 2);
            esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
            esc.addText("桌号："+cfpb.getCzmc()+"\n"); // 桌号
            esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 取消倍高倍宽
            esc.addText("点单员："+cfpb.getYhmc()+"    时间："+cfpb.getXdsj()+"\n");
            esc.addText("-------------------------------\n");
            esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
            esc.addText(cfpb.getYwcsl()+cfpb.getDw()+"  "+cfpb.getXmmc()+"\n\n");
            String pz = cfpb.getPz();
            if(!TextUtils.isEmpty(pz)){
                if(pz.contains("&lt;")&&pz.contains("&gt;")){
                    pz=pz.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
                }
            }
            esc.addText(pz);
            esc.addText("\n\n\n\n\n");
        }
        Vector<Byte> datas = esc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String sss = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rs;
        try {
            rs = mGpService.sendEscCommand(0, sss);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rs];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(MyApplication.getContext(), "打印失败，请到历史菜品中重新打印", Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void printUsb(GpService mGpService,Cfpb cfpb){
        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
        esc.addPrintAndFeedLines((byte) 2);
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
        esc.addText("桌号："+cfpb.getCzmc()+"\n"); // 桌号
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 取消倍高倍宽
        esc.addText("点单员："+cfpb.getYhmc()+"    时间："+cfpb.getXdsj()+"\n");
        esc.addText("-------------------------------\n");
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
        esc.addText(cfpb.getYwcsl()+cfpb.getDw()+"  "+cfpb.getXmmc()+"\n\n");
        if(!TextUtils.isEmpty(cfpb.getPz())){
            esc.addText(cfpb.getPz());
        }
        esc.addText("\n\n\n\n\n");
        Vector<Byte> datas = esc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String sss = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rs;
        try {
            rs = mGpService.sendEscCommand(0, sss);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rs];
        if (r != GpCom.ERROR_CODE.SUCCESS) {
            Toast.makeText(MyApplication.getContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            EventBus.getDefault().post(new UsbState(MyApplication.getContext().getResources().getString(R.string.reconnect)));
            }
        } catch (RemoteException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        }
    }
}
