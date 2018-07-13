package com.example.gdtbg.buddy.buddy_08_myinfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gdtbg.buddy.R;
import com.example.gdtbg.buddy.VO.MemberVO;
import com.example.gdtbg.buddy.buddy.LoginInfo;
import com.example.gdtbg.buddy.buddy_00_intro.Buddy_Intro;
import com.example.gdtbg.buddy.buddy_01_join.buddy_01_join.Join;
import com.example.gdtbg.buddy.buddy_01_join.buddy_01_join.Join_CheckBox;
import com.example.gdtbg.buddy.buddy_02_mainboard.Mainboard;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import gun0912.tedbottompicker.TedBottomPicker;

import static com.example.gdtbg.buddy.buddy.Utilities.CheckPickedLanguage;
import static com.example.gdtbg.buddy.buddy_00_intro.Buddy_Intro.util;

/**
 * Created by friedegg on 2017-11-15.
 */

public class MyInfo extends AppCompatActivity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private ImageView profilephoto;
    private TextView nick;
    private EditText nickchange;

    LayoutInflater layoutInflater;
    Uri photoUri;
    private String ret;
    private ImageButton go;
    private String useLang;
    private String modifyNick;
    private String user_uid;
    private String user_nickname;
    private String user_email;
    private String user_profile;

    public static final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public static final DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buddy08_myinfo);

        preferences = getSharedPreferences("buddy", Context.MODE_PRIVATE);
        editor = preferences.edit();

        user_uid = preferences.getString("user_uid", "");
        user_email = preferences.getString("user_email", "");
        user_nickname = preferences.getString("user_nickname", "");
        user_profile = preferences.getString("user_profile", "");

        layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        nickchange = (EditText) findViewById(R.id.nickchange);
        profilephoto = findViewById(R.id.profilephoto);
        go = (ImageButton) findViewById(R.id.go);
        nick = (TextView)findViewById(R.id.nick);

        //기본 정보 뿌리기
        nick.setText(user_nickname);
        spinnerSet();
        Glide.with(MyInfo.this).load(user_profile).apply(new RequestOptions().circleCrop()).into(profilephoto);
        myInterests();

        //프로필 사진 변경
        profilephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTedBottomPicker();
            }
        });

        //닉네임 변경
        nick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifyNick = nickchange();

            }
        });

        //수정 완료
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifymyinfo();
            }
        });

    }

    private void myInterests() {

        databaseReference.child("user").child(user_uid).child("interest").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();

                while (child.hasNext())
                {
                    String a = child.next().getValue().toString();
                    new Join_CheckBox(MyInfo.this).checkingArrayName(a);
                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void spinnerSet() {

        spinnerSetFunction("useLang");
        spinnerSetFunction("likeLang");
    }

    private void modifymyinfo() {

        // TODO: 2017-11-16  친구리스트 삭제됨

        final String modifyMylan = modifyMlan(); //변경 모국어
        final String modifyIntlan = modifyIlan(); //변경 관심언어
        final String myProfile = user_profile.toString();
        String modifyNick = nickchange.getText().toString();
        if(nickchange.getText().toString().equals(""))
            modifyNick = user_nickname;

        final String finalModifyNick = modifyNick;
        databaseReference.child("user").orderByChild(user_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                MemberVO item = dataSnapshot.getValue(MemberVO.class);
                //기존것들 불러옴
                item.setUid(item.getUid());
                item.setJoinDate(item.getJoinDate());
                item.setType(item.getType());
                item.setEmail(item.getEmail());
                //수정된것 넣기
                item.setNickname(finalModifyNick);
                item.setProfilePhoto(myProfile);
                item.setLikeLang(modifyIntlan);
                item.setUseLang(modifyMylan);
                ArrayList<String> checkboxs = new Join_CheckBox(MyInfo.this).checking();

                if (checkboxs.size() == 0) {
                    Toast.makeText(MyInfo.this, "관심사를 하나 이상 선택하여 주세요", Toast.LENGTH_SHORT).show();
                    return;
                } else if (checkboxs.size() > 2) {
                    Toast.makeText(MyInfo.this, "관심사는 2개 까지 선택 가능합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                item.setInterest(checkboxs);

                LoginInfo loginInfo = LoginInfo.getInstance(MyInfo.this);
                loginInfo.setUser_nickname(finalModifyNick);
                databaseReference.child("user").child(user_uid).setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        editor.putString("user_nickname", finalModifyNick);
                        editor.commit();
                        Log.d("기덕", "변경 성공 ");
                        Log.d("기덕", "현재 프리퍼런스 닉네임 : " + user_nickname);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("기덕", "변경 실패 ");

                    }
                })
                ;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Toast.makeText(this, "수정 완료", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MyInfo.this, Mainboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    private String modifyIlan() {
        Spinner intlan = (Spinner) findViewById(R.id.intlan);
        ret = util.CheckPickedLanguage(intlan.getSelectedItemPosition());

        return ret;
    }

    private String modifyMlan() {
        Spinner mylan = (Spinner) findViewById(R.id.mylan);
        ret = util.CheckPickedLanguage(mylan.getSelectedItemPosition());

        return ret;
    }

    private String nickchange() {
        nickchange = (EditText) findViewById(R.id.nickchange);
        nick = (TextView) findViewById(R.id.nick);

        nick.setVisibility(View.GONE);
        nickchange.setVisibility(View.VISIBLE);

        String returnnick = nickchange.getText().toString();
        return returnnick;
    }

    public void setTedBottomPicker() {

        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(MyInfo.this)
                .setImageProvider(new TedBottomPicker.ImageProvider() {
                    @Override
                    public void onProvideImage(ImageView imageView, Uri imageUri) {

                        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        Glide.with(MyInfo.this).load(imageUri).into(imageView);
                        Log.d("Log", "Uri Log : " + imageUri.toString());

                    }
                })
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        photoUri = uri;
                        profilephoto = (ImageView) findViewById(R.id.profilephoto);
                        profilephoto.setImageURI(uri);
                    }
                }).create();

        tedBottomPicker.show(getSupportFragmentManager());
    }
    private void spinnerSetFunction(final String whichlang) {

        Spinner mylan = null;
        if(whichlang.equals("useLang")) //모국어
        {
            mylan = (Spinner) findViewById(R.id.mylan);
        }else if(whichlang.equals("likeLang")) //관심언어
        {
            mylan = (Spinner) findViewById(R.id.intlan);
        }

        final Spinner finalMylan = mylan;
        databaseReference.child("user").child(user_uid).
                child(whichlang).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                useLang = dataSnapshot.getValue().toString();
                Log.d("요호호무슨랭", whichlang + ":" + useLang);
                Log.d("요호호어떤스피너", String.valueOf(finalMylan.getId()));
                switch (useLang) {
                    case "eng":
                        finalMylan.setSelection(0);
                        break;
                    case "jp":
                        finalMylan.setSelection(1);
                        break;
                    case "chi":
                        finalMylan.setSelection(2);
                        break;
                    case "deutsch":
                        finalMylan.setSelection(3);
                        break;
                    case "french":
                        finalMylan.setSelection(4);
                        break;
                    case "italian":
                        finalMylan.setSelection(5);
                        break;
                    case "spanish":
                        finalMylan.setSelection(6);
                        break;
                    case "portuguese":
                        finalMylan.setSelection(7);
                        break;
                    case "russian":
                        finalMylan.setSelection(8);
                        break;
                    case "turkish":
                        finalMylan.setSelection(9);
                        break;
                    case "thai":
                        finalMylan.setSelection(0);
                        break;
                    case "laos":
                        finalMylan.setSelection(10);
                        break;
                    case "malaysia":
                        finalMylan.setSelection(11);
                        break;
                    case "indonesian":
                        finalMylan.setSelection(12);
                        break;
                    case "filipino":
                        finalMylan.setSelection(13);
                        break;
                    case "vietnamese":
                        finalMylan.setSelection(14);
                        break;
                    case "cambodia":
                        finalMylan.setSelection(15);
                        break;
                    case "myanmar":
                        finalMylan.setSelection(16);
                        break;
                    case "hindustani":
                        finalMylan.setSelection(17);
                        break;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
