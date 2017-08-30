package com.duowei.kitchen_china.bean;

/**
 * Created by Administrator on 2017-06-22.
 */

public class Cfpb_item {
    public String xmbh1;
    public String czmc1;
    public float sl1;
    public int fzs;
    public String xh;
    public String pz;
    public String xszt;
    public String by10;
    public String wc;

    public String getWc() {
        return wc;
    }

    public void setWc(String wc) {
        this.wc = wc;
    }

    public String getBy10() {
        return by10;
    }

    public void setBy10(String by10) {
        this.by10 = by10;
    }

    public Cfpb_item(String xmbh1, String czmc1, float sl1, int fzs, String xh, String pz, String xszt, String by10) {
        this.xmbh1 = xmbh1;
        this.czmc1 = czmc1;
        this.sl1 = sl1;
        this.fzs = fzs;
        this.xh = xh;
        this.pz = pz;
        this.xszt = xszt;
        this.by10 = by10;
    }

    @Override
    public String toString() {
        return czmc1+"["+fzs+"分]";
    }
}
