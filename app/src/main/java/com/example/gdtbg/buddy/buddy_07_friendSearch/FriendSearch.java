package com.example.gdtbg.buddy.buddy_07_friendSearch;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gdtbg.buddy.R;
import com.example.gdtbg.buddy.buddy.CustomActivity;
import com.example.gdtbg.buddy.buddy.LoginInfo;

/**
 * Created by gdtbg on 2017-10-11.
 */

public class FriendSearch extends CustomActivity implements View.OnClickListener {

    EditText inputField;
    Button searchBtn;
    String keyword;
    Bundle bundle;
    Fragment fragment;

    LoginInfo loginInfo;
    String user_uid;
    String user_email;
    String user_nickname;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buddy07_main);
        super.setMenuBar();
        actionbar_title.setText("친구 검색");
        loginInfo = LoginInfo.getInstance(this);
        user_email = loginInfo.getUser_email();
        user_nickname = loginInfo.getUser_nickname();


        keyword = "nullpointer";

        searchBtn = (Button) findViewById(R.id.friend_search_btn);
        inputField = (EditText) findViewById(R.id.friend_search_inputText);

        searchBtn.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.friend_search_btn) {

            keyword = inputField.getText().toString();

            if (keyword.equals(user_email) || keyword.equals(user_nickname)) {

                Toast.makeText(this, "본인의 계정은 검색할 수 없습니다.", Toast.LENGTH_SHORT).show();
                return;

            }

            Bundle bundle = new Bundle();
            bundle.putSerializable("keyword", keyword);
            fragment = new FriendSearch_Fragment();
            fragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.friend_search_fragment, fragment).commit();


        }

    }


    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
