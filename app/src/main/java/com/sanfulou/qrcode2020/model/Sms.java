package com.sanfulou.qrcode2020.model;

import java.io.Serializable;

public class Sms implements Serializable {
    private String getMessage;
    private String getPhoneNumber;

    public String getGetMessage() {
        return getMessage;
    }

    public void setGetMessage(String getMessage) {
        this.getMessage = getMessage;
    }

    public String getGetPhoneNumber() {
        return getPhoneNumber;
    }

    public void setGetPhoneNumber(String getPhoneNumber) {
        this.getPhoneNumber = getPhoneNumber;
    }

    public Sms() {
    }

    public Sms(String getMessage, String getPhoneNumber) {
        this.getMessage = getMessage;
        this.getPhoneNumber = getPhoneNumber;
    }
}
