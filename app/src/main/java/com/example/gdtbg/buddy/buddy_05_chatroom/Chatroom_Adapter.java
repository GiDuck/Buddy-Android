package com.example.gdtbg.buddy.buddy_05_chatroom;

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
import com.example.gdtbg.buddy.buddy_04_chat.Chat;
import com.example.gdtbg.buddy.buddy.interfaces.ItemOnclickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.gdtbg.buddy.buddy.Utilities.databaseReference;

/**
 * Created by gdtbg on 2017-10-04.
 */

public class Chatroom_Adapter extends RecyclerView.Adapter<Chatroom_ViewHolder> implements ItemOnclickListener {


    private Context context;
    private ArrayList<ChatroomVO> items;
    private ArrayList<String> chatroomUids;
    private ChatroomVO item;


    public Chatroom_Adapter(Context context, ArrayList<ChatroomVO> items, ArrayList<String> chatroomUids) {
        this.context = context;
        this.items = items;
        this.chatroomUids = chatroomUids;


    }

    public void add(ArrayList<ChatroomVO> item) {

        items = item;
        notifyDataSetChanged();

    }


    @Override
    public Chatroom_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.buddy05_custom_view, parent, false);
        Chatroom_ViewHolder holder = new Chatroom_ViewHolder(v);


        return holder;
    }

    @Override
    public void onViewRecycled(Chatroom_ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.itemView.setOnLongClickListener(null);


    }

    @Override
    public void onBindViewHolder(final Chatroom_ViewHolder holder, final int position) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("lastChat");


        item = items.get(position);

        databaseReference.child("user").child(item.getFriend_uid()).child("nickname")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String nickname = (String) dataSnapshot.getValue();
                        holder.chatroom_name.setText(nickname);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        databaseReference.child("user").child(item.getFriend_uid()).child("profilePhoto")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String profile = (String) dataSnapshot.getValue();
                        Glide.with(context).load(profile).apply(new RequestOptions().circleCrop()).into(holder.chatroom_profile);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        myRef.child(chatroomUids.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                holder.chatroom_content.setText(dataSnapshot.child("lc_content").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("기덕", "채팅룸 어뎁터 에러 발생");
            }
        });

        holder.setItemOnclickListener(this);


    }

    @Override
    public void onListItemClick(int position) {
        String friend_uid = items.get(position).getFriend_uid();
        Intent intent = new Intent(context, Chat.class);
        intent.putExtra("chatroom_uid", chatroomUids.get(position));
        intent.putExtra("friend_uid", friend_uid);
        Log.d("기덕", "Chatroom_Adapter, friend uid : " + friend_uid );
        context.startActivity(intent);

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public ArrayList<ChatroomVO> getItems() {
        return items;
    }
}
