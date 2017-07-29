package com.duowei.kitchen_china.print;

import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.activity.MainActivity;
import com.gprinter.aidl.GpService;
import com.gprinter.command.GpCom;
import com.gprinter.io.PortParameters;
import com.gprinter.save.PortParamDataBase;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Administrator on 2017-07-27.
 */

public class UsbPrint {
    private UsbPrint(){}
    private static UsbPrint print=null;
    public static UsbPrint getInstance(){
        if(print==null){
            print=new UsbPrint();
        }
        return print;
    }

    public void intiPrint(Context context, GpService gpService){
        Toast.makeText(context,"USB打印机连接中……",Toast.LENGTH_SHORT).show();
        initPortParam(context);
        getUsbDeviceList(context);
        connectOrDisConnectToDevice(context,gpService);
    }

    private int mPrinterId = 0;
//    private PortParameters mPortParam[] = new PortParameters[GpPrintService.MAX_PRINTER_CNT];
    private PortParameters mPortParam[] = new PortParameters[1];
    private void initPortParam(Context context) {
        Intent intent = new Intent();
        boolean[] state = intent.getBooleanArrayExtra(MainActivity.CONNECT_STATUS);
        for (int i = 0; i < 1; i++) {
            PortParamDataBase database = new PortParamDataBase(context);
            mPortParam[i] = new PortParameters();
            mPortParam[i] = database.queryPortParamDataBase("" + i);
            if(state!=null){
                mPortParam[i].setPortOpenState(state[i]);
            }
        }
    }

    public void getUsbDeviceList(Context context) {
        UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        // Get the list of attached devices
        HashMap<String, UsbDevice> devices = manager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = devices.values().iterator();
        int count = devices.size();
        if(count > 0)
        {
            while (deviceIterator.hasNext()) {
                UsbDevice device = deviceIterator.next();
                String devicename = device.getDeviceName();
                if(checkUsbDevicePidVid(device)){
//                    mUsbDeviceArrayAdapter.add(devicename);
//                    mTvaddress.setText("USB打印机地址："+devicename);
                    mPortParam[mPrinterId].setUsbDeviceName(devicename);
                    mPortParam[mPrinterId].setPortType(PortParameters.USB);
                }
            }
        }
        else
        {
            String noDevices = context.getResources().getText(R.string.none_usb_device)
                    .toString();
//            mUsbDeviceArrayAdapter.add(noDevices);
        }
    }
    boolean checkUsbDevicePidVid(UsbDevice dev) {
        int pid = dev.getProductId();
        int vid = dev.getVendorId();
        boolean rel = false;
        if ((vid == 34918 && pid == 256) || (vid == 1137 && pid == 85)
                || (vid == 6790 && pid == 30084)
                || (vid == 26728 && pid == 256) || (vid == 26728 && pid == 512)
                || (vid == 26728 && pid == 256) || (vid == 26728 && pid == 768)
                || (vid == 26728 && pid == 1024)|| (vid == 26728 && pid == 1280)
                || (vid == 26728 && pid == 1536)) {
            rel = true;
        }
        return rel;
    }
    private void connectOrDisConnectToDevice(Context context, GpService mGpService) {
        mPrinterId = 0;
        int rel = 0;
        if (mPortParam[0].getPortOpenState() == false) {
            if (CheckPortParamters(mPortParam[0])) {
                try {
                    mGpService.closePort(mPrinterId);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                switch (mPortParam[0].getPortType()) {
                    case PortParameters.USB:
                        try {
                            rel = mGpService.openPort(0, mPortParam[0].getPortType(),
                                    mPortParam[0].getUsbDeviceName(), 0);
                        } catch (RemoteException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        break;
                }
                GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
                if (r != GpCom.ERROR_CODE.SUCCESS) {
                    Toast.makeText(context,"USB打印机连接中失败!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context,"USB打印机连接成功!",Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context,"参数无效", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("断开连接", "DisconnectToDevice ");
        }
    }
    Boolean CheckPortParamters(PortParameters param) {
        boolean rel = false;
        if (!param.getUsbDeviceName().equals("")) {
            rel = true;
        }
        return rel;
    }
}
