package com.sanfulou.qrcode2020.adapter;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sanfulou.qrcode2020.fragment.AddQRcodeFragment;
import com.sanfulou.qrcode2020.fragment.ClockFragment;
import com.sanfulou.qrcode2020.fragment.HomeFragment;
import com.sanfulou.qrcode2020.fragment.SettingFragment;
import com.sanfulou.qrcode2020.fragment.StarFragment;

public class FragmentAdapter extends FragmentPagerAdapter {

    public static FragmentAdapter initFragmentAdapter(FragmentManager fm) {
        if (fm == null) {
            return null;
        }
        return new FragmentAdapter(fm);
    }

    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ClockFragment();

            case 1:
                return new AddQRcodeFragment();

            case 2:
                return new HomeFragment();

            case 3:
                return new StarFragment();

            case 4:
                return new SettingFragment();

            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
