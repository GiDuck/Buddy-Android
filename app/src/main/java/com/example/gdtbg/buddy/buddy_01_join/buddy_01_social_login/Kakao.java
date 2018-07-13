package com.example.gdtbg.buddy.buddy_01_join.buddy_01_social_login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.example.gdtbg.buddy.R;
import com.example.gdtbg.buddy.buddy.interfaces.SocialLogin;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

/**
 * Created by gdtbg on 2017-11-18.
 */

public class Kakao extends AppCompatActivity implements SocialLogin {

    //-------------------------- 카카오 필드 --------------------------
    private SessionCallback callback;
    LinearLayout success_layout;
    LoginButton loginButton;
    AQuery aQuery;

    String user_uid;

    String user_email;
    String user_profile;

    Activity activity;
    private Long mLastClickTime;

    public Kakao() {
    }

    public Kakao(final Activity activity) {
        this.activity = activity;
        mLastClickTime = 0L;

        aQuery = new AQuery(this);
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);

        //카카오톡 로그인 버튼
        loginButton = (LoginButton) activity.findViewById(R.id.com_kakao_login);
        loginButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(SystemClock.elapsedRealtime() - mLastClickTime <20){
                        return false;
                    }

                    if (!isConnected()) {
                        Toast.makeText(activity, "인터넷 연결을 확인해 주세요", Toast.LENGTH_SHORT).show();
                    }
                }

                mLastClickTime = SystemClock.elapsedRealtime();

                if (isConnected()) {
                    login();
                    return false;
                } else {
                    return true;
                }
            }
        });


    }

    @Override
    public void login() {
        if (Session.getCurrentSession().isOpened()) {
            requestMe();
        }
    }

    @Override
    public void logout() {

        requestLogout();

    }



    /*------------------------------------ 이너 클래스 ------------------------------------*/

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {//카카오 로그인 성공시 인텐트 전환하는 메소드


        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
        }
    }


    /*------------------------------------ 인터넷 상태 확인 메소드 ------------------------------------*/
    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    private void requestLogout() {
        //success_layout.setVisibility(View.GONE);
        loginButton.setVisibility(View.VISIBLE);
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "로그아웃 성공", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void requestMe() {

        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Log.e("onFailure", errorResult + "");
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.e("onSessionClosed", errorResult + "");
            }

            @Override
            //카카오 로그인에 성공했을때 userprofile을 받아온다
            public void onSuccess(UserProfile userProfile) {
                Log.e("onSuccess", userProfile.toString());
                user_uid = String.valueOf(userProfile.getId());
                Log.e("기덕 : ", "카카오 아이디 : " + user_uid);
                user_profile = userProfile.getThumbnailImagePath();
                user_email = userProfile.getEmail();
                if (user_uid != null) {
                   // Log.e("thumb : ", user_profile);
                    new LoginUtil(activity).UserChecker(user_uid, user_profile, user_email, "kakao");

                } else {
                    Log.e("thumb : ", "null");
                }



            }

            @Override
            public void onNotSignedUp() {
                Log.e("onNotSignedUp", "onNotSignedUp");

            }
        });
    }


    public void getResult(int requestCode, int resultCode, Intent data){

        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            Log.d("기덕","카카오 로그인 성공");
            return;
        }

    }


 /*   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        requestLogout();
        Session.getCurrentSession().removeCallback(callback);
    }
}
