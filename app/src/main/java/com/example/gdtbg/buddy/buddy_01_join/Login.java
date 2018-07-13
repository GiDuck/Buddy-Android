package com.example.gdtbg.buddy.buddy_01_join;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.gdtbg.buddy.R;
import com.example.gdtbg.buddy.buddy_01_join.buddy_01_social_login.Facebook;
import com.example.gdtbg.buddy.buddy_01_join.buddy_01_social_login.Google;
import com.example.gdtbg.buddy.buddy_01_join.buddy_01_social_login.Kakao;
import com.facebook.FacebookSdk;

public class Login extends AppCompatActivity {

    Facebook facebook;
    Kakao kakao;
    Google google;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext()); // SDK 초기화 (setContentView 보다 먼저 실행되어야합니다. 안그럼 에러납니다.
        setContentView(R.layout.activity_login);

        google = new Google(this);
        facebook = new Facebook(this);
        kakao = new Kakao(this);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebook.getResult(requestCode, resultCode, data);
        google.getResult(requestCode, resultCode, data);
        kakao.getResult(requestCode, resultCode, data);

    }
}
