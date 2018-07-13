package com.example.gdtbg.buddy.buddy.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by gdtbg on 2017-11-10.
 */



public class DBHelper extends SQLiteOpenHelper {

    //public static final String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "buddy";


    public DBHelper(Context context) {
        super(context, "buddy", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE IF NOT EXISTS " + "CHAT" + "(" +
                "USER_UID " + "TEXT NOT NULL," +
                "CHATROOM_UID " + "TEXT NOT NULL," +
                "CONTENT " + "TEXT," +
                "CHAT_TIME " + "REAL NOT NULL" + ");";
        Log.d("기덕", "DB HELPER 테이블 생성 쿼리 확인 : " + query);

        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
