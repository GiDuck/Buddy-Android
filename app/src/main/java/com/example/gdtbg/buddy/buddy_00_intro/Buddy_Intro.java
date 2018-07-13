package com.example.gdtbg.buddy.buddy_00_intro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.gdtbg.buddy.R;
import com.example.gdtbg.buddy.buddy.Database.DBHelper;
import com.example.gdtbg.buddy.buddy.Database.Database;
import com.example.gdtbg.buddy.buddy.LoginInfo;
import com.example.gdtbg.buddy.buddy.Services.ReciverManager;
import com.example.gdtbg.buddy.buddy.Utilities;
import com.example.gdtbg.buddy.buddy.VersionManager;
import com.example.gdtbg.buddy.buddy_01_join.Login;
import com.example.gdtbg.buddy.buddy_02_mainboard.Mainboard;

public class Buddy_Intro extends AppCompatActivity {

    public static Utilities util;
    public static VersionManager versionManager;
    public static Database database;

    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.buddy_intro);

        util =new Utilities(this);
        preferences = getSharedPreferences("buddy", MODE_PRIVATE);
        versionManager = new VersionManager();
        database = new Database(this, new DBHelper(this));

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;


                LoginInfo loginInfo = LoginInfo.getInstance(Buddy_Intro.this);
                Log.d("기덕", "인트로 시작 전  : USER UID -> " + loginInfo.getUser_nickname() + "입니다");
                Log.d("기덕", "인트로 시작 전  : USER EMAIL -> " + loginInfo.getUser_email() + "입니다");
                Log.d("기덕", "인트로 시작 전  : USER PROFILE -> " + loginInfo.getUser_profile() + "입니다");


                if (preferences.getString("user_uid", "").equals("")) {

                    Log.d("기덕", "인트로 : USER UID -> " + preferences.getString("user_uid", "") + "입니다");
                    intent = new Intent(Buddy_Intro.this, Login.class);
                    startActivity(intent);

                } else {
                    intent = new Intent(Buddy_Intro.this, Mainboard.class);
                    startActivity(intent);
                    new ReciverManager(Buddy_Intro.this).startServices();


                }

                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        }, 2000);


    }
}
