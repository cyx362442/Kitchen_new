package com.duowei.kitchen_china.uitls;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016-12-19.
 */

public class DateTimes {
    public static long loginTime=0;//登录时的本地时间

    public static long serverTime=0;//登录时的服务器时间

    public static long getServerTime() {
        return serverTime;
    }

    public static void setServerTime(long serverTime) {
        DateTimes.serverTime = serverTime;
    }

    public static String getWeek(){
        String z = "";
        Date date = new Date();
        SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
        String week = dateFm.format(date);
        if (week.equals("星期一")) {
            z = "Z1";
        } else if (week.equals("星期二")) {
            z = "Z2";
        } else if (week.equals("星期三")) {
            z = "Z3";
        } else if (week.equals("星期四")) {
            z = "Z4";
        } else if (week.equals("星期五")) {
            z = "Z5";
        } else if (week.equals("星期六")) {
            z = "Z6";
        } else if (week.equals("星期日")) {
            z = "Z7";
        }
        return z;
    }

    public static String getTime(){
        Date curDate = new Date(System.currentTimeMillis());
        long passtime = curDate.getTime() - loginTime;
        Date date = new Date(serverTime+passtime);
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dtime=dateformat.format(date);

        return dtime;
    }
    public static long getTime2(String xdsj){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long mTime = 0;
        try {
            Date date = format.parse(xdsj);
            mTime= date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mTime;
    }
    public static String getSysTime(){
        Date curDate = new Date(System.currentTimeMillis());
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateformat.format(curDate);
    }
}
