package com.duowei.kitchen_china.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.duowei.kitchen_china.print.PrintHandler;

public class ShutDownReceiver extends BroadcastReceiver {
    private final String action="android.intent.action.ACTION_SHUTDOWN";
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String msg = intent.getAction();
        if(msg.equals(action)){
//            PrintHandler.getInstance().closePrint();
        }
    }
}
