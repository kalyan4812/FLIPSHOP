package com.example.flipshop.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.flipshop.DashboardActivity;
import com.example.flipshop.R;
import com.example.flipshop.ui.auth.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private  static int SPLASH_SCREEN=5000;
    //variables
    Animation topAnim, bottomAnim;
    ImageView img;
    TextView title, qoute;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        initPreferences();
        //new code


        //Animation
        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);

        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //Hook
        img=findViewById(R.id.img);

        title=findViewById(R.id.title);
        qoute=findViewById(R.id.qoute);

        img.setAnimation(topAnim);
       // image.setAnimation(topAnim);
        title.setAnimation(bottomAnim);
        qoute.setAnimation(bottomAnim);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sharedPreferences.getBoolean("logged_in",false)) {
                    Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();

                }
                else{
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },SPLASH_SCREEN);

    }

    private void initPreferences() {
        sharedPreferences = getSharedPreferences("User_Details", MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }
}
