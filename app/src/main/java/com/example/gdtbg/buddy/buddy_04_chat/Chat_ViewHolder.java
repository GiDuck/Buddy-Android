package com.example.gdtbg.buddy.buddy_04_chat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gdtbg.buddy.R;

/**
 * Created by gdtbg on 2017-10-05.
 */

public class Chat_ViewHolder extends RecyclerView.ViewHolder {

    //왼쪽 버블 layout

    public ImageView left_chat_profile;
    public TextView left_chat_bubble;
    public TextView left_chat_time;

    // 오른쪽 버블 layout

    public TextView right_chat_bubble;
    public TextView right_chat_time;


    public Chat_ViewHolder(View itemView, int viewType) {
        super(itemView);

        if(viewType==2) {
            left_chat_profile = (ImageView) itemView.findViewById(R.id.left_chat_profile);
            left_chat_bubble = (TextView) itemView.findViewById(R.id.left_chat_bubble);
            left_chat_time = (TextView) itemView.findViewById(R.id.left_chat_time);
        }else if(viewType==1){
            right_chat_bubble = (TextView) itemView.findViewById(R.id.right_chat_bubble);
            right_chat_time = (TextView) itemView.findViewById(R.id.right_chat_time);

        }
    }
}
