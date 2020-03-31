package com.sanfulou.qrcode2020.model;

import java.io.Serializable;

public class Phone implements Serializable {
    private String getNumber;
    private String getType;

    public Phone(String getNumber, String getType) {
        this.getNumber = getNumber;
        this.getType = getType;
    }

    public String getGetNumber() {
        return getNumber;
    }

    public void setGetNumber(String getNumber) {
        this.getNumber = getNumber;
    }

    public String getGetType() {
        return getType;
    }

    public void setGetType(String getType) {
        this.getType = getType;
    }

    public Phone() {
    }
}
