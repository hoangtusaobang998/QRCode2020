package com.sanfulou.qrcode2020.srceen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.sanfulou.qrcode2020.R;
import com.sanfulou.qrcode2020.camera.HomeActivity;

public class Screen extends AppCompatActivity {
    private ImageView img;
    private TextView txt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        img = findViewById(R.id.img);
        txt = findViewById(R.id.txt);
        txt.setVisibility(View.VISIBLE);
        ViewAnimator.animate(txt).slideBottomIn().duration(5000).onStop(() -> {
            startActivity(new Intent(Screen.this, HomeActivity.class));
            finish();
        }).start();
    }
}
