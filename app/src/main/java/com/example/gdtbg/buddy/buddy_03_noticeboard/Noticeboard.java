package com.example.gdtbg.buddy.buddy_03_noticeboard;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.gdtbg.buddy.R;
import com.example.gdtbg.buddy.buddy.CustomActivity;

public class Noticeboard extends CustomActivity {

    WebView webView;
    private Handler mHandler;
    private boolean mFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buddy03_noticeboard);
        super.setMenuBar(); //Custom Activity의 setMenuBar 메소드를 호출 해 툴바와 액션바를 부착시킨다.
        actionbar_title.setText("홍보 게시판"); //액션바에 이름 세팅


        webView = (WebView)findViewById(R.id.noticeboard_webview);
        WebSettings webSettings = webView.getSettings();
        webView.setWebViewClient(new WebClient());
        webView.getSettings().setSupportZoom(true);//돋보기버튼 설정
        webView.getSettings().setUseWideViewPort(false);// 자동개행
        webView.getSettings().setJavaScriptEnabled(true);//자바스크립트 가능 설정
        webView.loadUrl("https://www.facebook.com/bufs.lounge/");

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 0) {
                    mFlag = false;
                }
            }
        };


    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // 백 키를 터치한 경우
        if(keyCode == KeyEvent.KEYCODE_BACK){

            // 이전 페이지를 볼 수 있다면 이전 페이지를 보여줌
            if(webView.canGoBack()){
                webView.goBack();
                return false;
            }

            // 이전 페이지를 볼 수 없다면 백키를 한번 더 터치해서 종료
            else {
                if(!mFlag) {
                    Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 어플리케이션으로 복귀합니다.", Toast.LENGTH_SHORT).show();
                    mFlag = true;
                    mHandler.sendEmptyMessageDelayed(0, 2000); // 2초 내로 터치시
                    return false;
                } else {
                    finish();
                }
            }
        }

        return super.onKeyDown(keyCode, event);
    }



}
