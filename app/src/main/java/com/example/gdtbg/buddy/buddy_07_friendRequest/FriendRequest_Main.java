package com.example.gdtbg.buddy.buddy_07_friendRequest;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.gdtbg.buddy.R;
import com.example.gdtbg.buddy.VO.FriendVO;
import com.example.gdtbg.buddy.buddy.CustomActivity;
import com.example.gdtbg.buddy.buddy.ErrorPageInflater;
import com.example.gdtbg.buddy.buddy.LoginInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.gdtbg.buddy.buddy.Utilities.databaseReference;

public class FriendRequest_Main extends CustomActivity {

    RecyclerView rv;
    FriendRequest_Adapter adapter;
    LinearLayoutManager lim;
    ArrayList<FriendVO> items;
    String user_uid;

    RelativeLayout root;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buddy07_friend_request_main);
        super.setMenuBar();
        actionbar_title.setText("친구 요청");

        root = (RelativeLayout) findViewById(R.id.root);
        user_uid = LoginInfo.getInstance(this).getUser_uid();

        rv = (RecyclerView) findViewById(R.id.friend_request_recyclerview);
        adapter = new FriendRequest_Adapter(this, items);
        lim = new LinearLayoutManager(this);
        rv.setLayoutManager(lim);
        rv.setAdapter(adapter);
        getRequestFriend();
    }

    /*------------------- 친구 목록을 불러오는 메소드 -------------------*/


    public void getRequestFriend() {

        items = new ArrayList<>();
        databaseReference.child("user")
                .child(user_uid)
                .child("friendList")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            FriendVO friend = snapshot.getValue(FriendVO.class);

                            if (!friend.getVerified()) {
                                items.add(friend);
                            }


                        }


                        if (items == null || items.size() == 0) {


                            new ErrorPageInflater(FriendRequest_Main.this).setErrorPage(root, rv, "친구요청이 없습니다!");

                        } else {
                            adapter.add(items);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        Toast.makeText(FriendRequest_Main.this, "요청중인 친구 목록이 없습니다.", Toast.LENGTH_SHORT).show();

                    }
                });


    }


}
