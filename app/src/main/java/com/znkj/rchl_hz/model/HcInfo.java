package com.znkj.rchl_hz.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by lenovo-pc on 2018-01-29.
 */
public class HcInfo implements Serializable {

    private String sjlx;//
    // 数据类型，hc，hjd，gjdq，sjzl
    private String hcbb;//核查版本
    private String hclx;//核查类型，jq，mh，cl，zh     精确、模糊（人员）车辆综合
    private String hcr_xm;
    private String hcr_sfzh;
    private String hcr_sjh;
    private String hcdz;
    private String hcjd;
    private String hcwd;

    private String bhcr_jnjw;
    private String bhcr_xm;
    private String bhcr_zjlx;
    private String bhcr_sfzh;
    private String bhcr_sjh;
    private String bhcr_hjszd;
    private String bhcr_mz;
    private String bhcr_csrqks;
    private String bhcr_csrqjs;
    private String bhcr_zp;
    private String bhcr_zp2;

    private String cl_clhm;
    private String cl_cllx;
    private String cl_scrs;
    private String cl_scwps;
    private JSONObject cl_wjwp;
    private String ywwjwp;//有无违禁物品 1有0无

    private String zh_ryxx;//综合核查人员信息，格式：身份证号：手机号：是否司机，身份证号：手机号：是否司机

    public String getSjlx() {
        return sjlx;
    }

    public void setSjlx(String sjlx) {
        this.sjlx = sjlx;
    }

    public String getHcbb() {
        return hcbb;
    }

    public void setHcbb(String hcbb) {
        this.hcbb = hcbb;
    }

    public String getHclx() {
        return hclx;
    }

    public void setHclx(String hclx) {
        this.hclx = hclx;
    }

    public String getHcr_xm() {
        return hcr_xm;
    }

    public void setHcr_xm(String hcr_xm) {
        this.hcr_xm = hcr_xm;
    }

    public String getHcr_sfzh() {
        return hcr_sfzh;
    }

    public void setHcr_sfzh(String hcr_sfzh) {
        this.hcr_sfzh = hcr_sfzh;
    }

    public String getHcr_sjh() {
        return hcr_sjh;
    }

    public void setHcr_sjh(String hcr_sjh) {
        this.hcr_sjh = hcr_sjh;
    }

    public String getHcdz() {
        return hcdz;
    }

    public void setHcdz(String hcdz) {
        this.hcdz = hcdz;
    }

    public String getBhcr_jnjw() {
        return bhcr_jnjw;
    }

    public void setBhcr_jnjw(String bhcr_jnjw) {
        this.bhcr_jnjw = bhcr_jnjw;
    }

    public String getBhcr_xm() {
        return bhcr_xm;
    }

    public void setBhcr_xm(String bhcr_xm) {
        this.bhcr_xm = bhcr_xm;
    }

    public String getBhcr_sfzh() {
        return bhcr_sfzh;
    }

    public void setBhcr_sfzh(String bhcr_sfzh) {
        this.bhcr_sfzh = bhcr_sfzh;
    }

    public String getBhcr_sjh() {
        return bhcr_sjh;
    }

    public void setBhcr_sjh(String bhcr_sjh) {
        this.bhcr_sjh = bhcr_sjh;
    }

    public String getBhcr_hjszd() {
        return bhcr_hjszd;
    }

    public void setBhcr_hjszd(String bhcr_hjszd) {
        this.bhcr_hjszd = bhcr_hjszd;
    }

    public String getBhcr_mz() {
        return bhcr_mz;
    }

    public void setBhcr_mz(String bhcr_mz) {
        this.bhcr_mz = bhcr_mz;
    }

    public String getBhcr_csrqks() {
        return bhcr_csrqks;
    }

    public void setBhcr_csrqks(String bhcr_csrqks) {
        this.bhcr_csrqks = bhcr_csrqks;
    }

    public String getBhcr_csrqjs() {
        return bhcr_csrqjs;
    }

    public void setBhcr_csrqjs(String bhcr_csrqjs) {
        this.bhcr_csrqjs = bhcr_csrqjs;
    }

    public String getCl_clhm() {
        return cl_clhm;
    }

    public void setCl_clhm(String cl_clhm) {
        this.cl_clhm = cl_clhm;
    }

    public String getCl_cllx() {
        return cl_cllx;
    }

    public void setCl_cllx(String cl_cllx) {
        this.cl_cllx = cl_cllx;
    }

    public String getCl_scrs() {
        return cl_scrs;
    }

    public void setCl_scrs(String cl_scrs) {
        this.cl_scrs = cl_scrs;
    }

    public String getCl_scwps() {
        return cl_scwps;
    }

    public void setCl_scwps(String cl_scwps) {
        this.cl_scwps = cl_scwps;
    }

    public JSONObject getCl_wjwp() {
        return cl_wjwp;
    }

    public void setCl_wjwp(JSONObject cl_wjwp) {
        this.cl_wjwp = cl_wjwp;
    }

    public String getBhcr_zp() {
        return bhcr_zp;
    }

    public void setBhcr_zp(String bhcr_zp) {
        this.bhcr_zp = bhcr_zp;
    }

    public String getBhcr_zp2() {
        return bhcr_zp2;
    }

    public void setBhcr_zp2(String bhcr_zp2) {
        this.bhcr_zp2 = bhcr_zp2;
    }

    public String getBhcr_zjlx() {
        return bhcr_zjlx;
    }

    public void setBhcr_zjlx(String bhcr_zjlx) {
        this.bhcr_zjlx = bhcr_zjlx;
    }

    public String getHcjd() {
        return hcjd;
    }

    public void setHcjd(String hcjd) {
        this.hcjd = hcjd;
    }

    public String getHcwd() {
        return hcwd;
    }

    public void setHcwd(String hcwd) {
        this.hcwd = hcwd;
    }

    public String getZh_ryxx() {
        return zh_ryxx;
    }

    public void setZh_ryxx(String zh_ryxx) {
        this.zh_ryxx = zh_ryxx;
    }

    public String getYwwjwp() {
        return ywwjwp;
    }

    public void setYwwjwp(String ywwjwp) {
        this.ywwjwp = ywwjwp;
    }
}
