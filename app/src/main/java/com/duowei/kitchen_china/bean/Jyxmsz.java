package com.duowei.kitchen_china.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017-07-01.
 */

public class Jyxmsz extends DataSupport {

    /**
     * xmbh : 00001
     * xmmc : 自助沙拉
     * py : ZZSL
     * gq : 0
     */

    private String xmbh;
    private String xmmc;
    private String py;
    private String gq;

    public String getXmbh() {
        return xmbh;
    }

    public void setXmbh(String xmbh) {
        this.xmbh = xmbh;
    }

    public String getXmmc() {
        return xmmc;
    }

    public void setXmmc(String xmmc) {
        this.xmmc = xmmc;
    }

    public String getPy() {
        return py;
    }

    public void setPy(String py) {
        this.py = py;
    }

    public String getGq() {
        return gq;
    }

    public void setGq(String gq) {
        this.gq = gq;
    }
}
