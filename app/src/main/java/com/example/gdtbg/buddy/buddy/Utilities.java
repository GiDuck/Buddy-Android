package com.example.gdtbg.buddy.buddy;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.gdtbg.buddy.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;

/**
 * 반복적으로 생성해야 하는 객체와 주요 로직이 아닌, 서브 기능만을 하는 메소드를 모아 놓은 클래스이다.
 * 처음 시작할때 인스턴스를 static 으로 한 번 생성한다.
 * context를 받아서 참조 해야 하는 메소드들은 여기에 있음.
 */

public final class Utilities {



    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aaa");
    //SimpleDateFormat 인스턴스

    public static final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public static final DatabaseReference databaseReference = firebaseDatabase.getReference();
    //firebase Database 접근을 위한 인스턴스

    public final static FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    public final static StorageReference storageReference = firebaseStorage.getReference();

    private Context context;

    public Utilities(Context context) {
        this.context = context;
    }

    public Utilities() {
    }

    public static void NotiMaker(Intent intent, Context context, String content) {

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notiManger = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification noti = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            noti = new Notification.Builder(context, "buddy")
                    .setContentTitle("Buddy")
                    .setContentText(content)
                    .setSmallIcon(R.drawable.bufs_mini_logo1)
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .setColor(context.getResources().getColor(R.color.bufsYellow))
                    .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                    .build();

        } else {

            noti = new Notification.Builder(context)
                    .setContentTitle("Buddy")
                    .setContentText(content)
                    .setSmallIcon(R.drawable.bufs_mini_logo1)
                    .setTicker("Buddy좀 보세요!!")
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                    .build();

        }

        noti.flags = Notification.FLAG_AUTO_CANCEL;
        notiManger.notify("buddy", 8134, noti);


    }



    public static void NotiMaker(Context context, String content) {

        NotificationManager notiManger = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification noti = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            noti = new Notification.Builder(context, "buddy")
                    .setContentTitle("Buddy")
                    .setContentText(content)
                    .setSmallIcon(R.drawable.bufs_mini_logo1)
                    .setWhen(System.currentTimeMillis())
                    .setColor(context.getResources().getColor(R.color.bufsYellow))
                    .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                    .build();

        } else {

            noti = new Notification.Builder(context)
                    .setContentTitle("Buddy")
                    .setContentText(content)
                    .setSmallIcon(R.drawable.bufs_mini_logo1)
                    .setTicker("Buddy좀 보세요!!")
                    .setWhen(System.currentTimeMillis())
                    .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                    .build();

        }

        noti.flags = Notification.FLAG_AUTO_CANCEL;
        notiManger.notify("buddy", 8134, noti);


    }



    /*------------------- 문자열에 색을 입혀주는 메소드 (HTML 태그 이용) -------------------*/

    public final String ColorMaker(String str) {

        String changed_str = "<font color=\'" + context.getResources().getString(R.string.more_text_color) + "\'>" + str + "</font>";

        return changed_str;

    }

    /*------------------- 현재 시간을 밀리세컨드 단위로 반환하는 메소드 (1970년 1월 1일부터) -------------------*/

    public static final Long CurrentLocalTime() {

        return System.currentTimeMillis();
    }

    /*------------------- Like 버튼을 클릭했을때 트랜잭션을 사용하여 DB에 데이터를 삽입하는 메소드 -------------------*/





           /*------------------- 가입시 스피너에서 선택한 부분 문자열로 리턴 -------------------*/

    public static String CheckPickedLanguage(int position) {

        String Lng = null;

        switch (position) {
            case 0:
                Lng = "english";
                break;
            case 1:
                Lng = "japan";
                break;
            case 2:
                Lng = "china";
                break;
            case 3:
                Lng = "germany";
                break;
            case 4:
                Lng = "france";
                break;
            case 5:
                Lng = "italy";
                break;
            case 6:
                Lng = "spain";
                break;
            case 7:
                Lng = "portugal";
                break;
            case 8:
                Lng = "russia";
                break;
            case 9:
                Lng = "turkey";
                break;
            case 10:
                Lng = "thailand";
                break;
            case 11:
                Lng = "laos";
                break;
            case 12:
                Lng = "malaysia";
                break;
            case 13:
                Lng = "indonesia";
                break;
            case 14:
                Lng = "indonesia";
                break;
            case 15:
                Lng = "vietnam";
                break;
            case 16:
                Lng = "cambodia";
                break;
            case 17:
                Lng = "myanmar";
                break;
            case 18:
                Lng = "india";
                break;
            default:
                Lng = "notfound";
        }

        return Lng;


    }


}
