package com.example.gdtbg.buddy.buddy_07_friendSearch;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gdtbg.buddy.R;
import com.example.gdtbg.buddy.VO.MemberVO;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.gdtbg.buddy.buddy.Utilities.databaseReference;


/**
 * Created by gdtbg on 2017-10-11.
 */

public class FriendSearch_Fragment extends Fragment {

    String keyword;
    RecyclerView rv;
    FriendSearch_Adapter adapter;
    LinearLayoutManager lim;
    ArrayList<MemberVO> items;

    public FriendSearch_Fragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.buddy07_fragment_main, container, false);
        items = new ArrayList<>();
        keyword = "null*";

        if (getArguments() != null) {
            keyword = getArguments().getString("keyword");
            Log.d("기덕", "keyword " + keyword);
        }

        rv = (RecyclerView) rootView.findViewById(R.id.friend_search_fragment_recyclerview);
        adapter = new FriendSearch_Adapter(rootView.getContext(), items);
        lim = new LinearLayoutManager(rootView.getContext());
        rv.setLayoutManager(lim);
        rv.setAdapter(adapter);

        //닉네임과 이메일로 사용자를 검색함

        databaseReference.child("user")
                .orderByChild("nickname")
                .equalTo(keyword)
                .addListenerForSingleValueEvent(new ValueEventListener() {


                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (items.size() != 0) {
                            items.clear();
                        }

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            items.add(snapshot.getValue(MemberVO.class));

                        }

                        //첫번째로 닉네임으로 검색했는데 데이터가 아무것도 없을 때,
                        if (items.size() == 0) {


                            //이메일로 다시 한 번 검색
                            databaseReference.child("user")
                                    .orderByChild("email")
                                    .equalTo(keyword)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {


                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                items.add(snapshot.getValue(MemberVO.class));
                                            }
                                            adapter.add(items);


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                        } else {


                            adapter.add(items);

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        return rootView;

    }


}
