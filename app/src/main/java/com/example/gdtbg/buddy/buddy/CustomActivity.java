package com.example.gdtbg.buddy.buddy;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gdtbg.buddy.R;
import com.example.gdtbg.buddy.buddy_02_mainboard.Mainboard;
import com.example.gdtbg.buddy.buddy_03_noticeboard.Noticeboard;
import com.example.gdtbg.buddy.buddy_05_chatroom.Chatroom;
import com.example.gdtbg.buddy.buddy_06_friendList.FriendList;
import com.example.gdtbg.buddy.buddy_08_myinfo.MyInfo;
import com.example.gdtbg.buddy.buddy_09_myActivityLog.MyActivity_Main;

import static com.example.gdtbg.buddy.buddy.VersionManager.ChangeStatusBar;

/**
 * Navigation Drawer를 사용하기 위해 상속 하는 액티비티
 */

public class CustomActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    //네비게이션 드로어
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;

    //상단에 나타나는 툴바와 액션바
    protected Toolbar toolbar;
    protected ActionBar actionBar;

    //액션바 안에 있는 문자열
    protected TextView actionbar_title;



    //툴바와 액션바를 붙여 주는 메소드
    protected void setMenuBar() {

        ChangeStatusBar(this); //상태바 색상 바꿔주는 메소드
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundResource(R.color.bufsGray);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(9);
        actionBar.setTitle("");
        actionbar_title = (TextView) findViewById(R.id.actionbar_title);

        //레이아웃 xml에서 뷰그룹, 뷰를 찾아옴
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(this);

    }



    /*------------------- 네비게이션 드로어에서 아이템을 선택 했을 경우 호출 되는 메소드 -------------------*/

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        drawerLayout.closeDrawers();
        Intent intent;
        int id = item.getItemId();

        switch (id) {

            case R.id.navigation_item_mainboard:

                intent = new Intent(CustomActivity.this, Mainboard.class);
                startActivity(intent);
                break;

            case R.id.navigation_item_noticeboard:
                intent = new Intent(CustomActivity.this, Noticeboard.class);
                startActivity(intent);
                break;

            case R.id.navigation_item_chatroom:
                intent = new Intent(CustomActivity.this, Chatroom.class);
                startActivity(intent);
                break;

            case R.id.navigation_item_friendlist:
                intent = new Intent(CustomActivity.this, FriendList.class);
                startActivity(intent);
                break;

            case R.id.navigation_item_myinfo:
                intent = new Intent(CustomActivity.this, MyInfo.class);
                startActivity(intent);
                break;

            case R.id.navigation_item_activity_log:
                intent = new Intent(CustomActivity.this, MyActivity_Main.class);
                startActivity(intent);
                break;


            case R.id.navigation_item_logout:
                Toast.makeText(CustomActivity.this, "로그아웃", Toast.LENGTH_SHORT).show();
                break;

        }

        return true;
    }

    /*-------------------메뉴 레이아웃을 inflate 시켜 주는 메소드 -------------------*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /*------------------- 액션바에서 아이템을 선택 하였을 경우 -------------------*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.action_setting:
                return true;

        }

        return super.onOptionsItemSelected(item);
    }



}
