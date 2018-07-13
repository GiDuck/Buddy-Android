package com.example.gdtbg.buddy.buddy_02_mainboard.buddy_02_mainboard_CRUD;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gdtbg.buddy.R;
import com.example.gdtbg.buddy.VO.MainboardVO;
import com.example.gdtbg.buddy.VO.ReplyVO;
import com.example.gdtbg.buddy.buddy.CustomActivity;
import com.example.gdtbg.buddy.buddy.Database.TransactionHelper;
import com.example.gdtbg.buddy.buddy.LoginInfo;
import com.example.gdtbg.buddy.buddy.Tools;
import com.example.gdtbg.buddy.buddy_02_mainboard.Mainboard;
import com.example.gdtbg.buddy.buddy_02_mainboard_reply.Mainboard_Reply_Fragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import static com.example.gdtbg.buddy.R.id.date;
import static com.example.gdtbg.buddy.buddy.Utilities.CurrentLocalTime;
import static com.example.gdtbg.buddy.buddy.Utilities.databaseReference;
import static com.example.gdtbg.buddy.buddy.Utilities.dateFormat;
import static com.example.gdtbg.buddy.buddy.Utilities.storageReference;
import static com.example.gdtbg.buddy.buddy.Utilities.timeFormat;

/**
 * Created by friedegg on 2017-10-09.
 */

public class Mainboard_Post_Detail extends CustomActivity implements View.OnClickListener {

    private TextView name;
    private TextView dateView;
    private TextView contentView;

    private Button modify;
    private Button delete;
    private TextView heart_num;
    private TextView comment_num;
    private String user_nickname;
    private ImageView user_profile;

    private String writer_uid;
    private Long time;
    private String content;

    private LinearLayout post_detail_photo_area;

    private LikeButton heart;

    private String user_uid;
    private String board_uid;
    private String user_profile_photo;

    private Boolean isLiked = false;
    private MainboardVO item;

    private ImageView reply_Btn;
    private EditText reply_content;

    private Fragment fragment;
    private ArrayList<ReplyVO> items;
    private ArrayList<String> reply_uidList;

    private EditText mainboard_modify_content;
    private Button mainboard_modifyBtn;

    private ImageView flag1;
    private ImageView flag2;


    private FragmentManager fragmentManager;
    private FragmentTransaction ft;

    ArrayList<String> photoUrl;

