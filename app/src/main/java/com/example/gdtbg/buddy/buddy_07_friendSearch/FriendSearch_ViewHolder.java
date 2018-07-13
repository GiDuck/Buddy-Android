package com.example.gdtbg.buddy.buddy_07_friendSearch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gdtbg.buddy.R;

/**
 * Created by gdtbg on 2017-10-11.
 */

public class FriendSearch_ViewHolder extends RecyclerView.ViewHolder {

    public final ImageView friend_search_profile;
    public final TextView friend_search_name;
    public final TextView friend_search_email;
    public final ImageView friend_search_add; //친구 요청 버튼



    public FriendSearch_ViewHolder(View itemView) {
        super(itemView);

        friend_search_add = (ImageView)itemView.findViewById(R.id.friend_search_add);
        friend_search_name = (TextView)itemView.findViewById(R.id.friend_search_name);
        friend_search_profile = (ImageView)itemView.findViewById(R.id.friend_search_profile);
        friend_search_email = (TextView)itemView.findViewById(R.id.friend_search_email);

    }
}
