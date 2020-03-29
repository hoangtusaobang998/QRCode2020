package com.sanfulou.qrcode2020.camera;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.github.florent37.viewanimator.ViewAnimator;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.sanfulou.qrcode2020.R;
import com.sanfulou.qrcode2020.base.BaseActivity;
import com.sanfulou.qrcode2020.utils.LogUtils;
import com.sanfulou.qrcode2020.utils.PemissionUtils;
import com.sanfulou.qrcode2020.utils.QrCodeAnalyzer;
import com.sanfulou.qrcode2020.views.CameraFocusBoxView;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.OnClick;

public class CaptureActivity extends BaseActivity {

    private static final int S = 1000;
    private Preview preview;
    private Context context = CaptureActivity.this;
    private boolean isFlash = false;
    private QrCodeAnalyzer qrCodeAnalyzer;
    private View vClcik;
    @BindView(R.id.preview_camera)
    TextureView cameraPreview;

    @BindView(R.id.focusBoxView)
    CameraFocusBoxView cameraFocusBoxView;

    @BindView(R.id.img_add_qr)
    ImageView imgAddQr;

    @BindView(R.id.img_star)
    ImageView imgStar;

    @BindView(R.id.img_setting)
    ImageView imgSetting;

    @BindView(R.id.img_clock)
    ImageView imgClock;

    @BindView(R.id.img_flash)
    ImageView imgFlash;

    @BindView(R.id.img_share)
    ImageView imgShare;


    @BindView(R.id.img_home)
    ImageView imgHome;

    @OnClick(R.id.img_flash)
    public void clickViiew(View view) {
        ViewAnimator.animate(view).bounceIn().duration(1000).start();
        if (this.preview == null) {
            return;
        }
        if (this.isFlash) {
            turnOffFlash();
            return;
        }
        turnOnFlash();


    }

    @OnClick({R.id.img_add_qr, R.id.img_clock, R.id.img_setting, R.id.img_star, R.id.img_home})
    public void click(View view) {
        this.setIMG(view);
    }

