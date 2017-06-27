package com.duowei.kitchen_china.uitls;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017-06-26.
 */

public class PreferenceUtils {
    private static Context context;
    private final SharedPreferences.Editor mEdit;
    private final SharedPreferences mPreferences;

    private PreferenceUtils (Context context){
       this.context=context;
        mPreferences = context.getSharedPreferences("Users", Context.MODE_PRIVATE);
        mEdit = mPreferences.edit();
   }
    private static PreferenceUtils pfu=null;
    public static PreferenceUtils  getInstance(Context context){
        if(pfu==null){
            pfu=new PreferenceUtils(context);
        }
        return pfu;
    }
    public void setServiceIp(String key,String value){
        mEdit.putString(key,value);
        mEdit.commit();
    }
    public String getServiceIp(String key,String value){
        return mPreferences.getString(key,value);
    }

    public void setPrinterIp(String key,String value){
        mEdit.putString(key,value);
        mEdit.commit();
    }
    public String getPrinterIp(String key,String value){
        return mPreferences.getString(key,value);
    }

    public void setAutoStart(String key,boolean b){
        mEdit.putBoolean(key,b);
        mEdit.commit();
    }
    public boolean getAutoStart(String key,boolean b){
        return mPreferences.getBoolean(key,b);
    }
}
