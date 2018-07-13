package com.example.gdtbg.buddy.buddy_05_chatroom;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.gdtbg.buddy.R;
import com.example.gdtbg.buddy.VO.ChatroomVO;
import com.example.gdtbg.buddy.buddy.CustomActivity;
import com.example.gdtbg.buddy.buddy.ErrorPageInflater;
import com.example.gdtbg.buddy.buddy.LoginInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.gdtbg.buddy.buddy.Utilities.databaseReference;
import static com.example.gdtbg.buddy.buddy_00_intro.Buddy_Intro.database;


public class Chatroom extends CustomActivity {

    private RecyclerView rv;
    private ArrayList<ChatroomVO> items; // 채팅방 리스트
    private Chatroom_Adapter adapter;
    private String user_uid;
    private RelativeLayout root;
    private ArrayList<String> chatroomUids; //채팅방 uid (key 집합)
    private LoginInfo loginInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buddy05_main);
        super.setMenuBar();
        actionbar_title.setText("쪽지함");

        loginInfo = LoginInfo.getInstance(this);
        user_uid = loginInfo.getUser_uid();

        root = (RelativeLayout) findViewById(R.id.rootView);

        items = new ArrayList<>();
        chatroomUids = new ArrayList<>();

        adapter = new Chatroom_Adapter(this, items, chatroomUids);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv = (RecyclerView) findViewById(R.id.chatroom_recyclerview);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(adapter);


        databaseReference.child("user").child(user_uid).child("chatroomList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<ChatroomVO> chatrooms = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                    chatrooms.add(snapshot.getValue(ChatroomVO.class));
                    chatroomUids.add(snapshot.getKey());
                }


                if (chatrooms == null || chatrooms.size() == 0) {

                    new ErrorPageInflater(Chatroom.this).setErrorPage(root, rv, "새로운 채팅 목록이 존재하지 않습니다.");

                } else {
                    adapter.add(chatrooms);
                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

                new ErrorPageInflater(Chatroom.this).setErrorPage(root, rv, "채팅목록을 불러오는데 실패 하였습니다.");
            }
        });


    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {


        final int nPosition = item.getOrder();

        if (item.getItemId() == 1) {
            deleteChatroom(nPosition);
        }

        return super.onContextItemSelected(item);
    }


    /*------------------- 쪽지 목록을 삭제 하는 메소드 -------------------*/


    public void deleteChatroom(final int nPosition){

        //다이얼로그 생성
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("쪽지를 삭제 하시겠습니까? 삭제된 쪽지는 복구가 불가합니다.");

        alertDialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                //"네" 클릭시 댓글 수정

                databaseReference.child("user").child(user_uid).child("chatroomList").child(chatroomUids.get(nPosition)).removeValue();

                // TODO: 2017-11-10 sqlite 채팅 테이블 삭제 
                database.delete_chat_table();
                Toast.makeText(Chatroom.this, "쪽지가 삭제 되었습니다.", Toast.LENGTH_SHORT).show();

                dialogInterface.dismiss();


            }
        });

        alertDialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //아니면 그냥 창 종료.
                dialogInterface.dismiss();

            }
        });

        AlertDialog alert = alertDialog.create();
        alert.setTitle("쪽지 삭제");
        alert.show();

    }
}
