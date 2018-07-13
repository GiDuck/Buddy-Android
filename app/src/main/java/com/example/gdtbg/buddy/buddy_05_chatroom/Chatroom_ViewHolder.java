package com.example.gdtbg.buddy.buddy_05_chatroom;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gdtbg.buddy.R;
import com.example.gdtbg.buddy.buddy.interfaces.ItemOnclickListener;

/**
 * Created by gdtbg on 2017-10-04.
 */

public class Chatroom_ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

    public final ImageView chatroom_profile;
    public final ImageView chatroom_button;
    public final TextView chatroom_content;
    public final TextView chatroom_name;

    public ItemOnclickListener itemOnclickListener;


    public Chatroom_ViewHolder(View itemView) {
        super(itemView);

        itemView.setOnCreateContextMenuListener(this);
        chatroom_profile = (ImageView)itemView.findViewById(R.id.chatroom_profile);
        chatroom_button = (ImageView)itemView.findViewById(R.id.chatroom_button);
        chatroom_content=(TextView)itemView.findViewById(R.id.chatroom_content);
        chatroom_name=(TextView)itemView.findViewById(R.id.chatroom_name);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemOnclickListener.onListItemClick(getAdapterPosition());
            }
        });


    }

    public void setItemOnclickListener(ItemOnclickListener itemOnclickListener) {
        this.itemOnclickListener = itemOnclickListener;
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

        contextMenu.setHeaderTitle("쪽지함");
        contextMenu.add(0, 1, getAdapterPosition(), "삭제하기");

    }
}
