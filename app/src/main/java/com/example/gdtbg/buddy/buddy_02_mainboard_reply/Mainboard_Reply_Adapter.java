package com.example.gdtbg.buddy.buddy_02_mainboard_reply;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gdtbg.buddy.R;
import com.example.gdtbg.buddy.VO.ReplyVO;
import com.example.gdtbg.buddy.buddy.LoginInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import static com.example.gdtbg.buddy.buddy.Utilities.databaseReference;
import static com.example.gdtbg.buddy.buddy.Utilities.dateFormat;
import static com.example.gdtbg.buddy.buddy.Utilities.timeFormat;

/**
 * Created by gdtbg on 2017-11-04.
 */

public class Mainboard_Reply_Adapter extends RecyclerView.Adapter<Mainboard_Reply_ViewHolder> {

    private Context context;
    private ArrayList<ReplyVO> items;
    private ArrayList<View> holdViews; // 바인딩 되는 item view 들을 담는 리스트
    private String user_uid;
    int position;

    public Mainboard_Reply_Adapter(Context context, ArrayList<ReplyVO> items) {
        this.context = context;
        this.items = items;
        user_uid = LoginInfo.getInstance(context).getUser_uid();
        holdViews = new ArrayList<>();
    }

    public void add(ArrayList<ReplyVO> items) {

        this.items = items;
        notifyDataSetChanged();


    }

    @Override
    public int getItemViewType(int position) {

        try {
            if (items.get(position).getReply_writer_uid().equals(user_uid)) {

                return 1; // 글쓴이 == 로그인 한 유저

            } else {

                return 0; // 글쓴이와 로그인한 유저가 다름
            }
        } catch (Exception e) {
            e.printStackTrace();

            return 0; //에러 상황 (Exception)
        }

    }

    @Override
    public Mainboard_Reply_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.buddy02_reply_item, parent, false);
        Mainboard_Reply_ViewHolder holder = new Mainboard_Reply_ViewHolder(v, viewType);
        holdViews.add(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(final Mainboard_Reply_ViewHolder holder, int position) {

        ReplyVO item = items.get(position);
        Date date = new Date(item.getReply_time());


        databaseReference.child("user").child(item.getReply_writer_uid()).child("profilePhoto").
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String profile = (String) dataSnapshot.getValue();
                        Glide.with(context).load(profile).apply(new RequestOptions().circleCrop()).into(holder.mainboard_reply_profile);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        databaseReference.child("user").child(item.getReply_writer_uid()).child("nickname")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String nickname = (String) dataSnapshot.getValue();
                        holder.mainboard_reply_writer.setText(nickname);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        holder.mainboard_reply_date.setText(dateFormat.format(date) + "\t\t" + timeFormat.format(date));
        holder.mainboard_reply_content.setText(item.getReply_content());
        holder.reply_item_modify_mode.setVisibility(View.GONE); //수정 시 나타나는 레이아웃
        holder.reply_item_write_mode.setVisibility(View.VISIBLE); //작성 시 나타나는 레이아웃 (기본)


    }

    @Override
    public void onViewRecycled(Mainboard_Reply_ViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ArrayList<ReplyVO> getItems() {
        return items;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ArrayList<View> getHoldViews() {
        return holdViews;
    }

    public void setHoldViews(ArrayList<View> holdViews) {
        this.holdViews = holdViews;
    }
}
