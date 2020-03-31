package com.sanfulou.qrcode2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.sanfulou.qrcode2020.base.BaseActivity;
import com.sanfulou.qrcode2020.model.Contact;
import com.sanfulou.qrcode2020.model.Display;
import com.sanfulou.qrcode2020.model.Mail;
import com.sanfulou.qrcode2020.model.Phone;
import com.sanfulou.qrcode2020.model.Sms;
import com.sanfulou.qrcode2020.model.Url;
import com.sanfulou.qrcode2020.model.Wifi;
import com.sanfulou.qrcode2020.utils.LogUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Review extends BaseActivity {


    public static final String KEY_DATA = "key_data";
    public static final String KEY_TYPE = "key_type";

    public static void open(Context context, Object list, int type) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, Review.class);
        intent.putExtra(KEY_DATA, (Serializable) list);
        intent.putExtra(KEY_TYPE, type);
        context.startActivity(intent);
    }

    private int getType() {
        if (getIntent() == null) {
            return -1;
        }
        return getIntent().getIntExtra(KEY_TYPE, -1);
    }

    @Override
    protected int initLayouts() {
        return R.layout.activity_review;
    }

    @Override
    protected void initViews() {

        if (getType() == -1) {
            return;
        }
        if (getType() == 0) {
            Contact info = (Contact) getIntent().getSerializableExtra(KEY_DATA);
            try {
                LogUtils.logE(info.getGetOrganization());
                LogUtils.logE(info.getGetTitle());
                LogUtils.logE(info.getGetAddresses().size() + "");
                LogUtils.logE(info.getGetEmails().size() + "");
                LogUtils.logE(info.getGetName().getFirst());
                LogUtils.logE(info.getGetPhones().size() + "");
                LogUtils.logE(info.getGetUrls().length + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        if (getType() == 1) {
            Mail info = (Mail) getIntent().getSerializableExtra(KEY_DATA);
            LogUtils.logE(Objects.requireNonNull(info).toString());
            return;
        }
        if (getType() == 2) {
            Phone info = (Phone) getIntent().getSerializableExtra(KEY_DATA);
            LogUtils.logE(Objects.requireNonNull(info).toString());
            return;
        }
        if (getType() == 3) {
            Sms info = (Sms) getIntent().getSerializableExtra(KEY_DATA);
            try {
                LogUtils.logE(info.getGetMessage());
                LogUtils.logE(info.getGetPhoneNumber());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        if (getType() == 4) {
            Url info = (Url) getIntent().getSerializableExtra(KEY_DATA);
            LogUtils.logE(info.getGetUrl());
            LogUtils.logE(info.getGetTitle());
            return;
        }
        if (getType() == 5) {
            Wifi info = (Wifi) getIntent().getSerializableExtra(KEY_DATA);
            LogUtils.logE(Objects.requireNonNull(info).toString());
            return;
        }
        if (getType() == 6) {
            Display info = (Display) getIntent().getSerializableExtra(KEY_DATA);
            LogUtils.logE(info.getDisplay());
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
