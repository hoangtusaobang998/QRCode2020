package com.sanfulou.qrcode2020.model;

import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;

import java.io.Serializable;
import java.util.List;

public class Contact implements Serializable {
    private List<FirebaseVisionBarcode.Address> getAddresses;
    private List<FirebaseVisionBarcode.Email> getEmails;
    private FirebaseVisionBarcode.PersonName getName;
    private String getOrganization;
    private List<FirebaseVisionBarcode.Phone> getPhones;
    private String getTitle;
    private String[] getUrls;

    public Contact() {
    }


    public List<FirebaseVisionBarcode.Email> getGetEmails() {
        return getEmails;
    }

    public void setGetEmails(List<FirebaseVisionBarcode.Email> getEmails) {
        this.getEmails = getEmails;
    }

    public FirebaseVisionBarcode.PersonName getGetName() {
        return getName;
    }

    public void setGetName(FirebaseVisionBarcode.PersonName getName) {
        this.getName = getName;
    }

    public List<FirebaseVisionBarcode.Phone> getGetPhones() {
        return getPhones;
    }

    public void setGetPhones(List<FirebaseVisionBarcode.Phone> getPhones) {
        this.getPhones = getPhones;
    }

    public Contact(List<FirebaseVisionBarcode.Address> getAddresses, List<FirebaseVisionBarcode.Email> getEmails, FirebaseVisionBarcode.PersonName getName, String getOrganization, List<FirebaseVisionBarcode.Phone> getPhones, String getTitle, String[] getUrls) {
        this.getAddresses = getAddresses;
        this.getEmails = getEmails;
        this.getName = getName;
        this.getOrganization = getOrganization;
        this.getPhones = getPhones;
        this.getTitle = getTitle;
        this.getUrls = getUrls;
    }

    public String getGetOrganization() {
        return getOrganization;
    }

    public void setGetOrganization(String getOrganization) {
        this.getOrganization = getOrganization;
    }


    public String getGetTitle() {
        return getTitle;
    }

    public void setGetTitle(String getTitle) {
        this.getTitle = getTitle;
    }

    public String[] getGetUrls() {
        return getUrls;
    }

    public void setGetUrls(String[] getUrls) {
        this.getUrls = getUrls;
    }

    public List<FirebaseVisionBarcode.Address> getGetAddresses() {
        return getAddresses;
    }

    public void setGetAddresses(List<FirebaseVisionBarcode.Address> getAddresses) {
        this.getAddresses = getAddresses;
    }

}
