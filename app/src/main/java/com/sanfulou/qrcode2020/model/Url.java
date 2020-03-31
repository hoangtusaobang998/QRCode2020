package com.sanfulou.qrcode2020.model;

import java.io.Serializable;

public class Url implements Serializable {
    private String getUrl;
    private String getTitle;

    public String getGetUrl() {
        return getUrl;
    }

    public void setGetUrl(String getUrl) {
        this.getUrl = getUrl;
    }

    public String getGetTitle() {
        return getTitle;
    }

    public void setGetTitle(String getTitle) {
        this.getTitle = getTitle;
    }

    public Url() {
    }

    public Url(String getUrl, String getTitle) {
        this.getUrl = getUrl;
        this.getTitle = getTitle;
    }
}
