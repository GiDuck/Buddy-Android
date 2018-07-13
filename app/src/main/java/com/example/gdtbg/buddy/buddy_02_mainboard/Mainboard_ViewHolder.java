package com.example.gdtbg.buddy.buddy_02_mainboard;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gdtbg.buddy.R;
import com.example.gdtbg.buddy.buddy.interfaces.ItemOnclickListener;
import com.like.LikeButton;

/**
 * Created by gdtbg on 2017-10-30.
 */

public class Mainboard_ViewHolder extends RecyclerView.ViewHolder {

    public final ImageView profile_photo;
    public final TextView content;
    public final TextView nickname;
    public final ImageView flag1;
    public final ImageView flag2;
    public final TextView heartNum;
    public final TextView commentNum;

    public final LinearLayout mainboard_one_ptoto;
    public final LinearLayout mainboard_multiple_photo;

    public final ImageView mainboard_one_ptoto_view;
    public final ImageView mainboard_multiple_ptoto_view1;
    public final ImageView mainboard_multiple_ptoto_view2;

    public final TextView mainboard_more_image;


    public final LikeButton likeButton;

    public final LinearLayout mainboard_item_photo_area;

    public ItemOnclickListener itemOnclickListener;

    public Mainboard_ViewHolder(View itemView) {
        super(itemView);
        profile_photo = (ImageView) itemView.findViewById(R.id.profile_photo);
        content = (TextView) itemView.findViewById(R.id.textv);
        flag1 = (ImageView) itemView.findViewById(R.id.flag);
        flag2 = (ImageView) itemView.findViewById(R.id.flag2);
        heartNum = (TextView) itemView.findViewById(R.id.heartNum);
        commentNum = (TextView) itemView.findViewById(R.id.commentNum);
        nickname = (TextView) itemView.findViewById(R.id.name);
        likeButton=(LikeButton)itemView.findViewById(R.id.likeButton);
        mainboard_item_photo_area = (LinearLayout)itemView.findViewById(R.id.mainboard_item_photo_area);
        mainboard_one_ptoto = (LinearLayout)itemView.findViewById(R.id.mainboard_one_ptoto);
        mainboard_multiple_photo = (LinearLayout)itemView.findViewById(R.id.mainboard_multiple_ptoto);
        mainboard_one_ptoto_view = (ImageView)itemView.findViewById(R.id.mainboard_one_ptoto_view);
        mainboard_multiple_ptoto_view1= (ImageView)itemView.findViewById(R.id.mainboard_multiple_ptoto_view1);
        mainboard_multiple_ptoto_view2= (ImageView)itemView.findViewById(R.id.mainboard_multiple_ptoto_view2);
        mainboard_more_image = (TextView)itemView.findViewById(R.id.mainboard_more_image);



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
}