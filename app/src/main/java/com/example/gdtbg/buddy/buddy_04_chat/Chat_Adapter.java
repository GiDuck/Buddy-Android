package com.example.gdtbg.buddy.buddy_04_chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gdtbg.buddy.R;
import com.example.gdtbg.buddy.VO.ChatVO;
import com.example.gdtbg.buddy.buddy.LoginInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import static com.example.gdtbg.buddy.buddy.Utilities.databaseReference;
import static com.example.gdtbg.buddy.buddy.Utilities.timeFormat;

/**
 * Created by gdtbg on 2017-10-05.
 */

public class Chat_Adapter extends RecyclerView.Adapter<Chat_ViewHolder> {

    private Context context;
    private ArrayList<ChatVO> items;
    private ChatVO item;
    private String friend_uid;
    private String user_uid;
    private View v;

    private SharedPreferences preferences;

    public Chat_Adapter(Context context, ArrayList<ChatVO> items, String friend_uid) throws Exception {
        this.context = context;
        this.items = items;
        this.friend_uid = friend_uid;
        ;
        user_uid = LoginInfo.getInstance(context).getUser_uid();


    }

    public void add(ChatVO item) throws Exception {

        this.item = item;
        items.add(item);
        notifyDataSetChanged();

    }

    public void listAdd(ArrayList<ChatVO> items) throws Exception {

        this.items = items;
        notifyDataSetChanged();

    }

    public ArrayList<ChatVO> getItems() {
        return items;
    }

    @Override
    public Chat_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {

            if (viewType == 1) {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.buddy04_right_view, parent, false);
            } else if (viewType == 2) {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.buddy04_left_view, parent, false);
            } else {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.buddy04_left_view, parent, false);
            }

            //뷰타입에 따라서 다른 아이템 레이아웃 inflate

            Chat_ViewHolder holder = new Chat_ViewHolder(v, viewType);
            return holder;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public int getItemViewType(int position) {

        try {
            if (items.get(position).getUser_uid().equals(user_uid)) {
                //만약 채팅 item에 있는 user_uid가 현재 로그인된 유저의 uid와 같다면 1을 리턴
                //즉 이 말은 현재 로그인된 유저가 쓴 채팅 내역이다.
                return 1;
            } else { //아니면 2를 리턴 (상대방이 보낸 채팅 내역이다)
                return 2;
            }

        } catch (Exception e) {

            e.printStackTrace();
            return 3;
        }


    }

    @Override
    public void onBindViewHolder(final Chat_ViewHolder holder, int position) {

        try {

            item = items.get(position);
            Date date = new Date(item.getTime());
            if (item.getUser_uid().equals(user_uid)) {

                holder.right_chat_bubble.setText(item.getContent());
                holder.right_chat_time.setText(timeFormat.format(date));

            } else {

                databaseReference.child("user").child(friend_uid).child("profilePhoto")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String profile = (String) dataSnapshot.getValue();
                                Glide.with(context).load(profile).apply(new RequestOptions().circleCrop()).into(holder.left_chat_profile);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                holder.left_chat_bubble.setText(item.getContent());
                holder.left_chat_time.setText(timeFormat.format(item.getTime()));

            }
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public void itemClear() {
        items.clear();
    }

    public int sizeChecker() throws Exception {

        return items.size();
    }

    @Override
    public int getItemCount() {

        try {
            return items.size();

        } catch (Exception e) {

            return 0;
        }
    }


}
