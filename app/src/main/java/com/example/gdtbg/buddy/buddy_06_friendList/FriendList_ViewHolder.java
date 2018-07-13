package com.example.gdtbg.buddy.buddy_06_friendList;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gdtbg.buddy.R;

/**
 * Created by gdtbg on 2017-10-08.
 */

public class FriendList_ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

    public final ImageView friend_profile;
    public final ImageView friend_mailBtn;
    public final TextView friend_name;


    public FriendList_ViewHolder(View itemView) {
        super(itemView);
        itemView.setOnCreateContextMenuListener(this);
        friend_mailBtn = (ImageView) itemView.findViewById(R.id.friend_mail);
        friend_profile = (ImageView) itemView.findViewById(R.id.friend_profile);
        friend_name = (TextView) itemView.findViewById(R.id.friend_name);

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

        contextMenu.setHeaderTitle("친구 목록");
        contextMenu.add(0, 1, getAdapterPosition(), "친구 삭제하기");
        contextMenu.add(0, 2, getAdapterPosition(), "친구 상세 정보 보기");


    }
}
