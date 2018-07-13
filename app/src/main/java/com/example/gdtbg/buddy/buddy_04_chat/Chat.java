package com.example.gdtbg.buddy.buddy_04_chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gdtbg.buddy.R;
import com.example.gdtbg.buddy.VO.ChatVO;
import com.example.gdtbg.buddy.buddy.CustomActivity;
import com.example.gdtbg.buddy.buddy.LoginInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.gdtbg.buddy.buddy.Utilities.CurrentLocalTime;
import static com.example.gdtbg.buddy.buddy.Utilities.databaseReference;
import static com.example.gdtbg.buddy.buddy_00_intro.Buddy_Intro.database;

public class Chat extends CustomActivity implements View.OnClickListener, ValueEventListener {

    RecyclerView rv;
    LinearLayoutManager lim;
    Chat_Adapter adapter;

    EditText inputField;
    Button submitBtn;

    ArrayList<ChatVO> items; //채팅 리스트를 담을 ArrayList

    private String userEmail; //User의 Email
    private String userNickname; //User의 NickName
    private String user_uid;


    private String friend_uid;
    private String chatroom_uid;

    private LoginInfo loginInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buddy04_main);
        super.setMenuBar();

        actionbar_title.setText("BUDDY");


        loginInfo = LoginInfo.getInstance(this);
        user_uid = loginInfo.getUser_uid();
        userNickname = loginInfo.getUser_nickname(); //preference에서 이메일과 닉네임 꺼내오기

        inputField = (EditText) findViewById(R.id.chat_inputField);
        submitBtn = (Button) findViewById(R.id.chat_sendBtn);
        items = new ArrayList<>();

        try {
            Intent intent = getIntent();
            chatroom_uid = intent.getStringExtra("chatroom_uid");
            friend_uid = intent.getStringExtra("friend_uid");

            Log.d("기덕", "채팅 uid " + chatroom_uid);
            Log.d("기덕", friend_uid);

            items = database.read_chat_data(chatroom_uid);
            adapter = new Chat_Adapter(this, items, friend_uid);

            rv = (RecyclerView) findViewById(R.id.chat_recyclerview);
            lim = new LinearLayoutManager(this);

            rv.setLayoutManager(lim);
            rv.setAdapter(adapter);

            /*items = database.read_chat_data(chatroom_uid);
            adapter.listAdd(items);*/


            submitBtn.setOnClickListener(this);

            this.items.clear();
            databaseReference.child("chat").child(chatroom_uid).addValueEventListener(this);

        } catch (Exception e) {

            e.printStackTrace();

        }


    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.chat_sendBtn) { //채팅 전송 버튼을 클릭 하였을 시,

            try {

                DatabaseReference myRef = databaseReference.child("chat").child(chatroom_uid); //데이터베이스 참조 객체를 채팅 노드를 향하도록 설정.


                Long today_time = CurrentLocalTime(); //현재 시간을 밀리세컨드 단위로 반환
                String content = inputField.getText().toString(); // 사용자가 입력한 내용 받아오고

                ChatVO newChat = new ChatVO();
                newChat.setUser_uid(user_uid);
                newChat.setContent(content);
                newChat.setChatroom_uid(chatroom_uid);
                newChat.setTime(today_time);
                newChat.setNickname(userNickname);
                //채팅VO 초기화

                String key = myRef.push().getKey(); //해쉬키 생성
                myRef.child(key).setValue(newChat); //채팅 노드에 푸쉬

                HashMap<String, String> lastChat = new HashMap<>();
                lastChat.put("lc_content", content);
                lastChat.put("lc_date", String.valueOf(today_time));
                lastChat.put("lc_name", userNickname);

                databaseReference.child("lastChat").child(chatroom_uid).setValue(lastChat); //최근 채팅 목록 업데이트

                inputField.setText(""); //채팅 입력창 초기화

                //adapter.add(newChat);
                rv.scrollToPosition(adapter.getItemCount() - 1); //메세지 보낸 곳으로 포커스 옮김


            } catch (Exception e) {

                e.printStackTrace();
            }
        }


    }

    //채팅 받아오는 로직

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {


        try {
            items.clear();
            for (DataSnapshot child : dataSnapshot.getChildren()) {

                ChatVO temp = child.getValue(ChatVO.class);
                items.add(temp);

            }

            Log.d("기덕", "채팅 받아옴");

            // FIXME: 2017-11-08 채팅을 입력 할때 마다 모든 메세지를 받아 오는 것은 부하가 많이 걸릴 것으로 예상되므로,
            // 이 부분은 차후 최근 업데이트 된 순으로 받아오는 것으로 변경 예정.

            // TODO: 2017-11-10 sqlite 채팅 목록 insert

            database.insert_chat_data(chatroom_uid, items);
            items = database.read_chat_data(chatroom_uid);

            Log.d("기덕", "데이터 받아서 DB에서 다시 받아온 리스트 개수 :  " + items.size());
            adapter.listAdd(items);
            rv.scrollToPosition(adapter.getItemCount() - 1);

        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(this, "채팅 메세지를 불러오는데 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
        this.finish();
    }
}
