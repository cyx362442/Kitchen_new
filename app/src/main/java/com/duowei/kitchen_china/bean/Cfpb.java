package com.duowei.kitchen_china.bean;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-06-23.
 */

public class Cfpb extends DataSupport{

    /**
     * XH : 10105
     * xmbh : 00073
     * xmmc : 彩虹飘香（6寸铁盘）
     * dw : 份
     * sl : 1.00
     * pz :
     * xdsj : 20170623T13:55:28
     * czmc : 101,
     * fzs : 69
     */

    private String XH;
    private String xmbh;
    private String xmmc;
    private String dw;
    private float sl;
    private String pz;
    private String xdsj;
    private String czmc;
    private int fzs;
    private String yhmc;
    private float ywcsl;
    private String py;
    private int cssj;
    private String by8;
    private String wcsj;
    private long time;

    public String getPy() {
        return py;
    }

    public void setPy(String py) {
        this.py = py;
    }

    public int getCssj() {
        return cssj;
    }

    public void setCssj(int cssj) {
        this.cssj = cssj;
    }

    public String getBy8() {
        return by8;
    }

    public void setBy8(String by8) {
        this.by8 = by8;
    }

    public Cfpb(String XH, String xmbh, String xmmc, String dw, float sl, String pz, String xdsj,
                String czmc, int fzs, String yhmc, float ywcsl, String wcsj,long time) {
            this.XH = XH;
        this.xmbh = xmbh;
        this.xmmc = xmmc;
        this.dw = dw;
        this.sl = sl;
        this.pz = pz;
        this.xdsj = xdsj;
        this.czmc = czmc;
        this.fzs = fzs;
        this.yhmc = yhmc;
        this.ywcsl = ywcsl;
        this.wcsj = wcsj;
        this.time=time;
    }

    public String getWcsj() {
        return wcsj;
    }

    public void setWcsj(String wcsj) {
        this.wcsj = wcsj;
    }

    public String getYhmc() {
        return yhmc;
    }

    public void setYhmc(String yhmc) {
        this.yhmc = yhmc;
    }

    public float getYwcsl() {
        return ywcsl;
    }

    public void setYwcsl(float ywcsl) {
        this.ywcsl = ywcsl;
    }

    private String sfxz="0";
    private List<Cfpb_item> listCfpb;
    public String getSfxz() {
        return sfxz;
    }

    public void setSfxz(String sfxz) {
        this.sfxz = sfxz;
    }


    public List<Cfpb_item> getListCfpb() {
        if(listCfpb==null){
            listCfpb=new ArrayList<>();
        }
        return listCfpb;
    }

    public void setListCfpb(List<Cfpb_item> listCfpb) {
        this.listCfpb = listCfpb;
    }

    public String getXH() {
        return XH;
    }

    public void setXH(String XH) {
        this.XH = XH;
    }

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

    public String getDw() {
        return dw;
    }

    public void setDw(String dw) {
        this.dw = dw;
    }

    public float getSl() {
        return sl;
    }

    public void setSl(float sl) {
        this.sl = sl;
    }

    public String getPz() {
        return pz;
    }

    public void setPz(String pz) {
        this.pz = pz;
    }

    public String getXdsj() {
        return xdsj;
    }

    public void setXdsj(String xdsj) {
        this.xdsj = xdsj;
    }

    public String getCzmc() {
        return czmc;
    }

    public void setCzmc(String czmc) {
        this.czmc = czmc;
    }

    public int getFzs() {
        return fzs;
    }

    public void setFzs(int fzs) {
        this.fzs = fzs;
    }
}
