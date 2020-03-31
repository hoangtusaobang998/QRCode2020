package com.sanfulou.qrcode2020.model;

import java.io.Serializable;

public class Wifi implements Serializable {
    private String getEncryptionType;
    private String getPassword;
    private String getSsid;

    public String getGetEncryptionType() {
        return getEncryptionType;
    }

    public void setGetEncryptionType(String getEncryptionType) {
        this.getEncryptionType = getEncryptionType;
    }

    public String getGetPassword() {
        return getPassword;
    }

    public void setGetPassword(String getPassword) {
        this.getPassword = getPassword;
    }

    public String getGetSsid() {
        return getSsid;
    }

    public void setGetSsid(String getSsid) {
        this.getSsid = getSsid;
    }

    public Wifi() {
    }

    public Wifi(String getEncryptionType, String getPassword, String getSsid) {
        this.getEncryptionType = getEncryptionType;
        this.getPassword = getPassword;
        this.getSsid = getSsid;
    }
}
