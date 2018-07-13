package com.example.gdtbg.buddy.buddy_02_mainboard_reply;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gdtbg.buddy.R;

/**
 * Created by gdtbg on 2017-11-04.
 */

public class Mainboard_Reply_ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

    public final ImageView mainboard_reply_profile;
    public final TextView mainboard_reply_writer;
    public final TextView mainboard_reply_date;
    public final TextView mainboard_reply_content;
    public final LinearLayout reply_item_write_mode;
    public final LinearLayout reply_item_modify_mode;


    public Mainboard_Reply_ViewHolder(View itemView, int viewType) {
        super(itemView);

        if (viewType == 1) {
            itemView.setOnCreateContextMenuListener(this);
        }
        mainboard_reply_profile = (ImageView) itemView.findViewById(R.id.mainboard_reply_profile);
        mainboard_reply_writer = (TextView) itemView.findViewById(R.id.mainboard_reply_writer);
        mainboard_reply_date = (TextView) itemView.findViewById(R.id.mainboard_reply_date);
        mainboard_reply_content = (TextView) itemView.findViewById(R.id.mainboard_reply_content);
        reply_item_write_mode = (LinearLayout) itemView.findViewById(R.id.mainboard_reply_item_write_mode);
        reply_item_modify_mode = (LinearLayout) itemView.findViewById(R.id.mainboard_reply_item_modify_mode);


    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

        contextMenu.setHeaderTitle("댓글");
        contextMenu.add(0, 1, getAdapterPosition(), "수정");
        contextMenu.add(0, 2, getAdapterPosition(), "삭제");

    }
    // 뷰 홀더에서 컨텍스트 메뉴를 생성해 주는데,
    // contextMenu.add의 파라미터로는 (int groupId, int itemId, int order, int titleRes) 이렇게 4개가 들어간다.
    // 나중에 onContextItemSelected 메소드를 오버라이드하면 등록한 item의 id와 order의 값을 꺼낼 수 있다.
    // 여기서는 order의 값을 어뎁터의 position 값으로 하여서 추후에 어뎁터에 있는 아이템을 제어할 수 있게 하였다.


}
