package com.example.gdtbg.buddy.buddy.Database;

import android.util.Log;

import com.example.gdtbg.buddy.VO.MainboardVO;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;


/**
 * Created by gdtbg on 2017-11-22.
 */

public class TransactionHelper {

    public TransactionHelper() {
    }

    /*------------------- Like 버튼을 클릭했을때 트랜잭션을 사용하여 DB에 데이터를 삽입하는 메소드 -------------------*/

    public final void onLikeClicked(final DatabaseReference postRef, final String user_uid) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                MainboardVO mainboardVO = mutableData.getValue(MainboardVO.class);

                if (mainboardVO == null) {
                    return Transaction.success(mutableData);
                }

                if (mainboardVO.likes.containsKey(user_uid)) {
                    // Unstar the post and remove self from stars
                    mainboardVO.likeCount = mainboardVO.likeCount - 1;
                    mainboardVO.likes.remove(user_uid);
                } else {
                    // Star the post and add self to stars
                    mainboardVO.likeCount = mainboardVO.likeCount + 1;
                    mainboardVO.likes.put(user_uid, true);
                }


                // Set value and report transaction success
                mutableData.setValue(mainboardVO);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d("기덕", "postTransaction:onComplete:" + databaseError);
            }
        });
    }

}
