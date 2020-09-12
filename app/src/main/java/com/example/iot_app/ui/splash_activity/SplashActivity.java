package com.example.iot_app.ui.splash_activity;

import android.app.ActionBar;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.VideoView;

import com.example.iot_app.R;
import com.example.iot_app.ui.base.BaseActivity;
import com.example.iot_app.ui.base.IBaseView;
import com.example.iot_app.ui.main.MainActivity;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity implements ISplashActivity {

    @Inject
    SplashPresenter mSplashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getIActivityComponent().inject(this);

        mSplashPresenter.onAttached(this);

        try {
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            linearLayout.setGravity(Gravity.CENTER);
            VideoView videoHolder = new VideoView(this);
            linearLayout.addView(videoHolder);
            setContentView(linearLayout);
            Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.intro_video);
            videoHolder.setVideoURI(video);

            videoHolder.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    jump();
//                    MainActivity.open(getApplicationContext());
                }
            });
            videoHolder.start();
        } catch (Exception ex) {
           jump();
//            MainActivity.open(getApplicationContext());
        }

    }







    @Override
    public boolean onTouchEvent(MotionEvent event) {
    jump();
        return true;
    }

    private void jump() {
        if (isFinishing())
            return;
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }





    @Override
    public void openMainActivity() {
        MainActivity.open(getApplicationContext());
    }
}
