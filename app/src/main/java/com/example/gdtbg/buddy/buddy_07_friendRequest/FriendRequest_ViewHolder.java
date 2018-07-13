package com.example.gdtbg.buddy.buddy_07_friendRequest;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gdtbg.buddy.R;

/**
 * Created by gdtbg on 2017-11-06.
 */

public class FriendRequest_ViewHolder extends RecyclerView.ViewHolder {

    public final ImageView friend_request_profile;
    public final TextView friend_request_name;
    public final Button friend_request_approveBtn;
    public final Button friend_request_denyBtn;


    public FriendRequest_ViewHolder(View itemView) {
        super(itemView);
        friend_request_profile = (ImageView)itemView.findViewById(R.id.friend_request_profile);
        friend_request_name = (TextView)itemView.findViewById(R.id.friend_request_name);
        friend_request_approveBtn = (Button)itemView.findViewById(R.id.friend_request_approveBtn);
        friend_request_denyBtn = (Button)itemView.findViewById(R.id.friend_request_denyBtn);


    }

}
