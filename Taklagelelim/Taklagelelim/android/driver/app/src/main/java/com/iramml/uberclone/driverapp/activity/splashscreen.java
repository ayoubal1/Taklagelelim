package com.iramml.uberclone.driverapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.iramml.uberclone.driverapp.R;

public class splashscreen extends AppCompatActivity {
    Animation anim;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen);


            anim= AnimationUtils.loadAnimation(this,R.anim.top_anim);

            //imageView.findViewById(R.id.imfub);

            //imageView.setAnimation(anim);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(splashscreen.this,LoginActivity.class);
                    startActivity(intent);
                }
            },5000);


    }
}