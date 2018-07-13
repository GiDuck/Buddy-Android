package com.example.gdtbg.buddy.buddy_02_mainboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gdtbg.buddy.R;
import com.example.gdtbg.buddy.VO.MainboardVO;
import com.example.gdtbg.buddy.buddy.Database.TransactionHelper;
import com.example.gdtbg.buddy.buddy.LoginInfo;
import com.example.gdtbg.buddy.buddy.Tools;
import com.example.gdtbg.buddy.buddy.VersionManager;
import com.example.gdtbg.buddy.buddy.interfaces.ItemOnclickListener;
import com.example.gdtbg.buddy.buddy_02_mainboard.buddy_02_mainboard_CRUD.Mainboard_Post_Detail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.Iterator;

import static com.example.gdtbg.buddy.buddy.Utilities.databaseReference;
import static com.example.gdtbg.buddy.buddy_00_intro.Buddy_Intro.util;


/**
 * Created by friedegg on 2017-10-02.
 */

public class Mainboard_Adapter extends RecyclerView.Adapter<Mainboard_ViewHolder> implements ItemOnclickListener {

    private ArrayList<MainboardVO> items;
    private ArrayList<String> uidList;
    private Context context;

    private String user_uid;
    private TransactionHelper transactionHelper;

    LoginInfo loginInfo;

    private Intent intent;

    public Mainboard_Adapter(Context context) {
        this.context = context;
        loginInfo = LoginInfo.getInstance(context);
        user_uid = loginInfo.getUser_uid();
        transactionHelper = new TransactionHelper();


    }

    /*------------------- 데이터를 받아와서 초기화 시켜주는 메소드 -------------------*/


    public void setData(ArrayList<MainboardVO> items, ArrayList<String> uidList) {


        this.items = items;
        this.uidList = uidList;


    }

