package com.example.gdtbg.buddy.buddy_06_friendList;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.gdtbg.buddy.R;
import com.example.gdtbg.buddy.VO.FriendVO;
import com.example.gdtbg.buddy.buddy.CustomActivity;
import com.example.gdtbg.buddy.buddy.ErrorPageInflater;
import com.example.gdtbg.buddy.buddy.LoginInfo;
import com.example.gdtbg.buddy.buddy_07_friendRequest.FriendRequest_Main;
import com.example.gdtbg.buddy.buddy_07_friendSearch.FriendSearch;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.gdtbg.buddy.buddy.Utilities.databaseReference;

public class FriendList extends CustomActivity implements ValueEventListener, View.OnClickListener {

    private RecyclerView rv;
    private LinearLayoutManager lim;
    private FriendList_Adapter adapter;
    private ArrayList<FriendVO> items;
    private RelativeLayout root;
    private String user_uid = null;

    FloatingActionButton addBtn;
    FloatingActionButton friend_request_check_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buddy06_main);
        super.setMenuBar();
        actionbar_title.setText("친구목록");

        user_uid = LoginInfo.getInstance(this).getUser_uid();


        adapter = new FriendList_Adapter(this, items);

        root = (RelativeLayout) findViewById(R.id.root06);
        databaseReference.child("user")
                .child(user_uid)
                .child("friendList")
                .addValueEventListener(this);
        ;

        items = new ArrayList<>();
        rv = (RecyclerView) findViewById(R.id.friend_recyclerview);
        addBtn = (FloatingActionButton) findViewById(R.id.friend_add_btn);
        friend_request_check_btn = (FloatingActionButton) findViewById(R.id.friend_request_check_btn);

        addBtn.setOnClickListener(this);
        friend_request_check_btn.setOnClickListener(this);
        lim = new LinearLayoutManager(this);

        rv.setLayoutManager(lim);
        rv.setAdapter(adapter);


    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        for (DataSnapshot snap : dataSnapshot.getChildren()) {

            Log.d("기덕", "프렌드 리스트 " + snap.toString());
            FriendVO friend = snap.getValue(FriendVO.class);

            if (friend.getVerified()) {
                items.add(friend);
            }


        }

        if (items == null || items.size() == 0) {

            new ErrorPageInflater(FriendList.this).setErrorPage(root, rv, "친구목록이 없습니다. 친구를 추가해 보세요!");

        } else {
            adapter.add(items);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        new ErrorPageInflater(FriendList.this).setErrorPage(root, rv, "친구목록을 불러오는 중에 오류가 발생하였습니다.");

    }


    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.friend_add_btn) {

            Intent intent = new Intent(FriendList.this, FriendSearch.class);
            startActivity(intent);


        } else if (id == R.id.friend_request_check_btn) {

            Intent intent = new Intent(FriendList.this, FriendRequest_Main.class);
            startActivity(intent);

        }

    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {

        final int nPosition = item.getOrder();

        switch (item.getItemId()) {

            case 1: //친구 삭제
                deleteFriend(nPosition);
                break;

            case 2: //친구 정보 상세보기

                break;
        }


        return super.onContextItemSelected(item);
    }

    /*------------------- 친구를 목록에서 삭제 하는 메소드 -------------------*/


    public void deleteFriend(final int nPosition) {

        //다이얼로그 생성
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("친구를 목록에서 삭제 하시겠습니까? 삭제된 데이터는 복구되지 않습니다.");

        alertDialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                //"네" 클릭시 친구 목록에서 삭제

                databaseReference.child("user").child(user_uid).child("friendList").child(items.get(nPosition).getFriend_uid()).removeValue();
                databaseReference.child("user").child(items.get(nPosition).getFriend_uid()).child("friendList").child(user_uid).removeValue();

                Toast.makeText(FriendList.this, "친구를 삭제하였습니다.", Toast.LENGTH_SHORT).show();

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
        alert.setTitle("친구 목록");
        alert.show();

    }


}
