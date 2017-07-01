package com.duowei.kitchen_china.httputils;

/**
 * Created by Administrator on 2017-06-23.
 */

public class Net {
    public static String url;

    public static String sql_cfpb="select A.XH,A.xmbh,LTrim(A.xmmc)as xmmc,A.dw,(isnull(A.sl,0)-isnull(A.tdsl,0)-isnull(A.YWCSL,0))sl,\n" +
            "A.pz,a.xdsj,CONVERT(varchar(100), a.xdsj, 120)as xdsj,A.BY1 as czmc,datediff(minute,A.xdsj,getdate())fzs,A.yhmc,A.ywcsl from cfpb A LEFT JOIN JYXMSZ J ON A.XMBH=J.XMBH\n" +
            "where A.XDSJ BETWEEN DATEADD(mi,-180,GETDATE()) AND GETDATE() and (isnull(A.sl,0)-isnull(A.tdsl,0))>0\n" +
            "order by A.xdsj,A.xmmc|";

    public static String sql_jyxmsz="select xmbh,xmmc,py,isnull(gq,'0')gq from jyxmsz where isnull(sfqx,'0')<>'1'|";
}
