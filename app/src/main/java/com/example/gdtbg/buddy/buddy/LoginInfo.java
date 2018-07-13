package com.example.gdtbg.buddy.buddy;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gdtbg on 2017-11-18.
 */

public class LoginInfo {

    private static LoginInfo ourInstance;
    private Context context;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public LoginInfo(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("buddy", Context.MODE_PRIVATE);
        editor = preferences.edit();


    }

    public static LoginInfo getInstance(Context context) {
        if (ourInstance == null) {
            //Always pass in the Application Context
            ourInstance = new LoginInfo(context);
        }
        return ourInstance;
    }

    public String getUser_uid() {
        return preferences.getString("user_uid", "");
    }

    public void setUser_uid(String user_uid) {
        editor.putString("user_uid", user_uid);
        editor.commit();
    }

    public String getUser_nickname() {
        return preferences.getString("user_nickname", "");
    }

    public void setUser_nickname(String user_nickname) {
        editor.putString("user_nickname", user_nickname);
        editor.commit();
    }

    public String getUser_email() {
        return preferences.getString("user_email", "");
    }

    public void setUser_email(String user_email) {
        editor.putString("user_email", user_email);
        editor.commit();

    }

    public String getUser_profile() {
        return preferences.getString("user_profile", "");
    }

    public void setUser_profile(String user_profile) {
        editor.putString("user_profile", user_profile);
        editor.commit();

    }
};
