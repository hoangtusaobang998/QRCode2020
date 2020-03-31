package com.sanfulou.qrcode2020.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraX;
import androidx.camera.core.FlashMode;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageAnalysisConfig;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.core.app.ActivityCompat;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.sanfulou.qrcode2020.R;
import com.sanfulou.qrcode2020.Review;
import com.sanfulou.qrcode2020.base.BaseFragment;
import com.sanfulou.qrcode2020.model.Contact;
import com.sanfulou.qrcode2020.model.Display;
import com.sanfulou.qrcode2020.model.EventScrollPager;
import com.sanfulou.qrcode2020.model.Mail;
import com.sanfulou.qrcode2020.model.Phone;
import com.sanfulou.qrcode2020.model.Sms;
import com.sanfulou.qrcode2020.model.Url;
import com.sanfulou.qrcode2020.model.Wifi;
import com.sanfulou.qrcode2020.utils.LogUtils;
import com.sanfulou.qrcode2020.utils.PemissionUtils;
import com.sanfulou.qrcode2020.utils.QrCodeAnalyzer;
import com.sanfulou.qrcode2020.views.CameraFocusBoxView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeFragment extends BaseFragment {

    private static final int S = 1000;
    private Preview preview;
    private boolean isFlash = false;
    private QrCodeAnalyzer qrCodeAnalyzer;
    private Vibrator vibrator;
    private int type = -1;


    @BindView(R.id.preview_camera)
    TextureView cameraPreview;

    @BindView(R.id.focusBoxView)
    CameraFocusBoxView cameraFocusBoxView;

    @BindView(R.id.img_flash)
    ImageView imgFlash;

    @BindView(R.id.img_share)
    ImageView imgShare;

    @OnClick(R.id.img_flash)
    void clickViiew(View view) {
        ViewAnimator.animate(view).bounceIn().duration(S).start();
        if (this.preview == null) {
            return;
        }
        if (this.isFlash) {
            turnOffFlash(false);
            return;
        }
        turnOnFlash();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (qrCodeAnalyzer != null) {
            qrCodeAnalyzer.setQrcode(true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventScrollPager event) {
        LogUtils.logE(event.is + "");
        if (event.is) {
            qrCodeAnalyzer.setQrcode(false);
        } else {
            qrCodeAnalyzer.setQrcode(true);
        }
        if (this.isFlash && event.is) {
            this.turnOffFlash(this.isFlash);
        }
        if (this.isFlash && !event.is) {
            this.turnOnFlash();

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected int initLayout() {
        return R.layout.home_fragment;
    }

    @Override
    protected void initView() {
        if (getActivity() == null) {
            return;
        }
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        if (PemissionUtils.permissionsGrantedCamera(getContext())) {
            cameraPreview.post(this::startCamera);
        } else {
            ActivityCompat.requestPermissions(
                    getActivity(), new String[]{REQUIRED_PERMISSIONS_CAMERA}, REQUEST_CODE_PERMISSIONS_CAMERA);
        }
        this.cameraPreview.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> updateTransform());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (getActivity() == null) {
            return;
        }
        if (requestCode == REQUEST_CODE_PERMISSIONS_CAMERA) {
            if (PemissionUtils.permissionsGrantedCamera(getContext())) {
                this.cameraPreview.post(this::startCamera);
            }
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
                LogUtils.logE("I null");
                return;
            }
            if (i.isEmpty()) {
                LogUtils.logE("I isEmpty");
                return;
            }
            if (this.vibrator == null) {
                LogUtils.logE("vibrator null");
                return;
            }
            if (vibrator.hasVibrator()) {
                vibrator.vibrate(100);
            }
            qrCodeAnalyzer.setQrcode(false);
            FirebaseVisionBarcode barcode = i.get(0);
            Object oj = this.getObject(barcode);
            if (this.type == -1) {
                return;
            }
            ViewAnimator.animate(cameraFocusBoxView).pulse().duration(1000).onStop(() -> {
                Review.open(getContext(), oj, type);
                type = -1;
            }).start();

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

    private void turnOffFlash(boolean is) {
        if (this.preview == null) {
            LogUtils.logD("preview || imgScanFlash null");
            return;
        }
        this.imgFlash.setImageResource(R.drawable.ic_flash_off);
        this.preview.enableTorch(false);
        this.isFlash = is;
    }


    private Object getObject(FirebaseVisionBarcode barcode) {
        if (barcode.getContactInfo() != null) {
            this.type = 0;
            Contact contact = new Contact();
            contact.setGetAddresses(barcode.getContactInfo().getAddresses());
            contact.setGetEmails(barcode.getContactInfo().getEmails());
            contact.setGetName(barcode.getContactInfo().getName());
            contact.setGetOrganization(barcode.getContactInfo().getOrganization());
            contact.setGetPhones(barcode.getContactInfo().getPhones());
            contact.setGetUrls(barcode.getContactInfo().getUrls());
            contact.setGetTitle(barcode.getContactInfo().getTitle());
            return contact;
        }
        if (barcode.getEmail() != null) {
            this.type = 1;
            Mail mail = new Mail();
            mail.setGetType(String.valueOf(barcode.getEmail().getType()));
            mail.setGetAddress(barcode.getEmail().getAddress());
            mail.setGetBody(barcode.getEmail().getBody());
            mail.setGetSubject(barcode.getEmail().getSubject());
            return mail;
        }
        if (barcode.getPhone() != null) {
            this.type = 2;
            Phone phone = new Phone();
            phone.setGetType(String.valueOf(barcode.getPhone().getType()));
            phone.setGetNumber(barcode.getPhone().getNumber());
            return phone;
        }
        if (barcode.getSms() != null) {
            this.type = 3;
            Sms sms = new Sms();
            sms.setGetMessage(barcode.getSms().getMessage());
            sms.setGetPhoneNumber(barcode.getSms().getPhoneNumber());
            return sms;
        }

        if (barcode.getUrl() != null) {
            this.type = 4;
            Url url = new Url();
            url.setGetUrl(barcode.getUrl().getUrl());
            url.setGetTitle(barcode.getUrl().getTitle());
            return url;
        }

        if (barcode.getWifi() != null) {
            this.type = 5;
            Wifi wifi = new Wifi();
            wifi.setGetSsid(barcode.getWifi().getSsid());
            wifi.setGetEncryptionType(String.valueOf(barcode.getWifi().getEncryptionType()));
            wifi.setGetPassword(barcode.getWifi().getPassword());
            return wifi;
        }
        if (barcode.getDisplayValue() != null) {
            this.type = 6;
            Display display = new Display();
            display.setDisplay(barcode.getDisplayValue());
            return display;
        }
        this.type = -1;
        return null;
    }

}
