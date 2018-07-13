package com.example.gdtbg.buddy.buddy_01_join.buddy_01_social_login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.gdtbg.buddy.R;
import com.example.gdtbg.buddy.buddy.interfaces.SocialLogin;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by gdtbg on 2017-11-18.
 */

public class Facebook extends Activity implements SocialLogin {


    private CallbackManager callbackManager;
    private String user_uid;
    private String user_profile;
    private Activity activity;

    public Facebook() {
    }

    public Facebook(final Activity activity) {

        this.activity = activity;

        callbackManager = CallbackManager.Factory.create();
        com.facebook.login.widget.LoginButton loginButton = (com.facebook.login.widget.LoginButton) activity.findViewById(R.id.facebook_sogn_in_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    user_uid = String.valueOf(object.get("id")); //페이스북 프로필 정보에서 uid정보를 받아온다
                                    user_profile = "https://graph.facebook.com/" + String.valueOf(object.get("id")) + "/picture"; //받아온 uid정보를 이용하여 프로필 사진의 주소를 받아온다
                                    new LoginUtil(activity).UserChecker(user_uid, user_profile, null, "facebook");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.v("result", object.toString());
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("friends", "id, name, email, gender, birthday");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("LoginErr", error.toString());
            }
        });


    }

    @Override
    public void login() {



    }

    @Override
    public void logout() {
        LoginManager.getInstance().logOut();
    }


    public void getResult(int requestCode, int resultCode, Intent data) {

        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.d("기덕","페이스북 로그인 성공");

    }
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }*/
}
