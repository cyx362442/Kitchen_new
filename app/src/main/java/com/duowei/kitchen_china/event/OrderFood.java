package com.duowei.kitchen_china.event;

import com.duowei.kitchen_china.bean.Cfpb;

import java.util.List;

/**
 * Created by Administrator on 2017-06-22.
 */

public class OrderFood {
    public List<Cfpb> listCfpb;

    public OrderFood(List<Cfpb> listCfpb) {
        this.listCfpb = listCfpb;
    }
}
