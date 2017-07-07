package com.duowei.kitchen_china.print;

import android.app.Activity;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.bean.Cfpb;
import com.duowei.kitchen_china.uitls.DateTimes;
import com.zj.usbsdk.UsbController;

import java.util.List;

/**
 * Created by Administrator on 2017-07-07.
 */

public class UsbPrint {
    private Context context;
    private int[][] u_infor;
    UsbController usbCtrl = null;
    UsbDevice dev = null;
    private byte[] mCmd;

    private UsbPrint(Context context){
        this.context=context;
    }
    private static UsbPrint ub=null;
    public static UsbPrint getInstance(Context context){
        if(ub==null){
            ub=new UsbPrint(context);
        }
        return ub;
    }

    /*初始化打印机*/
    public void intUsbPrint(){
        usbCtrl = new UsbController((Activity) context,mHandler);
        u_infor = new int[6][2];
        u_infor[0][0] = 0x1CBE;
        u_infor[0][1] = 0x0003;
        u_infor[1][0] = 0x1CB0;
        u_infor[1][1] = 0x0003;
        u_infor[2][0] = 0x0483;
        u_infor[2][1] = 0x5740;
        u_infor[3][0] = 0x0493;
        u_infor[3][1] = 0x8760;
        u_infor[4][0] = 0x0416;
        u_infor[4][1] = 0x5011;
        u_infor[5][0] = 0x0416;
        u_infor[5][1] = 0xAABB;
        mCmd = new byte[3];
        mCmd[0] = 0x1b;
        mCmd[1] = 0x21;
    }

    /*连接打印机*/
    public void connectUsbPrint(){
        usbCtrl.close();
        int  i = 0;
        for( i = 0 ; i < 6 ; i++ ){
            dev = usbCtrl.getDev(u_infor[i][0],u_infor[i][1]);
            if(dev != null)
                break;
        }

        if( dev != null ){
            if( !(usbCtrl.isHasPermission(dev))){
                //Log.d("usb调试","请求USB设备权限.");
                usbCtrl.getPermission(dev);
            }else{
                Toast.makeText(context,R.string.msg_getpermission,Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void usbPrint(List<Cfpb>listCfpb){
        byte isHasPaper = usbCtrl.revByte(dev);
        if( isHasPaper == 0x38 ){
            Toast.makeText(context, "打印机没纸了",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if( CheckUsbPermission() == true ){
            for(int i=0;i<listCfpb.size();i++){
                Cfpb cfpb = listCfpb.get(i);

                Command.WEIGHT[2] = 0x11;
                usbCtrl.sendByte(Command.WEIGHT,dev);
                usbCtrl.sendMsg("桌号："+ cfpb.getCzmc(),"GBK", dev);

                Command.WEIGHT[2]=0x00;
                usbCtrl.sendByte(Command.WEIGHT,dev);
                usbCtrl.sendMsg("点单员："+ cfpb.getYhmc()+"    时间："+ DateTimes.getTime(),"GBK", dev);
                usbCtrl.sendMsg("--------------------------------","GBK", dev);

                Command.WEIGHT[2] = 0x11;
                usbCtrl.sendByte(Command.WEIGHT,dev);
                usbCtrl.sendMsg(cfpb.getYwcsl()+ cfpb.getDw()+"  "+ cfpb.getXmmc()+"\n","GBK", dev);
                usbCtrl.sendMsg(cfpb.getPz(),"GBK", dev);
                usbCtrl.sendMsg("\n\n\n","GBK", dev);
//                mIPrint.sendMsg(Command.KNIFE);
            }
        }
    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UsbController.USB_CONNECTED:
                    Toast.makeText(context, R.string.msg_getpermission, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    //检查是否具有访问usb设备的权限
    public boolean CheckUsbPermission(){
        if( dev != null ){
            if( usbCtrl.isHasPermission(dev)){
                return true;
            }
        }

        Toast.makeText(context, R.string.msg_conn_state, Toast.LENGTH_SHORT).show();
        return false;
    }

    public void closeUsbPrint(){
        usbCtrl.close();
    }
}
