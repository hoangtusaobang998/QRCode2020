package com.sanfulou.qrcode2020.model;

import java.io.Serializable;

public class Display implements Serializable {
    private String display;

    public Display() {
    }

    public Display(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
