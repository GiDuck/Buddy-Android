package com.example.gdtbg.buddy.buddy_09_myActivityLog;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.gdtbg.buddy.R;

/**
 * Created by gdtbg on 2017-11-22.
 */

public class MyActivity_ViewHolder extends RecyclerView.ViewHolder {

    TextView myActivty_time;
    TextView myActivty_date;
    TextView myActivty_content;

    public MyActivity_ViewHolder(View itemView) {
        super(itemView);

        myActivty_time = (TextView)itemView.findViewById(R.id.activity_log_time);
        myActivty_date = (TextView)itemView.findViewById(R.id.activity_log_date);
        myActivty_content = (TextView)itemView.findViewById(R.id.activity_log_content);


    }
}
