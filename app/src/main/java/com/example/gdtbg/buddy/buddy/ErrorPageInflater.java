package com.example.gdtbg.buddy.buddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gdtbg.buddy.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by gdtbg on 2017-11-22.
 */

public class ErrorPageInflater {

    Context context;

    public ErrorPageInflater() {
    }

    public ErrorPageInflater(Context context) {
        this.context = context;
    }
    /*------------------- 에러 페이지를 띄워주는 메소드 -------------------*/
    // 레이아웃 인플레이터를 사용해 기존에 인플레이트 된 ViewGroup 안에 있는 View들을 모두 제거하고,
    // 에러 페이지를 띄운 리니어 레이아웃을 대신 추가한다.


    public final void setErrorPage(ViewGroup root) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        root.removeAllViews();
        LinearLayout errorpage = (LinearLayout) inflater.inflate(R.layout.buddy_errorpage, root, false);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        errorpage.setLayoutParams(param);
        root.addView(errorpage);


    }

    //필요 없는 뷰들만 제거하는 메소드

    public final void setErrorPage(ViewGroup root, View[] removeChildren) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        for (View removeChild : removeChildren) {
            root.removeView(removeChild);
        }
        LinearLayout errorpage = (LinearLayout) inflater.inflate(R.layout.buddy_errorpage, root, false);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        errorpage.setLayoutParams(param);
        root.addView(errorpage);

    }

    //뷰 하나만 제거 하는 메소드

    public final void setErrorPage(ViewGroup root, View removeChild) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

        RelativeLayout errorpage = (RelativeLayout) inflater.inflate(R.layout.buddy_errorpage, root, false);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        errorpage.setLayoutParams(param);
        root.removeView(removeChild);
        root.addView(errorpage);


    }

    //에러메세지의 내용을 원하는 문자열로 바꿔주면서 인플레이트 시키는 메소드

    public final void setErrorPage(ViewGroup root, View removeChild, String errorMessage) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

        RelativeLayout errorpage = (RelativeLayout) inflater.inflate(R.layout.buddy_errorpage, root, false);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        errorpage.setLayoutParams(param);

        root.removeView(removeChild);
        root.addView(errorpage);
        TextView errorText = (TextView) errorpage.findViewById(R.id.errorText);
        errorText.setText(errorMessage);


    }


}
