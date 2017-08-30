package com.duowei.kitchen_china.event;

import com.duowei.kitchen_china.bean.Cfpb;

import java.util.List;

/**
 * Created by Administrator on 2017-08-30.
 */

public class Completes {
    private List<Cfpb> listCfpbComplete;

    public Completes(List<Cfpb> listCfpbComplete) {
        this.listCfpbComplete = listCfpbComplete;
    }

    public List<Cfpb> getListCfpbComplete() {
        return listCfpbComplete;
    }
}
