package com.duowei.kitchen_china.event;

import com.duowei.kitchen_china.bean.Cfpb2;

import java.util.List;

/**
 * Created by Administrator on 2017-06-22.
 */

public class OrderFood {
    public List<Cfpb2> listCfpb;

    public OrderFood(List<Cfpb2> listCfpb) {
        this.listCfpb = listCfpb;
    }
}
