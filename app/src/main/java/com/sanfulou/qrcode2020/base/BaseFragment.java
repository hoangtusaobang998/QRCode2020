package com.sanfulou.qrcode2020.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sanfulou.qrcode2020.utils.Consts;

import java.util.Objects;

import butterknife.ButterKnife;

abstract public class BaseFragment extends Fragment implements Consts {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(initLayout(), container, false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    abstract protected int initLayout();

    abstract protected void initView();


}
