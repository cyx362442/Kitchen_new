package com.duowei.kitchen_china.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.duowei.kitchen_china.activity.MainActivity;
import com.duowei.kitchen_china.httputils.Post;
import com.duowei.kitchen_china.uitls.PreferenceUtils;

public class MyReceiver extends BroadcastReceiver {
    static final String action_boot ="android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        boolean auto = PreferenceUtils.getInstance(context).getAutoStart("auto", true);
        if(intent.getAction().equals(action_boot)&&auto==true){
            Intent bootIntent = new Intent(context, MainActivity.class);
            bootIntent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(bootIntent);
        }
    }
}
