package com.example.gdtbg.buddy.buddy_02_mainboard_reply;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gdtbg.buddy.R;
import com.example.gdtbg.buddy.VO.ReplyVO;

import java.util.ArrayList;

/**
 * 댓글창 프레그먼트
 */

public class Mainboard_Reply_Fragment extends Fragment {

    RecyclerView rv;
    Mainboard_Reply_Adapter adapter;
    LinearLayoutManager lim;
    ArrayList<ReplyVO> items;
    String board_key; //참조하는 게시글 key
    View rootView;


    public Mainboard_Reply_Fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.buddy02_reply_fragment, container, false);

        if (getArguments() != null) {
            items = new ArrayList<>();
            board_key = this.getArguments().getString("board_key");
            items = (ArrayList<ReplyVO>) this.getArguments().getSerializable("reply_list");
            rv = (RecyclerView) rootView.findViewById(R.id.reply_fragment_recyclerview);

            adapter = new Mainboard_Reply_Adapter(rootView.getContext(), items);
            lim = new LinearLayoutManager(rootView.getContext());
            rv.setLayoutManager(lim);
            rv.setAdapter(adapter);
            adapter.add(items);
            registerForContextMenu(rv); //컨텍스트 메뉴를 리사이클러뷰에 등록한다.


        }


        return rootView;
    }

    public ArrayList<ReplyVO> getReplyList() {
        return adapter.getItems();
    }

    public Mainboard_Reply_Adapter getAdapter() {
        return adapter;
    }


}
