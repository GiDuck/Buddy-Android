package com.example.gdtbg.buddy.buddy.Services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.gdtbg.buddy.VO.MainboardVO;
import com.example.gdtbg.buddy.buddy.interfaces.Receiver;
import com.example.gdtbg.buddy.buddy_02_mainboard.Mainboard;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.gdtbg.buddy.buddy.Utilities.NotiMaker;

/**
 * Created by gdtbg on 2017-11-15.
 */

public class PostReciver extends Service implements ChildEventListener, Receiver {

    private boolean isChecked;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference ref;
    private SharedPreferences preferences;
    private String user_uid;
    private String user_nickname;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference();
        isChecked = false;
        preferences = getSharedPreferences("buddy", MODE_PRIVATE);
        user_uid = preferences.getString("user_uid", "");
        user_nickname = preferences.getString("user_nickname", "");
        Log.d("기덕", "Post Service is running");


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            receive();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {


        MainboardVO new_board = dataSnapshot.getValue(MainboardVO.class);

        if (isChecked) {
            if (!new_board.getBoard_writer_uid().equals(user_uid)) {
                ref.child("user").child(new_board.getBoard_writer_uid()).child("nickname")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String nickname = (String)dataSnapshot.getValue();
                                notification(nickname);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        } else {
            isChecked = true;
        }


    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void receive() {
        ref.child("board").limitToLast(1).addChildEventListener(this);

    }

    @Override
    public void notification(String writer) {

        Intent intent = new Intent(PostReciver.this, Mainboard.class);
        NotiMaker(intent, this, "새로운 글이 등록 되었습니다!");

    }
}
