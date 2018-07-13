package com.example.gdtbg.buddy.buddy_02_mainboard.buddy_02_mainboard_CRUD;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Iterator;

import gun0912.tedbottompicker.TedBottomPicker;

import static com.example.gdtbg.buddy.buddy.Utilities.CurrentLocalTime;
import static com.example.gdtbg.buddy.buddy.Utilities.databaseReference;
import static com.example.gdtbg.buddy.buddy.Utilities.storageReference;

public class Mainboard_Modify_Post extends CustomActivity implements View.OnClickListener {

    private EditText content;
    private ImageButton btnPhoto;
    private ImageButton btnSubmit;
    private LinearLayout post_main;
    private ArrayList<Uri> updatePhotos;
    private LoginInfo loginInfo;
    LayoutInflater inflater;


    Intent intent;
    MainboardVO item;
    String board_uid;
    Boolean isLiked = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buddy02_write_post);

        loginInfo = LoginInfo.getInstance(this);
        intent = getIntent();
        item = (MainboardVO) intent.getSerializableExtra("mainboardVO");
        board_uid = intent.getStringExtra("board_uid");
        isLiked = intent.getBooleanExtra("isLiked", isLiked);

        super.setMenuBar();
        actionbar_title.setText("글 수정 하기");

        updatePhotos = new ArrayList<>();


        btnPhoto = (ImageButton) findViewById(R.id.btnPhoto);
        btnSubmit = (ImageButton) findViewById(R.id.btnSubmit);
        content = (EditText) findViewById(R.id.content);

        post_main = (LinearLayout) findViewById(R.id.post_main);

        content.setText(item.getBoard_content());

        btnPhoto.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        setImages();


    }

    public void setImages() {


        Iterator<String> iterator = item.getBoard_photos().keySet().iterator();
        while (iterator.hasNext()) {

            final String key = iterator.next();

            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            final View imageSelector = inflater.inflate(R.layout.buddy_image_selector, null);
            ImageView image = (ImageView) imageSelector.findViewById(R.id.image_selector_image);
            ImageButton removeBtn = (ImageButton) imageSelector.findViewById(R.id.image_selector_removeBtn);
            Glide.with(this).load(item.getBoard_photos().get(key)).into(image);
            removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    post_main.removeView(imageSelector);
                    item.getBoard_photos().remove(key);
                    storageReference.child("images").child("mainboard").child(board_uid).child(key).delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Log.d("기덕", "수정 사진 삭제 완료");

                                }
                            });
                }
            });

            post_main.addView(imageSelector);


        }


    }

    public void pickImages() {

        Log.d("기덕", "테드");

        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(Mainboard_Modify_Post.this)
                .setImageProvider(new TedBottomPicker.ImageProvider() {
                    @Override
                    public void onProvideImage(ImageView imageView, Uri imageUri) {
                        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        Glide.with(Mainboard_Modify_Post.this).load(imageUri).into(imageView);
                        Log.d("Log", "Uri Log : " + imageUri.toString());
                    }
                }).setOnMultiImageSelectedListener(new TedBottomPicker.OnMultiImageSelectedListener() {
                    @Override
                    public void onImagesSelected(ArrayList<Uri> uriList) {

                        updatePhotos = uriList;
                        Log.d("기덕", "고른 사진 리스트 + " + updatePhotos.size());
                        displayAddedPhoto();

                    }
                }).create();

        tedBottomPicker.show(getSupportFragmentManager());


    }

    public void displayAddedPhoto() {


        for (final Uri uri : updatePhotos) {

            Log.d("기덕", "view 추가");

            final View imageSelector = inflater.inflate(R.layout.buddy_image_selector, null);
            ImageView image = (ImageView) imageSelector.findViewById(R.id.image_selector_image);
            ImageButton removeBtn = (ImageButton) imageSelector.findViewById(R.id.image_selector_removeBtn);
            Glide.with(this).load(uri).into(image);
            post_main.addView(imageSelector);

            removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int index = updatePhotos.indexOf(uri);
                    updatePhotos.remove(index);
                    post_main.removeView(imageSelector);


                }
            });
            Log.d("기덕", "view 추가 끝");


        }


    }

    @SuppressWarnings("all")
    public void uploadImages() {

        for (Uri uri : updatePhotos) {

            final String stoarge_key = databaseReference.push().getKey();

            storageReference.child("images").child("mainboard").child(board_uid).child(stoarge_key).putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String url = taskSnapshot.getDownloadUrl().toString();
                            databaseReference.child("board").child(board_uid).child("board_photos").child(stoarge_key).setValue(url);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("기덕", "게시글 수정 사진 업로드 실패");
                }
            });
        }


    }


    public void sendMessage() {


        Long board_time = CurrentLocalTime();
        String board_content = content.getText().toString();
        String board_writer_uid = loginInfo.getUser_uid();


        item.setBoard_content(board_content);
        item.setBoard_time(board_time);
        item.setBoard_writer_uid(board_writer_uid);

        databaseReference.child("board").child(board_uid).setValue(item);

        Toast.makeText(Mainboard_Modify_Post.this, "수정 완료", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Mainboard_Modify_Post.this, Mainboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {

            case R.id.btnPhoto:
                pickImages();
                break;

            case R.id.btnSubmit:
                sendMessage();
                uploadImages();
                break;


        }
    }


}
