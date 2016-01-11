package com.tae.asos.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tae.asos.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Eduardo on 20/12/2015.
 */
public class SplashActivity extends AppCompatActivity {

    private static final long TIME_DELAY = 3000;
    private SplashTask task;
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        imageView = (ImageView) findViewById(R.id.img_splash);
        Glide.with(this).load(R.drawable.logo_splash).asGif().animate(R.anim.rotate_hamburguer).crossFade().into(imageView);
//        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_hamburguer);
//        imageView.startAnimation(animation);
        task = new SplashTask();
        task.execute();

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }


    private class SplashTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
            };
            Timer timer = new Timer();
            timer.schedule(timerTask, TIME_DELAY);
            return null;
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
            task = null;
        }
    }

}