    private void setIMG(View view) {
        if (this.vClcik != null) {
            if (this.vClcik == view) {
                return;
            }
        }
        ViewAnimator.animate(view).bounceIn().duration(S).start();
        if (view == this.imgAddQr) {
            this.imgAddQr.setImageResource(R.drawable.ic_add_qr_on);
            this.imgClock.setImageResource(R.drawable.ic_clock_off);
            this.imgSetting.setImageResource(R.drawable.ic_setting_off);
            this.imgStar.setImageResource(R.drawable.ic_star_off);
        }
        if (view == this.imgClock) {
            this.imgAddQr.setImageResource(R.drawable.ic_add_qr_off);
            this.imgClock.setImageResource(R.drawable.ic_clock_on);
            this.imgSetting.setImageResource(R.drawable.ic_setting_off);
            this.imgStar.setImageResource(R.drawable.ic_star_off);

        }
        if (view == this.imgSetting) {
            this.imgAddQr.setImageResource(R.drawable.ic_add_qr_off);
            this.imgClock.setImageResource(R.drawable.ic_clock_off);
            this.imgSetting.setImageResource(R.drawable.ic_setting_on);
            this.imgStar.setImageResource(R.drawable.ic_star_off);

        }
        if (view == this.imgStar) {
            this.imgAddQr.setImageResource(R.drawable.ic_add_qr_off);
            this.imgClock.setImageResource(R.drawable.ic_clock_off);
            this.imgSetting.setImageResource(R.drawable.ic_setting_off);
            this.imgStar.setImageResource(R.drawable.ic_start_on);
        }

        if (view == this.imgHome) {
            this.imgAddQr.setImageResource(R.drawable.ic_add_qr_off);
            this.imgClock.setImageResource(R.drawable.ic_clock_off);
            this.imgSetting.setImageResource(R.drawable.ic_setting_off);
            this.imgStar.setImageResource(R.drawable.ic_star_off);
        }
        vClcik = view;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (PemissionUtils.permissionsGrantedCamera(this)) {
            cameraPreview.post(this::startCamera);
        } else {
            ActivityCompat.requestPermissions(
                    this, new String[]{REQUIRED_PERMISSIONS_CAMERA}, REQUEST_CODE_PERMISSIONS_CAMERA);
        }
        this.cameraPreview.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> updateTransform());

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (qrCodeAnalyzer != null) {
            qrCodeAnalyzer.setQrcode(true);
        }
    }

    @Override
    protected int initLayouts() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        this.vClcik = imgHome;
        ViewAnimator.animate(cameraFocusBoxView, imgHome).pulse().duration(1000).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS_CAMERA) {
            if (PemissionUtils.permissionsGrantedCamera(this)) {
                this.cameraPreview.post(this::startCamera);
            }
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

    private void startCamera() {
        if (cameraPreview == null) {
            LogUtils.logD("cameraPreview null");
            return;
        }
        this.preview = setPreview();
        ImageCapture imageCapture = new ImageCapture(this.initImageConfig());
        ImageAnalysisConfig.Builder imageAnalysisConfig = new ImageAnalysisConfig.Builder();
        imageAnalysisConfig.setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE);
        ImageAnalysis imageAnalysis = new ImageAnalysis(imageAnalysisConfig.build());
        qrCodeAnalyzer = new QrCodeAnalyzer(i -> {
            if (i == null) {
                return;
            }
            for (FirebaseVisionBarcode barcode : i) {
                LogUtils.logE(barcode.getDisplayValue());
            }
        });

        imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), qrCodeAnalyzer);
        CameraX.bindToLifecycle(this, preview, imageCapture, imageAnalysis);
    }

    private ImageCaptureConfig initImageConfig() {
        return new ImageCaptureConfig.Builder()
                .setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                .setFlashMode(FlashMode.OFF)
                .build();
    }

    private Preview setPreview() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        PreviewConfig.Builder config = new PreviewConfig.Builder();
        config.setTargetResolution(new Size(metrics.widthPixels, metrics.heightPixels));
        PreviewConfig previewConfig = config.build();
        Preview preview = new Preview(previewConfig);
        preview.setOnPreviewOutputUpdateListener(it -> {
            ViewGroup parent = (ViewGroup) this.cameraPreview.getParent();
            if (parent == null) {
                return;
            }
            this.preview.enableTorch(this.isFlash);
            parent.removeView(this.cameraPreview);
            parent.addView(this.cameraPreview, 0);
            this.cameraPreview.setSurfaceTexture(it.getSurfaceTexture());
            this.updateTransform();
        });
        return preview;
    }

    private void updateTransform() {
        if (this.cameraPreview == null) {
            LogUtils.logD("cameraPreview null");
            return;
        }
        Matrix matrix = new Matrix();
        float centerX = cameraPreview.getWidth() / 2f;
        float centerY = cameraPreview.getHeight() / 2f;
        int rotationDegrees = rotationDegrees();
        matrix.postRotate(-(float) rotationDegrees, centerX, centerY);
        this.cameraPreview.setTransform(matrix);
    }

    @SuppressLint("SwitchIntDef")
    private int rotationDegrees() {
        switch (this.cameraPreview.getDisplay().getRotation()) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
            default:
                throw new UnsupportedOperationException(
                        "Unsupported display rotation: ");
        }
    }

    private void turnOnFlash() {
        if (this.preview == null) {
            LogUtils.logD("preview || imgScanFlash null");
            return;
        }
        this.imgFlash.setImageResource(R.drawable.ic_flash_on);
        this.isFlash = true;
        this.preview.enableTorch(true);
    }

    private void turnOffFlash() {
        if (this.preview == null) {
            LogUtils.logD("preview || imgScanFlash null");
            return;
        }
        this.imgFlash.setImageResource(R.drawable.ic_flash_off);
        this.preview.enableTorch(false);
        this.isFlash = false;
    }
}
