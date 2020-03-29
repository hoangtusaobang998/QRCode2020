package com.sanfulou.qrcode2020.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

public class PemissionUtils implements Consts {
    public static boolean permissionsGrantedCamera(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.checkSelfPermission(REQUIRED_PERMISSIONS_CAMERA) == PackageManager.PERMISSION_GRANTED;
        } else
            return true;

    }

}
