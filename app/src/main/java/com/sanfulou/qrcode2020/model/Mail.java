package com.sanfulou.qrcode2020.model;

import java.io.Serializable;

public class Mail implements Serializable {
   private String getSubject;
   private String getBody;
   private String getAddress;
   private String getType;

    public String getGetSubject() {
        return getSubject;
    }

    public void setGetSubject(String getSubject) {
        this.getSubject = getSubject;
    }

    public String getGetBody() {
        return getBody;
    }

    public void setGetBody(String getBody) {
        this.getBody = getBody;
    }

    public String getGetAddress() {
        return getAddress;
    }

    public void setGetAddress(String getAddress) {
        this.getAddress = getAddress;
    }

    public String getGetType() {
        return getType;
    }

    public void setGetType(String getType) {
        this.getType = getType;
    }

    public Mail() {
    }

    public Mail(String getSubject, String getBody, String getAddress, String getType) {
        this.getSubject = getSubject;
        this.getBody = getBody;
        this.getAddress = getAddress;
        this.getType = getType;
    }
}
