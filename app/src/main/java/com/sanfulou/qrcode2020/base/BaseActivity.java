package com.sanfulou.qrcode2020.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sanfulou.qrcode2020.utils.Consts;

import butterknife.ButterKnife;

abstract public class BaseActivity extends AppCompatActivity implements Consts {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayouts());
        ButterKnife.bind(this);
        initViews();
    }


    abstract protected int initLayouts();

    abstract protected void initViews();
}
