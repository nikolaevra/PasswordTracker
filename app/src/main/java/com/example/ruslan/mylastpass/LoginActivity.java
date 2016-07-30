package com.example.ruslan.mylastpass;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.IOException;


public class LoginActivity extends AppCompatActivity implements
        FingerprintHandler.OnAuthenticationSucceededListener,
        FingerprintHandler.OnAuthenticationErrorListener{

    private FingerprintHandler mFingerprintHandler;
    ImageView imageView1, imageView2;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFingerprintHandler = new FingerprintHandler(this);
        mFingerprintHandler.setOnAuthenticationSucceededListener(this);
        mFingerprintHandler.setOnAuthenticationFailedListener(this);

        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        textView = (TextView) findViewById(R.id.textView);
        textView.setTextColor(Color.WHITE);

        VideoView videoView =(VideoView)findViewById(R.id.vv01);

        //specify the location of media file
        Uri myUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.login_footage);

        //Setting MediaController and URI, then starting the videoView
        videoView.setVideoURI(myUri);
        videoView.requestFocus();
        videoView.start();
    }

    @Override
    public void onAuthFailed() {
        imageView1.setImageResource(R.drawable.decline);
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        imageView1.startAnimation(shake);

        textView.setText(R.string.failed);
        textView.setTextColor(Color.RED);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView1.setImageResource(R.drawable.fingerprint);
                textView.setText(R.string.login_prompt);
                textView.setTextColor(Color.GRAY);
            }
        }, 1000);
    }

    @Override
    public void onAuthSucceeded() {
        imageView1.animate().alpha(0f).setDuration(300);
        imageView2.animate().alpha(1f).rotationBy(90f).setDuration(1000);

        textView.setText(R.string.success);
        textView.setTextColor(Color.GREEN);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), PassList.class);
                startActivity(intent);
                imageView2.animate().alpha(0f).rotationBy(-90f).setDuration(0);
            }
        }, 1000);
    }

    @Override
    public void onResume() {
        super.onResume();
        imageView1.animate().alpha(1f).setDuration(0);
        imageView2.animate().alpha(0f).setDuration(0);

        mFingerprintHandler.startListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        mFingerprintHandler.stopListening();
    }
}



