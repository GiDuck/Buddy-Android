package com.example.gdtbg.buddy.buddy_09_myActivityLog;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.RelativeLayout;

import com.example.gdtbg.buddy.R;
import com.example.gdtbg.buddy.VO.ActivityVO;
import com.example.gdtbg.buddy.buddy.CustomActivity;
import com.example.gdtbg.buddy.buddy.ErrorPageInflater;
import com.example.gdtbg.buddy.buddy.LoginInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.gdtbg.buddy.buddy.Utilities.databaseReference;

public class MyActivity_Main extends CustomActivity implements ValueEventListener {

    private RelativeLayout rootView;
    private RecyclerView recyclerView;
    private LinearLayoutManager lim;
    private MyActivity_Adapter adapter;
    private ArrayList<ActivityVO> items;
    private String user_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buddy09_main);
        super.setMenuBar();
        actionbar_title.setText("활동 로그");
        init();
        getData();


    }

    public void init() {

        items = new ArrayList<>();
        rootView = (RelativeLayout) findViewById(R.id.activity_log_root);
        recyclerView = (RecyclerView) findViewById(R.id.activity_log_recycler);
        lim = new LinearLayoutManager(this);
        adapter = new MyActivity_Adapter(this);
        recyclerView.setLayoutManager(lim);
        recyclerView.setAdapter(adapter);
        user_uid = LoginInfo.getInstance(this).getUser_uid();

    }

    public void getData() {

        databaseReference.child("user")
                .child(user_uid)
                .child("myActivity")
                .orderByValue()
                .addValueEventListener(this);


    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        Log.d("기덕", "활동 로그 확인 : " + dataSnapshot.toString());
        if (dataSnapshot.getValue() != null) {

            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                ActivityVO activityVO = snapshot.getValue(ActivityVO.class);
                items.add(activityVO);
            }

            adapter.setData(items);

        } else {
            new ErrorPageInflater(this).setErrorPage(rootView, recyclerView, "활동 로그가 존재 하지 않습니다.");
        }

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