    private LoginInfo loginInfo;
    private TransactionHelper transactionHelper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buddy02_post_detail);
        super.setMenuBar();
        actionbar_title.setText("친구 찾기");


        init();
        getData();
        initView();
        pushData();
        setLikeCount();
        attachListener();

        if (item.getBoard_photos() != null && item.getBoard_photos().size() != 0) {
            setImageArea();
        }
        //프레그먼트 초기화
        attachFragment();


    }

    public void init() {

        loginInfo = LoginInfo.getInstance(this);
        transactionHelper = new TransactionHelper();

        //리스트 초기화
        items = new ArrayList<>();
        reply_uidList = new ArrayList<>();

    }

    public void getData() {
        Intent intent = getIntent();
        item = (MainboardVO) intent.getSerializableExtra("mainboardVO");
        user_uid = com.example.gdtbg.buddy.buddy.LoginInfo.getInstance(this).getUser_uid();
        board_uid = intent.getStringExtra("board_uid");
        isLiked = intent.getBooleanExtra("isLiked", isLiked);
        photoUrl = (ArrayList<String>) intent.getSerializableExtra("photoUrl");

        writer_uid = item.getBoard_writer_uid();
        time = item.getBoard_time();
        content = item.getBoard_content();


        //프리퍼런스에서 필요한 데이터를 가져옴
        user_nickname = loginInfo.getUser_nickname();
        user_profile_photo = loginInfo.getUser_profile();


    }

    public void setImageArea() {

        post_detail_photo_area = (LinearLayout) findViewById(R.id.post_detail_photo_area);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(10, 10, 10, 10);

        Iterator<String> iterator = item.getBoard_photos().keySet().iterator();

        while (iterator.hasNext()) {
            String key = iterator.next();
            ImageView image = new ImageView(Mainboard_Post_Detail.this);
            image.setLayoutParams(layoutParams);
            post_detail_photo_area.addView(image);
            Glide.with(Mainboard_Post_Detail.this).load(item.getBoard_photos().get(key)).into(image);
        }

    }

    public void attachListener() {

        //LikeButton에 클릭 리스너 부착
        heart.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                transactionHelper.onLikeClicked(databaseReference.child("board").child(board_uid), user_uid);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                transactionHelper.onLikeClicked(databaseReference.child("board").child(board_uid), user_uid);
            }
        });

        //클릭 리스너 부착
        modify.setOnClickListener(this);
        delete.setOnClickListener(this);
        reply_Btn.setOnClickListener(this);

        databaseReference.child("user").child(user_uid).child("likeLang")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            String flag = (String) dataSnapshot.getValue();
                            int resId = new Tools(Mainboard_Post_Detail.this).FlagTypeCaster(flag);
                            flag1.setImageResource(resId);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        databaseReference.child("user").child(user_uid).child("useLang")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            String flag = (String) dataSnapshot.getValue();
                            int resId = new Tools(Mainboard_Post_Detail.this).FlagTypeCaster(flag);
                            flag2.setImageResource(resId);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    public void pushData() {

        //밀리세컨드 단위로 받은 time 정보를 Date 객체로 변환
        Date board_date = new Date(time);

        if (user_nickname == null) {
            name.setText("이름 없음"); //유저 닉네임이 null값일시 기본값으로 초기화 (이름 없음)
        } else {
            Log.d("기덕", "board detail : " + writer_uid);
            databaseReference.child("user").child(writer_uid).child("nickname").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String nickname = (String)dataSnapshot.getValue();
                    name.setText(nickname);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        if (user_uid.equals(writer_uid)) {
            //글 작성자와 현재 로그인한 유저의 닉네임이 같다면 수정, 삭제 버튼 나타나도록
            modify.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);

        }


        databaseReference.child("user").child(item.getBoard_writer_uid()).child("profilePhoto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String profile = (String)dataSnapshot.getValue();
                Glide.with(Mainboard_Post_Detail.this).load(profile).apply(new RequestOptions().circleCrop()).apply(new RequestOptions().error(R.drawable.ic_user2)).into(user_profile);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        dateView.setText(dateFormat.format(board_date) + "\t" + timeFormat.format(board_date));
        contentView.setText(content);
        heart.setLiked(isLiked); //현재 Like버튼 상태 표시


    }


    public void initView() {

        //뷰 연결
        name = (TextView) findViewById(R.id.name);
        dateView = (TextView) findViewById(date);
        contentView = (TextView) findViewById(R.id.textv);
        heart_num = (TextView) findViewById(R.id.heart_num);
        comment_num = (TextView) findViewById(R.id.comment_num);
        modify = (Button) findViewById(R.id.modify);
        delete = (Button) findViewById(R.id.delete);
        heart = (LikeButton) findViewById(R.id.heart);
        reply_Btn = (ImageView) findViewById(R.id.mainboard_detail_reply_writeBtn);
        reply_content = (EditText) findViewById(R.id.mainboard_detail_reply_content);
        user_profile = (ImageView) findViewById(R.id.profile_photo);
        flag1 = (ImageView)findViewById(R.id.flag);
        flag2 = (ImageView)findViewById(R.id.flag2);

        mainboard_modify_content = (EditText) findViewById(R.id.mainboard_modify_content);
        mainboard_modifyBtn = (Button) findViewById(R.id.mainboard_modifyBtn);

        post_detail_photo_area = (LinearLayout) findViewById(R.id.post_detail_photo_area);

    }


    public void setLikeCount() {


        //LikeCount의 변화에 따라 값을 받아옴
        databaseReference.child("board").child(board_uid).child("likeCount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Long likeCount = (Long) dataSnapshot.getValue();
                if (String.valueOf(likeCount).equals("null")) { //만약 표시 되는 문자열이 null 이라면 그냥 통과

                } else {
                    heart_num.setText(String.valueOf(likeCount)); //값이 있다면 좋아요 개수에 넣어줌
                    databaseReference.child("board").child(board_uid).addListenerForSingleValueEvent(new ValueEventListener() { //이어서 값의 변화에 따라서
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) { //변한 값에 맞게 하트를 on/off 해준다


                            MainboardVO item = dataSnapshot.getValue(MainboardVO.class);


                            if (item.likes != null) {
                                if (item.likes.containsKey(user_uid)) {
                                    heart.setLiked(true);
                                } else {

                                    heart.setLiked(false);

                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {


                            Log.e("FirebaseError", databaseError.getDetails());

                        }
                    });


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(Mainboard_Post_Detail.this, "데이터 갱신 중에 오류가 발생 하였습니다.", Toast.LENGTH_SHORT).show();

            }
        });


    }


    /*------------------- 댓글창 프레그먼트를 초기화 시키는 메소드 -------------------*/

    public void attachFragment() {


        //
        databaseReference.child("board").child(board_uid).child("replies").orderByChild("reply_time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (items != null) {
                    items.clear();
                }
                if (reply_uidList != null) {
                    reply_uidList.clear();
                }
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    items.add(snapshot.getValue(ReplyVO.class));
                    reply_uidList.add(snapshot.getKey());


                }

                comment_num.setText(String.valueOf(dataSnapshot.getChildrenCount()));

                Log.d("기덕", "가져온 댓글 갯수 : " + items.size());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //전달할 데이터를 번들에 담음
        Bundle bundle = new Bundle();
        bundle.putString("board_key", board_uid);
        bundle.putSerializable("reply_list", items);

        //프레그먼트 초기화
        fragment = new Mainboard_Reply_Fragment();
        fragment.setArguments(bundle);


        //프레그먼트 교체 (하위 버전 호환을 위해 app.v4 버전 사용)
        fragmentManager = getSupportFragmentManager();
        ft = fragmentManager.beginTransaction();
        ft.replace(R.id.mainboard_detail_reply_fragment, fragment).commit();


    }

    /*------------------- 게시글 삭제 메소드 -------------------*/

    public void delete() {

        //다이얼로그 생성
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("게시글을 삭제 하시곘습니까?");

        alertDialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {

                if (photoUrl != null) {
                    for (String url : photoUrl) {
                        Log.d("기덕", "delete board uid : " + board_uid);
                        Log.d("기덕", "delete photo url : " + url);
                        storageReference.child("images").child("mainboard").child(board_uid).child(url).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Log.d("기덕", "delete photo success ");

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("기덕", "delete photo fail! " + e.getMessage());

                            }
                        });
                    }

                }
                databaseReference.child("board").child(board_uid).removeValue();


                Toast.makeText(Mainboard_Post_Detail.this, "게시물이 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Mainboard_Post_Detail.this, Mainboard.class);
                startActivity(intent);
                dialogInterface.dismiss();

                //"네" 클릭시 게시글 삭제

            }
        });

        alertDialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //아니면 그냥 창 종료.
                dialogInterface.dismiss();

            }
        });

        AlertDialog alert = alertDialog.create();
        alert.setTitle("게시글 삭제");
        alert.show();


    }

    /*------------------- 댓글 수정 메소드 -------------------*/
    public void modifyReply(final int position) {

        //다이얼로그 생성
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("댓글을 수정 하시겠습니까?");

        alertDialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Mainboard_Reply_Fragment tf = (Mainboard_Reply_Fragment) getSupportFragmentManager().findFragmentById(R.id.mainboard_detail_reply_fragment);
                LinearLayout reply_item_modify_mode = tf.getAdapter().getHoldViews().get(position).findViewById(R.id.mainboard_reply_item_modify_mode);
                LinearLayout reply_item_write_mode = tf.getAdapter().getHoldViews().get(position).findViewById(R.id.mainboard_reply_item_write_mode);
                EditText reply_item_content = tf.getAdapter().getHoldViews().get(position).findViewById(R.id.mainboard_modify_content);

                //"네" 클릭시 댓글 수정
                ReplyVO reply = items.get(position);
                reply.setReply_content(reply_item_content.getText().toString());
                databaseReference.child("board").child(board_uid).child("replies").child(reply_uidList.get(position)).setValue(reply);
                Toast.makeText(Mainboard_Post_Detail.this, "댓글이 수정 되었습니다.", Toast.LENGTH_SHORT).show();

                reply_item_modify_mode.setVisibility(View.GONE);
                reply_item_write_mode.setVisibility(View.VISIBLE);

                attachFragment();

                dialogInterface.dismiss();


            }
        });

        alertDialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //아니면 그냥 창 종료.
                dialogInterface.dismiss();

            }
        });

        AlertDialog alert = alertDialog.create();
        alert.setTitle("댓글 수정");
        alert.show();


    }


        /*------------------- 댓글 작성 메소드 -------------------*/

    public void writeReply() {

        ReplyVO reply = new ReplyVO();

        reply.setReply_writer_uid(user_uid);
        reply.setReply_content(reply_content.getText().toString());
        reply.setReply_time(CurrentLocalTime());

        databaseReference.child("board").child(board_uid).child("replies").push().setValue(reply);

        reply_content.setText("");


    }

        /*------------------- 댓글 삭제 메소드 -------------------*/

    public void deleteReply(final int position) {

        //다이얼로그 생성
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("댓글을 삭제 하시겠습니까?");

        alertDialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                //"네" 클릭시 댓글 수정

                databaseReference.child("board").child(board_uid).child("replies").child(reply_uidList.get(position)).removeValue();
                Toast.makeText(Mainboard_Post_Detail.this, "댓글이 삭제 되었습니다.", Toast.LENGTH_SHORT).show();

                attachFragment();
                dialogInterface.dismiss();


            }
        });

        alertDialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //아니면 그냥 창 종료.
                dialogInterface.dismiss();

            }
        });

        AlertDialog alert = alertDialog.create();
        alert.setTitle("댓글 수정");
        alert.show();


    }


    @Override
    public void onClick(View view) {

        Intent intent;
        switch (view.getId()) {

            case R.id.modify:
                intent = new Intent(Mainboard_Post_Detail.this, Mainboard_Modify_Post.class);
                intent.putExtra("mainboardVO", item);
                intent.putExtra("board_uid", board_uid);
                intent.putExtra("isLiked", heart.isLiked());
                startActivity(intent);
                break;

            case R.id.delete:
                delete();
                break;

            case R.id.mainboard_detail_reply_writeBtn:
                writeReply();
                attachFragment();
                break;


        }

    }

    /*------------------- 컨텍스트 메뉴의 아이템을 클릭했을 때 호출되는 메소드 -------------------*/


    @Override
    public boolean onContextItemSelected(MenuItem item) {


        final int nPosition = item.getOrder(); //뷰홀더에서 position 값으로 등록한 id 값을 뽑아옴

        switch (item.getItemId()) {


            //1이면 수정
            case 1:

                Mainboard_Reply_Fragment tf = (Mainboard_Reply_Fragment) getSupportFragmentManager().findFragmentById(R.id.mainboard_detail_reply_fragment);
                //해당 Fragment를 찾아서 인스턴스 변수에 저장한다.


                LinearLayout reply_item_modify_mode = tf.getAdapter().getHoldViews().get(nPosition).findViewById(R.id.mainboard_reply_item_modify_mode);
                LinearLayout reply_item_write_mode = tf.getAdapter().getHoldViews().get(nPosition).findViewById(R.id.mainboard_reply_item_write_mode);
                //수정시 visible 되는 레이아웃과 작성시 visible 되는 레이아웃을 불러온다.
                //레이아웃은 프레그먼트에 안에 있는 Mainboard_Reply_Adapter 안에 저장되어 있는 View List에서 참조한다.

                reply_item_modify_mode.setVisibility(View.VISIBLE);
                reply_item_write_mode.setVisibility(View.GONE);

                Button reply_button = tf.getAdapter().getHoldViews().get(nPosition).findViewById(R.id.mainboard_modifyBtn);

                reply_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modifyReply(nPosition);
                    }
                });

                break;

            //2면 삭제
            case 2:
                deleteReply(nPosition);
                break;
        }

        return super.onContextItemSelected(item);
    }


}
