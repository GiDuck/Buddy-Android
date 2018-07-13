package com.example.gdtbg.buddy.buddy_02_mainboard.buddy_02_mainboard_CRUD;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gdtbg.buddy.R;
import com.example.gdtbg.buddy.VO.MainboardVO;
import com.example.gdtbg.buddy.buddy.CustomActivity;
import com.example.gdtbg.buddy.buddy.LoginInfo;
import com.example.gdtbg.buddy.buddy_02_mainboard.Mainboard;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import gun0912.tedbottompicker.TedBottomPicker;

import static com.example.gdtbg.buddy.buddy.Utilities.CurrentLocalTime;
import static com.example.gdtbg.buddy.buddy.Utilities.databaseReference;
import static com.example.gdtbg.buddy.buddy.Utilities.storageReference;

/**
 * Created by gdtbg on 2017-10-29.
 */

public class Mainboard_Write_Post extends CustomActivity implements View.OnClickListener, ValueEventListener {

    private EditText content;
    private ImageButton btnPhoto;
    private ImageButton btnSubmit;
    boolean hasCheck;
    private LoginInfo loginInfo;

    ArrayList<String> photoUrls;
    String user_uid;
    ArrayList<Uri> photoUris;
    String user_nickname;


    LayoutInflater layoutInflater;
    LinearLayout post_main;


    int count;

    MainboardVO new_board;
    final String key = databaseReference.push().getKey();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buddy02_write_post);
        super.setMenuBar();
        actionbar_title.setText("글 작성 하기");
        loginInfo = LoginInfo.getInstance(this);

        count = 0;
        photoUris = new ArrayList<>();
        btnPhoto = (ImageButton) findViewById(R.id.btnPhoto);
        btnSubmit = (ImageButton) findViewById(R.id.btnSubmit);
        content = (EditText) findViewById(R.id.content);

        btnPhoto.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);


        user_uid = loginInfo.getUser_uid();

        layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        post_main = (LinearLayout) findViewById(R.id.post_main);

        hasCheck = false;


    }

    @SuppressWarnings("all")
    public void sendMessage() {


        Long board_time = CurrentLocalTime();
        String board_content = content.getText().toString();
        String board_writer_uid = loginInfo.getUser_uid();

        new_board = new MainboardVO();
        new_board.setBoard_writer_uid(board_writer_uid);

        new_board.setBoard_time(board_time);
        new_board.setBoard_content(board_content);

        databaseReference.child("board").child(key).setValue(new_board);


        Log.d("기덕", "URI PHOTOS 갯수 : " + photoUris.size());
        photoUrls = new ArrayList<>();
        for (final Uri photo : photoUris) {
            final String storage_key = databaseReference.push().getKey();
            //비동기로 들어감
            storageReference.child("images").child("mainboard").child(key).child(storage_key).putFile(photo)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                                    Log.d("기덕", "파일업로드 " + photo.toString() + " 전송 성공");
                                    Log.d("기덕", "파일 URL : " + taskSnapshot.getDownloadUrl().toString());

                                    databaseReference.child("board").child(key).child("board_photos").child(storage_key).setValue(taskSnapshot.getDownloadUrl().toString());

                                }
                            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("기덕", "파일업로드 + " + photo.toString() + " 전송 실패");

                        }
                    });


        }
        Log.d("기덕", "URL 모음 크기 : " + photoUrls.size());

        Toast.makeText(Mainboard_Write_Post.this, "작성 완료", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Mainboard_Write_Post.this, Mainboard.class);
        startActivity(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.finish();


    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {

            case R.id.btnPhoto:


                if (hasCheck) { //한 번 눌러져 있는 상태면 (이미 사진이 올라와 있는 상태)

                    post_main.removeAllViews();
                    setTedBottomPicker();


                } else {
                    setTedBottomPicker();
                    hasCheck = true;
                }


                break;

            case R.id.btnSubmit:
                sendMessage();

                break;


        }
    }

    public void setTedBottomPicker() {


        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(Mainboard_Write_Post.this)
                .setImageProvider(new TedBottomPicker.ImageProvider() {
                    @Override
                    public void onProvideImage(ImageView imageView, Uri imageUri) {

                        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        Glide.with(Mainboard_Write_Post.this).load(imageUri).into(imageView);
                        Log.d("Log", "Uri Log : " + imageUri.toString());

                    }
                })
                .setOnMultiImageSelectedListener(new TedBottomPicker.OnMultiImageSelectedListener() {
                    @Override
                    public void onImagesSelected(ArrayList<Uri> uriList) {


                        photoUris = uriList;
                        post_main = (LinearLayout) findViewById(R.id.post_main);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);

                        layoutParams.setMargins(10, 10, 10, 10);

                        for (Uri item : uriList) {
                            ImageView image = new ImageView(Mainboard_Write_Post.this);
                            image.setLayoutParams(layoutParams);
                            post_main.addView(image);
                            Glide.with(Mainboard_Write_Post.this).load(item).into(image);
                        }


                    }
                }).create();

        tedBottomPicker.show(getSupportFragmentManager());
    }


    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        count = Integer.parseInt(String.valueOf(dataSnapshot.getChildrenCount()));

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

        Toast.makeText(this, "전송 실패 하였습니다.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.get(this).trimMemory(level);
    }


}
