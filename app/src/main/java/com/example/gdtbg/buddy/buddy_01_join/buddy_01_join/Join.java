package com.example.gdtbg.buddy.buddy_01_join.buddy_01_join;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gdtbg.buddy.R;
import com.example.gdtbg.buddy.VO.MemberVO;
import com.example.gdtbg.buddy.buddy.LoginInfo;
import com.example.gdtbg.buddy.buddy.Services.ReciverManager;
import com.example.gdtbg.buddy.buddy_02_mainboard.Mainboard;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.gdtbg.buddy.buddy.Utilities.CheckPickedLanguage;
import static com.example.gdtbg.buddy.buddy.Utilities.databaseReference;

public class Join extends AppCompatActivity implements View.OnClickListener {


    FirebaseDatabase database;
    DatabaseReference myRef;

    Intent intent;
    String type = null; //로그인 유형
    String uid = null; //uid
    String useLng, likeLng; //useLng = 모국어, likeLng = 선호언어
    String thumb = null; //썸네일 주소
    Spinner mainLng, learnLng; //모국어, 사용언어(선호언어)
    Long joindate = null; //가입날짜
    String nickname = null; //닉네임
    Button next; //다음 페이지로 넘어가는 버튼
    String email = null;
    String dummy = "dummy";

    ImageView join_profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);


        intent = getIntent();
        type = intent.getStringExtra("login_type");
        uid = intent.getStringExtra("user_uid");
        thumb = intent.getStringExtra("user_profile");
        email = intent.getStringExtra("user_email");


        join_profile = (ImageView) findViewById(R.id.join_profile);
        Glide.with(this).load(thumb).apply(new RequestOptions().circleCrop())
                .apply(new RequestOptions().error(R.drawable.ic_user))
                .into(join_profile);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        mainLng = (Spinner) findViewById(R.id.mainLng);
        mainLng.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                useLng = CheckPickedLanguage(position);
                Log.e("useLng : ", Join.this.useLng);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        learnLng = (Spinner) findViewById(R.id.learnLng);
        learnLng.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                likeLng = CheckPickedLanguage(position);
                Log.e("likeLng : ", Join.this.likeLng);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*
        버튼을 클릭하면 DB에 회원정보를 저장하고 액티비티 전환
        필요한 정보
        1. uid
        2. 가입날짜
        3. 닉네임
        4. 선호언어
        5. 모국어
        6. 프로필사진
        */
        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(this);

    }


    public void sendData() {

        ArrayList<String> checkboxs = new Join_CheckBox(Join.this).checking();
        joindate = System.currentTimeMillis();

        if (checkboxs.size() == 0) {
            Toast.makeText(Join.this, "관심사를 하나 이상 선택하여 주세요", Toast.LENGTH_SHORT).show();
            return;
        } else if (checkboxs.size() > 2) {
            Toast.makeText(Join.this, "관심사는 2개 까지 선택 가능합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        MemberVO mv = new MemberVO();
        mv.setUid(uid); //0
        mv.setJoinDate(joindate); //0
        mv.setNickname(nickname); //0
        mv.setLikeLang(likeLng); //0
        mv.setUseLang(useLng); //0
        mv.setProfilePhoto(thumb); //0
        mv.setType(type);
        mv.setEmail(email);  //0
        mv.setInterest(checkboxs);


        database.getReference().child("user").child(uid).setValue(mv);
        database.getReference().child("user").child(uid).child("myActivity").setValue(dummy);

        Intent intent = new Intent(Join.this, Mainboard.class);

        LoginInfo loginInfo = LoginInfo.getInstance(Join.this);
        loginInfo.setUser_nickname(nickname);
        loginInfo.setUser_uid(uid);
        loginInfo.setUser_profile(thumb);


        startActivity(intent);
        new ReciverManager(this).startServices();
        Toast.makeText(this, nickname + "님의 가입을 환영합니다!", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.next) {

            EditText et = (EditText) findViewById(R.id.nickname);
            nickname = et.getText().toString();

            databaseReference.child("user").orderByChild("nickname")
                    .equalTo(nickname)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.getChildrenCount() == 0) {

                                sendData();

                            } else {


                                Toast.makeText(Join.this, "닉네임이 이미 존재합니다.", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


        }


    }
}
