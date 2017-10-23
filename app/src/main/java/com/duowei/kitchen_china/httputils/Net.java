package com.duowei.kitchen_china.httputils;

/**
 * Created by Administrator on 2017-06-23.
 */

public class Net {
    public static String url;
    public static String sql_cfpb;
    public static String sql_call;

    public static String sql_jyxmsz="select xmbh,xmmc,py,isnull(gq,'0')gq from jyxmsz where isnull(sfqx,'0')<>'1' and isnull(sftc,'0')<>'1'|";
}
