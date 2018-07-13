package com.example.gdtbg.buddy.buddy.Services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.example.gdtbg.buddy.VO.ActivityVO;
import com.example.gdtbg.buddy.buddy.interfaces.Receiver;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.gdtbg.buddy.buddy.Utilities.NotiMaker;

public class ActivityReciver extends Service implements Receiver, ChildEventListener {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    SharedPreferences preferences;
    String user_uid;
    private Boolean isCreated;

    public ActivityReciver() {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference();
        preferences = getSharedPreferences("buddy", MODE_PRIVATE);
        user_uid = preferences.getString("user_uid", "");
        isCreated = false;
        Log.d("기덕", "Activity Service is running");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (isCreated) {
            receive();
        }
        isCreated = true;

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void receive() {

        ref.child("user").child(user_uid).child("myActivity").orderByValue().limitToLast(1).addChildEventListener(this);


    }

    @Override
    public void notification(String str) {


        NotiMaker(this, str);

    }


    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

        try {
            ActivityVO activityVO = dataSnapshot.getValue(ActivityVO.class);
            notification(activityVO.getContent());
        } catch (Exception e) {
            e.printStackTrace();
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
}
