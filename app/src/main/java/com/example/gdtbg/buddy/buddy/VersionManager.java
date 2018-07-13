package com.example.gdtbg.buddy.buddy;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;

/**
 * API 버전이 다를 경우, (예를 들면 같은 메소드라도 API 24이하 버전에서는 사용가능, 이상 버전에서는 Deprecated 된 메소드) 버전에 맞춰서
 * 사용 가능하게 하는 메소드를 모아 놓은 클래스. 기본적으로 static 메소드이다.
 */

public class VersionManager {

    public static final Spanned fromHtml(String source) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            // noinspection deprecation
            return Html.fromHtml(source);
        }
        return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
    }



    public static final void ChangeStatusBar(Activity activity){

        if(Build.VERSION.SDK_INT>=21){

            activity.getWindow().setStatusBarColor(Color.parseColor("#636466"));
            //#6d6e70
        }


    }



}
