package com.duowei.kitchen_china.bean;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-06-22.
 */

public class Cfpb {

    /**
     * XH : 10046
     * MTXH : 46
     * WMDBH : CYY20170622094858575
     * XMBH : 00926
     * XMMC : 玫瑰奶茶
     * DW : 份
     * SL : 5.00
     * PZ :
     * XSZT :
     * YHMC : 管理员
     * POS : cyy
     * XDSJ : 20170622T09:49:07
     * BY1 : 206,
     * BY2 : CYY
     * BY3 : 14
     * BY4 :
     * BY5 : 754411
     * YWCSL : 4.00
     */

    private String XH;
    private String MTXH;
    private String WMDBH;
    private String XMBH;
    private String XMMC;
    private String DW;
    private float SL;
    private String PZ;
    private String XSZT;
    private String YHMC;
    private String POS;
    private String XDSJ;
    private String BY1;
    private String BY2;
    private String BY3;
    private String BY4;
    private String BY5;
    private String YWCSL;

    private float zjsl;

    private List<Cfpb_item>list;

    public List<Cfpb_item> getList() {
        if(list==null){
            list=new ArrayList<>();
        }
        return list;
    }

    public void setList(List<Cfpb_item> list) {
        this.list = list;
    }

    public float getZjsl() {
        return zjsl;
    }

    public void setZjsl(float zjsl) {
        this.zjsl = zjsl;
    }

    public String getXH() {
        return XH;
    }

    public void setXH(String XH) {
        this.XH = XH;
    }

    public String getMTXH() {
        return MTXH;
    }

    public void setMTXH(String MTXH) {
        this.MTXH = MTXH;
    }

    public String getWMDBH() {
        return WMDBH;
    }

    public void setWMDBH(String WMDBH) {
        this.WMDBH = WMDBH;
    }

    public String getXMBH() {
        return XMBH;
    }

    public void setXMBH(String XMBH) {
        this.XMBH = XMBH;
    }

    public String getXMMC() {
        return XMMC;
    }

    public void setXMMC(String XMMC) {
        this.XMMC = XMMC;
    }

    public String getDW() {
        return DW;
    }

    public void setDW(String DW) {
        this.DW = DW;
    }

    public float getSL() {
        return SL;
    }

    public void setSL(float SL) {
        this.SL = SL;
    }

    public String getPZ() {
        return PZ;
    }

    public void setPZ(String PZ) {
        this.PZ = PZ;
    }

    public String getXSZT() {
        return XSZT;
    }

    public void setXSZT(String XSZT) {
        this.XSZT = XSZT;
    }

    public String getYHMC() {
        return YHMC;
    }

    public void setYHMC(String YHMC) {
        this.YHMC = YHMC;
    }

    public String getPOS() {
        return POS;
    }

    public void setPOS(String POS) {
        this.POS = POS;
    }

    public String getXDSJ() {
        return XDSJ;
    }

    public void setXDSJ(String XDSJ) {
        this.XDSJ = XDSJ;
    }

    public String getBY1() {
        return BY1;
    }

    public void setBY1(String BY1) {
        this.BY1 = BY1;
    }

    public String getBY2() {
        return BY2;
    }

    public void setBY2(String BY2) {
        this.BY2 = BY2;
    }

    public String getBY3() {
        return BY3;
    }

    public void setBY3(String BY3) {
        this.BY3 = BY3;
    }

    public String getBY4() {
        return BY4;
    }

    public void setBY4(String BY4) {
        this.BY4 = BY4;
    }

    public String getBY5() {
        return BY5;
    }

    public void setBY5(String BY5) {
        this.BY5 = BY5;
    }

    public String getYWCSL() {
        return YWCSL;
    }

    public void setYWCSL(String YWCSL) {
        this.YWCSL = YWCSL;
    }
}