    @Override
    public Mainboard_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.buddy02_main_item, parent, false);
        Mainboard_ViewHolder holder = new Mainboard_ViewHolder(v);

        return holder;
    }


    @Override
    public void onBindViewHolder(final Mainboard_ViewHolder holder, final int position) {

        try {
            MainboardVO item = items.get(position);
            String long_text = null;
            String writer_uid = item.getBoard_writer_uid();

            databaseReference.child("user").child(writer_uid).child("nickname").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String nickname = (String) dataSnapshot.getValue();
                    holder.nickname.setText(nickname);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            databaseReference.child("user").child(writer_uid).child("profilePhoto").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String profile = (String) dataSnapshot.getValue();

                    Glide.with(context).load(profile).apply(new RequestOptions().circleCrop())
                            .apply(new RequestOptions().error(R.drawable.ic_user2))
                            .into(holder.profile_photo);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            if (item.getBoard_photos() != null && item.getBoard_photos().size() != 0) {

                holder.mainboard_item_photo_area.setVisibility(View.VISIBLE);
                ArrayList<String> photoUrl = new ArrayList<>();
                Iterator<String> iterator = item.getBoard_photos().keySet().iterator();

                while (iterator.hasNext()) {

                    String key = iterator.next();
                    photoUrl.add(item.getBoard_photos().get(key));


                }

                if (photoUrl.size() == 1) {

                    holder.mainboard_one_ptoto.setVisibility(View.VISIBLE);
                    Glide.with(context).load(photoUrl.get(0)).into(holder.mainboard_one_ptoto_view);


                } else if (photoUrl.size() == 2) {

                    holder.mainboard_one_ptoto.setVisibility(View.GONE);
                    holder.mainboard_multiple_photo.setVisibility(View.VISIBLE);
                    holder.mainboard_more_image.setVisibility(View.GONE);
                    Glide.with(context).load(photoUrl.get(0)).into(holder.mainboard_multiple_ptoto_view1);
                    Glide.with(context).load(photoUrl.get(1)).into(holder.mainboard_multiple_ptoto_view2);


                } else if (photoUrl.size() > 2) {

                    holder.mainboard_one_ptoto.setVisibility(View.VISIBLE);
                    holder.mainboard_multiple_photo.setVisibility(View.VISIBLE);

                    holder.mainboard_multiple_ptoto_view2.setColorFilter(Color.parseColor("#66000000"), PorterDuff.Mode.SRC_OVER);

                    Glide.with(context).load(photoUrl.get(0)).into(holder.mainboard_one_ptoto_view);
                    Glide.with(context).load(photoUrl.get(1)).into(holder.mainboard_multiple_ptoto_view1);
                    Glide.with(context).load(photoUrl.get(2)).into(holder.mainboard_multiple_ptoto_view2);

                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View layout = (View) inflater.inflate(R.layout.buddy_more_image, null);


                }


            } else {

                holder.mainboard_item_photo_area.setVisibility(View.GONE);


            }


            //텍스트 길이가 100자 이상 될 경우, 텍스트 100자까지 잘라주고 끝에 " +더보기 " 라는 색 있는 문자열을 삽입.
            if (item.getBoard_content().length() > 100) {

                long_text = item.getBoard_content().substring(0, 99);
                long_text += util.ColorMaker("\t\t\t+더보기");
                holder.content.setText(VersionManager.fromHtml(long_text));

            } else {
                holder.content.setText(item.getBoard_content());

            }


            // likes 목록에 현재 로그인 한 유저의 uid가 들어있다면 (즉, 이 유저가 지금 바인딩 하고 있는 게시글에 좋아요를 클릭한 적이 있다면)
            // like button을 on으로, 아니면 off로 초기화
            if (item.likes.containsKey(user_uid)) {

                holder.likeButton.setLiked(true);

            } else {

                holder.likeButton.setLiked(false);
            }

            //게시글에 달린 댓글 수를 가지고 와서 초기화 시켜줌. (변화 있을때 마다)
            databaseReference.child("board").child(uidList.get(position)).child("replies").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    holder.commentNum.setText(String.valueOf(dataSnapshot.getChildrenCount()));

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            holder.heartNum.setText(Integer.toString(item.likeCount)); //좋아요 수를 세팅해줌

            final DatabaseReference reference = databaseReference.child("board").child(uidList.get(position));
            //LikeButton의 클릭 리스너를 이너클래스로 구현

            holder.likeButton.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {


                    transactionHelper.onLikeClicked(reference, user_uid);


                }

                @Override
                public void unLiked(LikeButton likeButton) {

                    transactionHelper.onLikeClicked(reference, user_uid);

                }
            });

            databaseReference.child("user").child(user_uid).child("likeLang")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                String flag = (String) dataSnapshot.getValue();
                                int resId = new Tools(context).FlagTypeCaster(flag);
                                holder.flag1.setImageResource(resId);
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
                                int resId = new Tools(context).FlagTypeCaster(flag);
                                holder.flag2.setImageResource(resId);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


            //likeCount가 변할때 마다 값을 새롭게 받아옴
            databaseReference.child("board").child(uidList.get(position)).child("likeCount")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Long likeCount = (Long) dataSnapshot.getValue();
                            if (String.valueOf(likeCount).equals("null")) { //만약 표시 되는 문자열이 null 이라면 그냥 통과

                            } else {
                                holder.heartNum.setText(String.valueOf(likeCount)); //값이 있다면 좋아요 개수에 넣어줌


                                databaseReference.child("board").child(uidList.get(position))
                                        .addListenerForSingleValueEvent(new ValueEventListener() { //likeCount가 변했다면
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) { //좋아요 표시도 변화에 맞게 on/off 해준다.


                                                MainboardVO item = dataSnapshot.getValue(MainboardVO.class);
                                                if (item == null) {


                                                }

                                                if (item.likes != null) {
                                                    if (item.likes.containsKey(user_uid)) {
                                                        holder.likeButton.setLiked(true);
                                                    } else {

                                                        holder.likeButton.setLiked(false);

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

                            Toast.makeText(context, "데이터 갱신 중에 오류가 발생 하였습니다.", Toast.LENGTH_SHORT).show();
                            Log.e("FirebaseError", databaseError.getDetails());

                        }
                    });

            holder.setItemOnclickListener(this); //아이템 뷰에 온 클릭 리스너 부착

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onListItemClick(int position) {

        //아이템 뷰 클릭시 상세보기 액티비티로 이동

        MainboardVO item = items.get(position);
        intent = new Intent(context, Mainboard_Post_Detail.class);
        intent.putExtra("mainboardVO", item);
        intent.putExtra("user_uid", user_uid);
        intent.putExtra("board_uid", uidList.get(position));

        if (item.likes.containsKey(user_uid)) {

            intent.putExtra("isLiked", true);

        } else {

            intent.putExtra("isLiked", false);

        }

        ArrayList<String> photoUrl = new ArrayList<>();
        if (item.getBoard_photos() != null && item.getBoard_photos().size() != 0) {

            Iterator<String> iterator = item.getBoard_photos().keySet().iterator();
            while (iterator.hasNext()) {

                String key = iterator.next();
                photoUrl.add(key);

            }
            intent.putExtra("photoUrl", photoUrl);

        }


        context.startActivity(intent);

    }


}
