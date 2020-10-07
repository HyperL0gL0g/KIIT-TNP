package com.application.kiit_tnp.utils;

public class dataObj {

    String sdate,edata,banner,noticeID;

    public dataObj(String sdate, String edata, String banner, String noticeID) {
        this.sdate = sdate;
        this.edata = edata;
        this.banner = banner;
        this.noticeID = noticeID;
    }


    public  String getNoticeID(){return noticeID;}
    public String getSdata() {
        return sdate;
    }

    public String getEdata() {
        return edata;
    }


    public String getBanner() {
        return banner;
    }
}
