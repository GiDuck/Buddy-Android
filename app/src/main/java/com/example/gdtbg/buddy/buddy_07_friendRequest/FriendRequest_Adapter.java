package com.example.gdtbg.buddy.buddy_07_friendRequest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gdtbg.buddy.R;
import com.example.gdtbg.buddy.VO.ActivityVO;
import com.example.gdtbg.buddy.VO.FriendVO;
import com.example.gdtbg.buddy.buddy.LoginInfo;
import com.example.gdtbg.buddy.buddy_06_friendList.FriendList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.gdtbg.buddy.buddy.Utilities.CurrentLocalTime;
import static com.example.gdtbg.buddy.buddy.Utilities.databaseReference;

/**
 * Created by gdtbg on 2017-11-06.
 */

public class FriendRequest_Adapter extends RecyclerView.Adapter<FriendRequest_ViewHolder> {

    Context context;
    ArrayList<FriendVO> items;

    private String user_uid;
    private String user_profile;
    private String user_nickname;

    private LoginInfo loginInfo;

    String friend_name;

    public FriendRequest_Adapter(Context context, ArrayList<FriendVO> items) {
        this.context = context;
        this.items = items;
        loginInfo = LoginInfo.getInstance(context);

        user_uid = loginInfo.getUser_uid();
        user_profile = loginInfo.getUser_profile();
        user_nickname = loginInfo.getUser_nickname();

    }

    @Override
    public FriendRequest_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.buddy07_friend_request_item_view, parent, false);
        FriendRequest_ViewHolder holder = new FriendRequest_ViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(final FriendRequest_ViewHolder holder, final int position) {

        try {
            final FriendVO item = items.get(position);

            databaseReference.child("user").child(item.getFriend_uid()).child("profilePhoto")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String profile = (String)dataSnapshot.getValue();
                            Glide.with(context).load(profile).apply(new RequestOptions().circleCrop()).into(holder.friend_request_profile);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

            databaseReference.child("user").child(item.getFriend_uid()).child("nickname")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String nickname = (String)dataSnapshot.getValue();
                            holder.friend_request_name.setText(nickname);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


            //친구 요청 승인
            holder.friend_request_approveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    approve(position);


                }
            });


            holder.friend_request_denyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    denyRequest(position);


                }
            });
        } catch (Exception e) {

            e.printStackTrace();
        }

    }




    /*------------------- 친구 요청을 승인 하는 메소드 -------------------*/

    public void approve(final int nPosition) {

        FriendVO friend = items.get(nPosition);
        //친구 인증 상태를 false에서 true로 바꿔줌으로써 친구 목록에 나타나도록 함.

        FriendVO newFriend = new FriendVO(user_uid);
        newFriend.setVerified(true);
        ActivityVO activityVO = new ActivityVO();


        databaseReference.child("user").child(friend.getFriend_uid()).child("nickname")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        friend_name = (String)dataSnapshot.getValue();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



        databaseReference.child("user").child(friend.getFriend_uid()).child("friendList").child(user_uid).setValue(newFriend);

        databaseReference.child("user").child(user_uid).child("friendList").child(friend.getFriend_uid()).child("verified").setValue(true);
        //사용자와 사용자가 요청한 친구 모두 데이터를 넣어준다.

        String approve = user_nickname + " 님께서 친구 요청을 수락 하셨습니다.";
        activityVO.setTime(CurrentLocalTime());
        activityVO.setContent(approve);
        databaseReference.child("user").child(friend.getFriend_uid()).child("myActivity").push().setValue(activityVO);

        String approve_complete = user_nickname + " 님과 " + friend_name + "님은 이제 친구입니다!";
        activityVO.setContent(approve_complete);
        databaseReference.child("user").child(friend.getFriend_uid()).child("myActivity").push().setValue(activityVO);
        databaseReference.child("user").child(user_uid).child("myActivity").push().setValue(activityVO);

        Toast.makeText(context, user_nickname + " 님과 " + friend_name + "님은 이제 친구입니다!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(context, FriendList.class);
        context.startActivity(intent);

    }




    /*------------------- 친구 요청을 거부 하는 메소드 -------------------*/


    public void denyRequest(final int nPosition) {


        //다이얼로그 생성
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setMessage("친구 요청을 거부하시겠어요?");

        alertDialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                //"네" 클릭시 친구 요청 삭제

                databaseReference.child("user").child(user_uid).child("friendList").child(items.get(nPosition).getFriend_uid()).removeValue();
                databaseReference.child("user").child(items.get(nPosition).getFriend_uid()).child("friendList").child(user_uid).removeValue();

                Toast.makeText(context, "친구 요청을 거부하였습니다.", Toast.LENGTH_SHORT).show();

                dialogInterface.dismiss();

                Intent intent = new Intent(context, FriendList.class);
                context.startActivity(intent);


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
        alert.setTitle("친구 요청 거부하기");
        alert.show();


    }

    /*------------------- 어뎁터 리스트 초기화 -------------------*/

    public void add(ArrayList<FriendVO> items) {


        this.items = items;
        notifyDataSetChanged();

    }

    /*------------------- 어뎁터의 현재 사이즈를 알려 주는 메소드 -------------------*/

    @Override
    public int getItemCount() {
        try {
            return items.size();
        } catch (Exception e) {
            return 0;
        }
    }

}
