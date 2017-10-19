package com.duowei.kitchen_china.uitls;

/**
 * Created by Administrator on 2017-10-19.
 */

public class NumUtil {
    public static String clearZero(String s) {
        String str = "0";
        if (s.indexOf(".") > 0) {
            str = s.replaceAll("0+?$", "").replaceAll("[.]$", "");//去掉后面无用的零
        }
        return str;
    }
}
