package com.example.gdtbg.buddy.buddy_01_join.buddy_01_social_login;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.gdtbg.buddy.R;
import com.example.gdtbg.buddy.buddy.interfaces.SocialLogin;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Created by gdtbg on 2017-11-18.
 */

public class Google extends AppCompatActivity implements SocialLogin, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private String user_uid;
    private String user_profile;
    private String user_email;


    //-------------------------- 구글 필드 --------------------------
    SignInButton signInButton;
    TextView statusTextView;
    GoogleApiClient mGoogleApiClient;
    public static String TAG = "SigninActivity";
    public static int RC_SIGN_IN = 9001;
    private Activity activity;

    public Google() {
    }

    public Google(Activity activity) {
        this.activity = activity;
        //구글 로그인 옵션 객체 생성
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //생성된 구글 로그인 옵션 객체를 이용해 GoogleAPI클라이언트 생성성
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton = (SignInButton) activity.findViewById(R.id.google_sign_in_button);
        signInButton.setOnClickListener(this);

    }

    @Override
    public void login() {



        Intent signinIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        activity.startActivityForResult(signinIntent, RC_SIGN_IN); //activity 실행 이후 사용자의 데이터를 받아온다


    }

    @Override
    public void logout() {

                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                statusTextView.setText("Signed out");
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.google_sign_in_button: //구글 로그인
                Log.e("구글로그인 버튼", "버튼 눌러짐 리스너에 넘어옴");
                login();
                break;
        }
    }


    //구글 로그인 성공시 인텐트 전환하는 메소드
    public void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        Log.d("기덕","구글 로그인 성공");

        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount(); //Google로그인 계정을 받아온다
            user_uid = acct.getId(); //구글uid
            user_profile = String.valueOf(acct.getPhotoUrl()); //구글 프로필사진
            user_email = String.valueOf(acct.getEmail());
        }


        new LoginUtil(activity).UserChecker(user_uid, user_profile, user_email, "google");


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed" + connectionResult);
    }


    public void getResult(int requestCode, int resultCode, Intent data){


        if (requestCode == RC_SIGN_IN) {
            //Intent로부터 받아온 데이터에서 Google로그인 결과 객체를 받아온다
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            //Intent로부터 받아온 데이터에서 Google로그인 결과 객체를 받아온다
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }*/
}
