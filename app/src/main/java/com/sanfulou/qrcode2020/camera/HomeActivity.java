package com.sanfulou.qrcode2020.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraX;
import androidx.camera.core.FlashMode;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageAnalysisConfig;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.github.florent37.viewanimator.ViewAnimator;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.sanfulou.qrcode2020.R;
import com.sanfulou.qrcode2020.adapter.FragmentAdapter;
import com.sanfulou.qrcode2020.base.BaseActivity;
import com.sanfulou.qrcode2020.model.EventScrollPager;
import com.sanfulou.qrcode2020.utils.LogUtils;
import com.sanfulou.qrcode2020.utils.PemissionUtils;
import com.sanfulou.qrcode2020.utils.QrCodeAnalyzer;
import com.sanfulou.qrcode2020.views.CameraFocusBoxView;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity {

    private static final int S = 1000;
    private Context context = HomeActivity.this;
    private FragmentAdapter fragmentAdapter;
    private View vClcik;

    @BindView(R.id.img_add_qr)
    ImageView imgAddQr;

    @BindView(R.id.img_star)
    ImageView imgStar;

    @BindView(R.id.img_setting)
    ImageView imgSetting;

    @BindView(R.id.img_clock)
    ImageView imgClock;

    @BindView(R.id.img_home)
    ImageView imgHome;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @OnClick({R.id.img_add_qr, R.id.img_clock, R.id.img_setting, R.id.img_star, R.id.img_home})
    public void click(View view) {
        this.setIMG(view, -1);
    }

    private void setIMG(View view, int position) {
        if (this.vClcik != null) {
            if (this.vClcik == view) {
                return;
            }
        }
        if (position != 2) {
            EventBus.getDefault().post(new EventScrollPager(true));
        }else {
            EventBus.getDefault().post(new EventScrollPager(false));
        }
        if (view == this.imgAddQr || position == 1) {
            this.viewPager.setCurrentItem(1);
            this.imgAddQr.setImageResource(R.drawable.ic_add_qr_on);
            this.imgClock.setImageResource(R.drawable.ic_clock_off);
            this.imgSetting.setImageResource(R.drawable.ic_setting_off);
            this.imgStar.setImageResource(R.drawable.ic_star_off);
            this.animator(this.imgAddQr);
            this.vClcik=this.imgAddQr;

        }
        if (view == this.imgClock || position == 0) {
            this.viewPager.setCurrentItem(0);
            this.imgAddQr.setImageResource(R.drawable.ic_add_qr_off);
            this.imgClock.setImageResource(R.drawable.ic_clock_on);
            this.imgSetting.setImageResource(R.drawable.ic_setting_off);
            this.imgStar.setImageResource(R.drawable.ic_star_off);
            this.animator(this.imgClock);
            this.vClcik=this.imgClock;
        }
        if (view == this.imgSetting || position == 4) {
            this.viewPager.setCurrentItem(4);
            this.imgAddQr.setImageResource(R.drawable.ic_add_qr_off);
            this.imgClock.setImageResource(R.drawable.ic_clock_off);
            this.imgSetting.setImageResource(R.drawable.ic_setting_on);
            this.imgStar.setImageResource(R.drawable.ic_star_off);
            this.animator(this.imgSetting);
            this.vClcik=this.imgSetting;
        }
        if (view == this.imgStar || position == 3) {
            this.viewPager.setCurrentItem(3);
            this.imgAddQr.setImageResource(R.drawable.ic_add_qr_off);
            this.imgClock.setImageResource(R.drawable.ic_clock_off);
            this.imgSetting.setImageResource(R.drawable.ic_setting_off);
            this.imgStar.setImageResource(R.drawable.ic_start_on);
            this.animator(this.imgStar);
            this.vClcik=this.imgStar;
        }

        if (view == this.imgHome || position == 2) {
            this.viewPager.setCurrentItem(2);
            this.imgAddQr.setImageResource(R.drawable.ic_add_qr_off);
            this.imgClock.setImageResource(R.drawable.ic_clock_off);
            this.imgSetting.setImageResource(R.drawable.ic_setting_off);
            this.imgStar.setImageResource(R.drawable.ic_star_off);
            this.animator(this.imgHome);
            this.vClcik=this.imgHome;
        }

    }

    private void animator(View view) {
        ViewAnimator.animate(view).bounceIn().duration(S).start();
    }

    @Override
    protected int initLayouts() {
        return R.layout.main;
    }

    @Override
    protected void initViews() {
        this.vClcik = imgHome;
        this.fragmentAdapter = FragmentAdapter.initFragmentAdapter(getSupportFragmentManager());
        this.viewPager.setAdapter(fragmentAdapter);
        this.viewPager.setOffscreenPageLimit(5);
        this.viewPager.setCurrentItem(2);
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setIMG(null, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
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
