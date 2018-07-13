package com.example.gdtbg.buddy.buddy.Services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.gdtbg.buddy.VO.MemberVO;
import com.example.gdtbg.buddy.buddy.interfaces.Receiver;
import com.example.gdtbg.buddy.buddy_04_chat.Chat;
import com.example.gdtbg.buddy.buddy_05_chatroom.Chatroom;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.example.gdtbg.buddy.buddy.Utilities.NotiMaker;

/**
 * Created by gdtbg on 2017-11-19.
 */

public class ChatReciver extends Service implements Receiver, ValueEventListener {


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference ref;
    private SharedPreferences preferences;

    private String user_uid;
    private String user_nickname;

    private ArrayList<String> chatroomList;

    private boolean isFirst;

    @Override
    public void onCreate() {
        super.onCreate();

        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference();
        preferences = getSharedPreferences("buddy", MODE_PRIVATE);
        user_uid = preferences.getString("user_uid", "");
        user_nickname = preferences.getString("user_nickname", "");
        chatroomList = new ArrayList<>();
        isFirst = false;
        Log.d("기덕", "Chat Service is running");


        getChatroomList();


    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getChatroomList();
        receive();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void receive() {

        try {
            for (String uid : chatroomList) {
                Log.d("기덕", "리시브 중");
                Log.d("기덕", uid);

                ref.child("lastChat").child(uid).addValueEventListener(this);


            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void notification(String str) {

    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        if (!isFirst) {
            isFirst = true;
            return;
        }
        try {
            HashMap<String, String> lastChat = (HashMap<String, String>) dataSnapshot.getValue();

            Log.d("기덕", "해쉬맵 : " + lastChat.get("lc_name"));
            boolean checker = isChatActivityTop();
            Log.d("기덕", "체커 " + checker);

            if (checker) {
                if (!lastChat.get("lc_name").equals(user_nickname)) {
                    Intent intent = new Intent(ChatReciver.this, Chatroom.class);
                    String text = lastChat.get("lc_name") + "님이 메세지를 보내셨습니다.";
                    NotiMaker(intent, this, text);


                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    private boolean isChatActivityTop() {

        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List info = activityManager.getRunningAppProcesses();
        Log.d("기덕", "1");
        for (int i = 0; i < info.size(); i++) {
            Log.d("기덕", "2");

            if (!info.get(0).getClass().equals(Chat.class.getClass().getName())) {
                Log.d("기덕", "현재 Chat Activity 실행 중");

                return true;


            } else {

            }
        }
        return false;

    }

    private void getChatroomList() {

        Log.d("기덕", "채팅룸 리스트 받아오기");


        ref.child("user").child(user_uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    MemberVO member = dataSnapshot.getValue(MemberVO.class);

                    if (member.getChatroomList() == null) {

                        Log.d("기덕", "채팅룸 리스트 비었음");

                    } else {


                        Iterator<String> iterator = member.getChatroomList().keySet().iterator();

                        for (String key; iterator.hasNext(); ) {

                            key = iterator.next();

                            chatroomList.add(key);

                        }

                        Log.d("기덕", "채팅룸 리스트 " + chatroomList.size());
                        receive();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}

