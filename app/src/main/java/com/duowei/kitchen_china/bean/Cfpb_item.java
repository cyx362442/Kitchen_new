package com.duowei.kitchen_china.bean;

/**
 * Created by Administrator on 2017-06-22.
 */

public class Cfpb_item {
    public String xmbh1;
    public String czmc1;
    public float sl1;
    public String fzs;
    public String xh;
    public String pz;

    public Cfpb_item(String xmbh1, String czmc1, float sl1, String fzs,String xh,String pz) {
        this.xmbh1 = xmbh1;
        this.czmc1 = czmc1;
        this.sl1 = sl1;
        this.fzs = fzs;
        this.xh=xh;
        this.pz=pz;
    }

    @Override
    public String toString() {
        return czmc1+"["+fzs+"分]";
    }
}
