package com.example.gdtbg.buddy.buddy.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.gdtbg.buddy.VO.ChatVO;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by gdtbg on 2017-11-10.
 */

public class Database {

    DBHelper helper;
    SQLiteDatabase sqLiteDatabase;
    ArrayList<ChatVO> chats;
    Context context;

    public Database(Context context, DBHelper helper) {
        this.context = context;
        this.helper = helper;
        chats = new ArrayList<>();
        sqLiteDatabase = helper.getWritableDatabase();
        //delete_chat_table();
        //create_chat_table();


    }


    public void init_DB() {

        sqLiteDatabase = null;
        File file = new File(context.getFilesDir(), "buddy.db");
        Log.d("기덕", "SQLITE FILE PATH : " + file.toString());
        Log.d("기덕", "SQLITE FILE PATH : " + context.getDir("", 0));


        try {
            sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(file, null);

        } catch (SQLiteException sql) {

            sql.printStackTrace();
            Log.d("기덕", "SQLITE DB 생성 실패 : " + file.getAbsolutePath());

        }

    }


    public void create_chat_table() {

        if (sqLiteDatabase != null) {
            try {

                String query = "CREATE TABLE IF NOT EXISTS " + "CHAT" + "(" +
                        "USER_UID " + "TEXT NOT NULL," +
                        "CHATROOM_UID " + "TEXT NOT NULL," +
                        "CONTENT " + "TEXT," +
                        "CHAT_TIME " + "REAL NOT NULL" + ");";

                Log.d("기덕", "테이블 생성 쿼리 확인 : " + query);


                sqLiteDatabase.execSQL(query);


            } catch (SQLiteException sql) {

                Log.d("기덕", "CREATE TABLE 쿼리 실패 : " + sql.getMessage());


            }
        }

    }

    // FIXME: 2017-11-11 이 부분 수정 해야 함
    public ArrayList<ChatVO> read_chat_data(String chatroom_uid) {

        ArrayList<ChatVO> chats = new ArrayList<>();

        if (sqLiteDatabase == null) {
            Log.d("기덕", "sqlite 객체가 null임");

        }

        if (sqLiteDatabase != null) {

            try {
                String query = "SELECT * FROM " + " CHAT " +
                        "WHERE CHATROOM_UID = " + "'" + chatroom_uid + "'";

              /*  String query = "SELECT * FROM " + "CHAT";*/

                // + " ORDER BY CHAT_TIME"; //DESC = 내림차순, ASC = 오름차순
                Log.d("기덕", "SELECT 쿼리 확인 : " + query);
                Cursor cursor = null;

                cursor = sqLiteDatabase.rawQuery(query, null);
                int count = 0;
                while (cursor.moveToNext()) {

                    ChatVO chat = new ChatVO();
                    chat.setUser_uid(cursor.getString(0));
                    Log.d("기덕", "가져온 유저 uid : " + cursor.getString(0));
                    chat.setContent(cursor.getString(2));
                    Log.d("기덕", "가져온 내용 : " + cursor.getString(2));
                    chat.setTime(cursor.getLong(3));
                    Log.d("기덕", "가져온 시간 : " + cursor.getLong(3));
                    chats.add(chat);
                    Log.d("기덕", "SELECT COUNT ----------- " + count + " 번째----------------");
                    count++;

                }
                count = 0;


            } catch (SQLiteException sql) {
                Log.d("기덕", "SELECT 쿼리 실패 : " + sql.getMessage());
                return null;

            }
            Log.d("기덕", "SQLITE 받아온 채팅 리스트 수 : " + chats.size());

        }
        return chats;
    }


    public void insert_chat_data(String chatroom_uid, ArrayList<ChatVO> chats) {

        if (sqLiteDatabase != null) {
            try {
                for (ChatVO chat : chats) {

                    // FIXME: 2017-11-11 DUAL 때문에 안되는 듯
                    String query = "INSERT INTO " + "CHAT " + "(USER_UID, CHATROOM_UID, CONTENT, CHAT_TIME)" + " SELECT " +
                            "'" + chat.getUser_uid() + "'," +
                            "'" + chatroom_uid + "'," +
                            "'" + chat.getContent() + "'," +
                            +chat.getTime() +
                            " WHERE NOT EXISTS ( SELECT * FROM CHAT WHERE CONTENT = '" + chat.getContent() + "'" +
                            "AND USER_UID=" + "'" + chat.getUser_uid() + "'" + " AND CHAT_TIME = " + chat.getTime() + ")";

                   /* String query = "INSERT INTO " + "CHAT" +
                            "(USER_UID, CHATROOM_UID, CONTENT, CHAT_TIME)" +
                            " VALUES (" +
                            "'" + chat.getUser_uid() + "'," +
                            "'" + chatroom_uid + "'," +
                            "'" + chat.getContent() + "'," +
                            + chat.getSeq() +
                            ")";*/

                    Log.d("기덕", "INSERT 쿼리 확인 : " + query);
                    sqLiteDatabase.execSQL(query);
                }
            } catch (SQLiteException sql) {

                Log.d("기덕", "INSERT 쿼리 실패 : " + sql.getMessage());


            }
        }

    }


    public void delete_chat_table() {

        if (sqLiteDatabase != null) {

            try {
                String query = "DROP TABLE " + "CHAT";
                sqLiteDatabase.execSQL(query);

                Log.d("기덕", "TABLE DELETE 쿼리  : " + query);


            } catch (SQLiteException sql) {

                Log.d("기덕", "TABLE DELETE 쿼리 실패 : " + sql.getMessage());


            }

        }


    }


}
