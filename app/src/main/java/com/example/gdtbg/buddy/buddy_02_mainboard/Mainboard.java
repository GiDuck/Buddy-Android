package com.example.gdtbg.buddy.buddy_02_mainboard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gdtbg.buddy.R;
import com.example.gdtbg.buddy.VO.MainboardVO;
import com.example.gdtbg.buddy.buddy.CustomActivity;
import com.example.gdtbg.buddy.buddy.ErrorPageInflater;
import com.example.gdtbg.buddy.buddy.LoginInfo;
import com.example.gdtbg.buddy.buddy.Permission;
import com.example.gdtbg.buddy.buddy.Utilities;
import com.example.gdtbg.buddy.buddy_02_mainboard.buddy_02_mainboard_CRUD.Mainboard_Write_Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.gdtbg.buddy.buddy.Utilities.databaseReference;

/**
 * 자유 게시판 액티비티
 */

public class Mainboard extends CustomActivity implements View.OnClickListener{

    private LoginInfo loginInfo;

    //글쓰기 플로팅 버튼
    private FloatingActionButton actionButton;
    //리사이클러뷰
    private RecyclerView rv;
    //자유게시판 아이템을 받아와 저장하는 리스트
    private ArrayList<MainboardVO> itemlist;
    //자유 게시글의 uid를 저장 하는 리스트
    private ArrayList<String> uidList;

    //리사이클러뷰 사용을 위한 어뎁터와 리니어 레이아웃 매니저
    private Mainboard_Adapter adapter;
    private LinearLayoutManager manager;

    Utilities util;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buddy02_main); //본 액티비티의 레이아웃을 먼저 inflate 시킨 후,
        super.setMenuBar(); //Custom Activity의 setMenuBar 메소드를 호출 해 툴바와 액션바를 부착시킨다.

        //너무 빨리 로딩 끝나서 돌아가는거 딜레이 줬어염
        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBoardList();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                },800);

            }
        });

        loginInfo = LoginInfo.getInstance(this);

        actionbar_title.setText("친구 찾기"); //액션바에 이름 세팅

        util = new Utilities(this);

        Log.d("기덕", loginInfo.getUser_uid());
        Log.d("기덕", loginInfo.getUser_profile());
        Log.d("기덕", loginInfo.getUser_nickname());


        Permission permission = new Permission(this);
        permission.checkPermission();

        itemlist = new ArrayList<MainboardVO>();
        uidList = new ArrayList<>();
        rv = (RecyclerView) findViewById(R.id.recycler);
        adapter = new Mainboard_Adapter(this);
        manager = new LinearLayoutManager(Mainboard.this);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        rv.setLayoutManager(manager);


        getBoardList();

    }

    private void getBoardList() {
        // 게시판 노드에 있는 모든 게시글을 가지고 온다.
        databaseReference.child("board").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MainboardVO item;

                //만약 받아온 노드의 자식 수 ( 게시글 수 ) 가 0개 라면 게시글이 한 개도 없으므로 에러 페이지 표시.
                if (dataSnapshot.getChildrenCount() == 0) {

                    RelativeLayout root = (RelativeLayout) findViewById(R.id.root);
                    new ErrorPageInflater(Mainboard.this).setErrorPage(root, rv, "게시글이 존재하지 않습니다.");
                    return;

                }

                //아이템 리스트가 이전에 사용해서 들어 있는 상태라면, 다시 데이터를 받기 위해 리스트 내용 삭제.
                if (itemlist != null) {
                    itemlist.clear();

                }

                if (uidList != null) {
                    uidList.clear();
                }


                //게시글 받아오기
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    item = snapshot.getValue(MainboardVO.class);
                    String uidKey = snapshot.getKey();
                    itemlist.add(item);
                    uidList.add(uidKey);

                    if (item.getBoard_photos() != null && item.getBoard_photos().size() != 0) {
                        Log.d("기덕", "사진이 있습니다");
                    }

                }

                //어뎁터에 받아온 데이터 전달
                adapter.setData(itemlist, uidList);
                //리사이클러뷰에 어뎁터 부착
                rv.setAdapter(adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


                Toast.makeText(Mainboard.this, "게시글을 불러오지 못하였습니다.", Toast.LENGTH_SHORT).show();
                return;

            }
        });


        rv.setLayoutManager(manager);
        rv.setItemAnimator(new DefaultItemAnimator());

        actionButton = (FloatingActionButton) findViewById(R.id.writebtn);
        actionButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

        int id = view.getId();

        //플로팅 버튼 클릭시 글쓰기 액티비티로 이동동
        if (id == R.id.writebtn) {

            Intent intent = new Intent(Mainboard.this, Mainboard_Write_Post.class);
            startActivity(intent);
        }

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.get(this).trimMemory(level);
    }


}