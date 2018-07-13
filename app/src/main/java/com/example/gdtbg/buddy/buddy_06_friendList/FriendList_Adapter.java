package com.example.gdtbg.buddy.buddy_06_friendList;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gdtbg.buddy.R;
import com.example.gdtbg.buddy.VO.ChatroomVO;
import com.example.gdtbg.buddy.VO.FriendVO;
import com.example.gdtbg.buddy.buddy.LoginInfo;
import com.example.gdtbg.buddy.buddy_04_chat.Chat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import static com.example.gdtbg.buddy.buddy.Utilities.databaseReference;

/**
 * Created by gdtbg on 2017-10-08.
 */

public class FriendList_Adapter extends RecyclerView.Adapter<FriendList_ViewHolder> {

    private final Context context;
    private ArrayList<FriendVO> items;
    String key;
    LoginInfo loginInfo;
    String user_uid;
    String user_nickname;
    String user_profile;

    public FriendList_Adapter(Context context, ArrayList<FriendVO> items) {
        this.context = context;
        this.items = items;
        loginInfo = LoginInfo.getInstance(context);
        user_uid = loginInfo.getUser_uid();
        user_nickname = loginInfo.getUser_nickname();
        user_profile = loginInfo.getUser_profile();
    }

    public void add(ArrayList<FriendVO> items) {

        this.items = items;
        notifyDataSetChanged();

    }

    @Override
    public FriendList_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.buddy06_custom_view, parent, false);
        FriendList_ViewHolder holder = new FriendList_ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final FriendList_ViewHolder holder, final int position) {
        try {
            final FriendVO item = items.get(position);

            databaseReference.child("user").child(item.getFriend_uid()).child("profilePhoto")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String profile = (String)dataSnapshot.getValue();
                            Glide.with(context).load(profile).apply(new RequestOptions().circleCrop()).into(holder.friend_profile);

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
                            holder.friend_name.setText(nickname);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

            holder.friend_mailBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    //친구목록 -> 채팅 하기 버튼을 클릭하면

                    //user가 가지고 있는 chatroomList 중에서 친구의 uid를 가진게 있는지 확인한다 (중복 방지를 위해)
                    databaseReference.child("user")
                            .child(user_uid)
                            .child("chatroomList")
                            .orderByChild("friend_uid")
                            .equalTo(item.getFriend_uid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {


                                    if (dataSnapshot.getChildrenCount() == 0) { //만약 없으면

                                        key = databaseReference.push().getKey(); //새로운 채팅방의 uid 키를 만들어 주고

                                        ChatroomVO chatroomVO = new ChatroomVO();
                                        chatroomVO.setFriend_uid(item.getFriend_uid());


                                        databaseReference.child("user").child(user_uid).child("chatroomList").child(key).setValue(chatroomVO);
                                        //내 chatroomList에 새로운 채팅방 uid 추가

                                        chatroomVO = new ChatroomVO();
                                        chatroomVO.setFriend_uid(user_uid);

                                        databaseReference.child("user").child(item.getFriend_uid()).child("chatroomList").child(key).setValue(chatroomVO);
                                        //친구의 chatroomList에 새로운 채팅방 uid 추가




                                    } else {
                                        //만약 기존에 친구랑 대화했던 채팅방이 존재 한다면

                                        HashMap<String, ChatroomVO> data = new HashMap<String, ChatroomVO>();
                                        data = (HashMap<String, ChatroomVO>) dataSnapshot.getValue();
                                        // 그 데이터를 그대로 들고와서

                                        Set getkey = data.keySet();
                                        for (Iterator iterator = getkey.iterator(); iterator.hasNext(); ) {

                                            key = (String) iterator.next();

                                        }

                                        //키를 뽑아준다.


                                    }
                                    Log.d("채팅방", "가져온 키 " + key);

                                    //그리고 채팅 액티비티로 전환
                                    Intent intent = new Intent(context, Chat.class);
                                    intent.putExtra("chatroom_uid", key);
                                    intent.putExtra("friend_uid", item.getFriend_uid());
                                    context.startActivity(intent);

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                }
            });
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {

        try {
            return items.size();
        } catch (Exception e) {

            e.printStackTrace();
            return 0;
        }
    }


}
