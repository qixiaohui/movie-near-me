package com.qi.xiaohui.movienearme.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.qi.xiaohui.movienearme.R;

/**
 * Created by TQi on 4/8/16.
 */
public class SplashActivity extends AppCompatActivity {
    private ImageView splash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splash = (ImageView) findViewById(R.id.splashIcon);
        splash.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate));
    }
}
