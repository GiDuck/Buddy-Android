package com.example.gdtbg.buddy.buddy_07_friendSearch;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gdtbg.buddy.R;
import com.example.gdtbg.buddy.VO.ActivityVO;
import com.example.gdtbg.buddy.VO.FriendVO;
import com.example.gdtbg.buddy.VO.MemberVO;
import com.example.gdtbg.buddy.buddy.LoginInfo;

import java.util.ArrayList;

import static com.example.gdtbg.buddy.buddy.Utilities.CurrentLocalTime;
import static com.example.gdtbg.buddy.buddy.Utilities.databaseReference;

/**
 * Created by gdtbg on 2017-10-11.
 */

public class FriendSearch_Adapter extends RecyclerView.Adapter<FriendSearch_ViewHolder> {

    private Context context;
    private ArrayList<MemberVO> items;
    private String user_uid;
    private String user_profile;
    private String user_nickname;
    private LoginInfo loginInfo;

    public FriendSearch_Adapter(Context context, ArrayList<MemberVO> items) {
        this.context = context;
        this.items = items;
        loginInfo = LoginInfo.getInstance(context);

        user_uid = loginInfo.getUser_uid();
        user_profile = loginInfo.getUser_profile();
        user_nickname = loginInfo.getUser_nickname();
    }

    @Override
    public FriendSearch_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.buddy07_custom_view, parent, false);
        FriendSearch_ViewHolder holder = new FriendSearch_ViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(FriendSearch_ViewHolder holder, final int position) {

        try {
            final MemberVO item = items.get(position);
            Glide.with(context).load(item.getProfilePhoto()).apply(new RequestOptions().circleCrop()).into(holder.friend_search_profile);
            holder.friend_search_name.setText(item.getNickname());
            holder.friend_search_email.setText(item.getEmail());

            //친구 요청 버튼
            holder.friend_search_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("기덕", "친구추가 " + user_uid + user_profile + user_nickname + item.getUid());
                    FriendVO newFriend = new FriendVO(user_uid);
                    databaseReference.child("user").child(item.getUid()).child("friendList").child(user_uid).setValue(newFriend);


                    String request = user_nickname + " 님께서 친구 요청을 하였습니다.";
                    ActivityVO activityVO = new ActivityVO();
                    activityVO.setTime(CurrentLocalTime());
                    activityVO.setContent(request);

                    /* newFriend = new FriendVO(item.getUid(), item.getProfilePhoto(), item.getNickname());
                    util.databaseReference.child("user").child(user_uid).child("friendList").child(item.getUid()).setValue(newFriend);*/


                    databaseReference.child("user").child(item.getUid()).child("myActivity").push().setValue(activityVO);
                    String request_to_me = item.getNickname() + " 님께 친구 요청을 하였습니다.";
                    activityVO.setContent(request_to_me);
                    databaseReference.child("user").child(user_uid).child("myActivity").push().setValue(activityVO);

                    Toast.makeText(context, item.getNickname() + " 님께 친구 요청을 전송하였습니다.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(context, FriendSearch.class);
                    context.startActivity(intent);

                }
            });
        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    /*------------------- 친구 목록을 불러오는 메소드 -------------------*/

    public void add(ArrayList<MemberVO> items) {

        this.items = items;
        notifyDataSetChanged();


    }

    public ArrayList<MemberVO> getItems() {
        return items;
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
