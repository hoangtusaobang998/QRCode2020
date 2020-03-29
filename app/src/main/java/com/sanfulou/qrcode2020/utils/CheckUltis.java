package com.sanfulou.qrcode2020.utils;

import android.text.TextUtils;

public class CheckUltis {

    static boolean isEmai(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    static boolean isLink(String link) {
        if (TextUtils.isEmpty(link)) {
            return false;
        }
        if (link.startsWith("http://") || link.startsWith(" https://") || link.startsWith("ftp://")){
            return true;
        }else return false;
    }

    static boolean isPhone(String link) {
        if (TextUtils.isEmpty(link)) {
            return false;
        }
        if (link.startsWith("http://") || link.startsWith(" https://") || link.startsWith("ftp://")){
            return true;
        }else return false;
    }


}
