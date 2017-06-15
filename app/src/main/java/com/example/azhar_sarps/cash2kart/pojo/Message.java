
package com.example.azhar_sarps.cash2kart.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message {

    @SerializedName("0")
    @Expose
    private String _0;
    @SerializedName("st_id")
    @Expose
    private String stId;
    @SerializedName("1")
    @Expose
    private String _1;
    @SerializedName("st_title")
    @Expose
    private String stTitle;
    @SerializedName("2")
    @Expose
    private String _2;
    @SerializedName("st_img")
    @Expose
    private String stImg;
    @SerializedName("3")
    @Expose
    private String _3;
    @SerializedName("st_link")
    @Expose
    private String stLink;

    public String get0() {
        return _0;
    }

    public void set0(String _0) {
        this._0 = _0;
    }

    public String getStId() {
        return stId;
    }

    public void setStId(String stId) {
        this.stId = stId;
    }

    public String get1() {
        return _1;
    }

    public void set1(String _1) {
        this._1 = _1;
    }

    public String getStTitle() {
        return stTitle;
    }

    public void setStTitle(String stTitle) {
        this.stTitle = stTitle;
    }

    public String get2() {
        return _2;
    }

    public void set2(String _2) {
        this._2 = _2;
    }

    public String getStImg() {
        return stImg;
    }

    public void setStImg(String stImg) {
        this.stImg = stImg;
    }

    public String get3() {
        return _3;
    }

    public void set3(String _3) {
        this._3 = _3;
    }

    public String getStLink() {
        return stLink;
    }

    public void setStLink(String stLink) {
        this.stLink = stLink;
    }

}
