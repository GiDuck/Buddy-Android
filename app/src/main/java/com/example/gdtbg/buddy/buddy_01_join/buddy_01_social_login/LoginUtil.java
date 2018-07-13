package com.example.gdtbg.buddy.buddy_01_join.buddy_01_social_login;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.example.gdtbg.buddy.VO.MemberVO;
import com.example.gdtbg.buddy.buddy.LoginInfo;
import com.example.gdtbg.buddy.buddy.Services.ReciverManager;
import com.example.gdtbg.buddy.buddy_01_join.buddy_01_join.Join;
import com.example.gdtbg.buddy.buddy_02_mainboard.Mainboard;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import static com.example.gdtbg.buddy.buddy.Utilities.databaseReference;

/**
 * Created by gdtbg on 2017-11-18.
 */

public class LoginUtil {

    private Activity activity;
    private LoginInfo loginInfo;

    public LoginUtil(Activity activity) {

        this.activity = activity;
        loginInfo = LoginInfo.getInstance(activity.getApplicationContext());

    }

    public final void UserChecker(final String check_uid, final String user_profile, final String user_email, final String login_type) {

        Log.d("기덕", "USER UID : " + check_uid);

        Log.d("기덕", "유저 중복 확인 함수");
        databaseReference.child("user").child(check_uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Intent intent;


                        if (dataSnapshot.getValue() ==  null) {
                            //DATABASE HAS NOT UID
                            intent = new Intent(activity, Join.class);


                            intent.putExtra("user_uid", check_uid);
                            intent.putExtra("user_profile", user_profile);
                            intent.putExtra("user_email", user_email);
                            intent.putExtra("login_type", login_type);
                            Log.d("기덕", "DATABASE HAS NOT UID, MOVE TO JOIN PAGE");
                            Log.d("기덕", "dataSnapShot" + dataSnapshot.toString());
                            Log.d("기덕", "USER UID : " + check_uid);


                            activity.startActivity(intent);


                        } else {
                            //DATABASE HAS UID

                            MemberVO memberVO = dataSnapshot.getValue(MemberVO.class);
                            Log.d("기덕", memberVO.toString());

                            loginInfo.setUser_nickname(dataSnapshot.getKey());
                            if (memberVO.getProfilePhoto() == null) {
                                memberVO.setProfilePhoto("https://www.adjust.com/new-assets/images/site-images/interface/user.svg");
                            } else {
                                loginInfo.setUser_profile(memberVO.getProfilePhoto());
                            }
                            loginInfo.setUser_email(memberVO.getEmail());
                            loginInfo.setUser_uid(memberVO.getUid());

                            Log.d("기덕", "넘어온 데이터 : " + dataSnapshot.getKey() + memberVO.getNickname() + memberVO.getProfilePhoto());
                            Log.d("기덕", "dataSnapShot" + dataSnapshot.toString());


                            intent = new Intent(activity, Mainboard.class);

                            Log.d("기덕", "DATABASE HAS UID, MOVE TO MAINBOARD PAGE");
                            Log.d("기덕", "USER UID : " + check_uid);


                            activity.startActivity(intent);
                            new ReciverManager(activity).startServices();

                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        Log.d("버디", "UID 중복 체크 오류 발생. 에러 내역 : " + databaseError.getDetails());

                    }
                });

    }


}
