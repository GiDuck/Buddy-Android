package com.example.gdtbg.buddy.buddy_09_myActivityLog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gdtbg.buddy.R;
import com.example.gdtbg.buddy.VO.ActivityVO;

import java.util.ArrayList;
import java.util.Date;

import static com.example.gdtbg.buddy.buddy.Utilities.dateFormat;
import static com.example.gdtbg.buddy.buddy.Utilities.timeFormat;

/**
 * Created by gdtbg on 2017-11-22.
 */

public class MyActivity_Adapter extends RecyclerView.Adapter<MyActivity_ViewHolder> {

    private Context context;
    private ArrayList<ActivityVO> items;

    public MyActivity_Adapter(Context context) {
        this.context = context;
        items = new ArrayList<>();
    }

    public void setData(ArrayList<ActivityVO> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public MyActivity_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = (LayoutInflater.from(parent.getContext()).inflate(R.layout.buddy09_item_view, parent, false));
        MyActivity_ViewHolder holder = new MyActivity_ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyActivity_ViewHolder holder, int position) {

        ActivityVO item = items.get(position);
        Long time = Long.valueOf(item.getTime());
        Date date = new Date(time);
        holder.myActivty_date.setText(dateFormat.format(date));
        holder.myActivty_time.setText(timeFormat.format(date));
        holder.myActivty_content.setText(item.getContent());



    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
