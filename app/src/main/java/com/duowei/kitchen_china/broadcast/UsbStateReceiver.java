package com.duowei.kitchen_china.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.duowei.kitchen_china.R;
import com.duowei.kitchen_china.event.UsbState;

import org.greenrobot.eventbus.EventBus;

public class UsbStateReceiver extends BroadcastReceiver {
    private final String usbInAction="android.hardware.usb.action.USB_DEVICE_ATTACHED";
    private final String usbOutAction="android.hardware.usb.action.USB_DEVICE_DETACHED";
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String action = intent.getAction();
        if(action.equals(usbInAction)){
            EventBus.getDefault().post(new UsbState(context.getString(R.string.usb_connect)));
        }else if(action.equals(usbOutAction)){
            EventBus.getDefault().post(new UsbState(context.getString(R.string.usb_disconnect)));
        }
    }
}
