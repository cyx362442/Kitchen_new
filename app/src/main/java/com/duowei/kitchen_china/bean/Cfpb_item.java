package com.duowei.kitchen_china.bean;

/**
 * Created by Administrator on 2017-06-22.
 */

public class Cfpb_item {
    String xmbh1;
    String czmc1;
    float sl1;
    String xdsj;

    public Cfpb_item(String xmbh1, String czmc1, float sl1, String xdsj) {
        this.xmbh1 = xmbh1;
        this.czmc1 = czmc1;
        this.sl1 = sl1;
        this.xdsj = xdsj;
    }

    @Override
    public String toString() {
        return czmc1+xdsj;
    }
}
