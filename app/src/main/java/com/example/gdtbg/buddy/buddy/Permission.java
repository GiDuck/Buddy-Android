package com.example.gdtbg.buddy.buddy;

import android.Manifest;
import android.content.Context;
import android.widget.Toast;

import com.gun0912.tedpermission.*;
import com.gun0912.tedpermission.PermissionListener;

import java.util.ArrayList;

/**
 * Created by gdtbg on 2017-11-22.
 */

public class Permission {

    private Context context;

    public Permission(Context context) {
        this.context = context;
    }

    public Permission() {
    }

    public void checkPermission(){

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(context, "권한 확인", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                String str=null;
                for(String message : deniedPermissions){

                    str += ", "+message;

                }
                Toast.makeText(context, str + " 권한이 거부 되었습니다.", Toast.LENGTH_SHORT).show();
            }

        };

        TedPermission.with(context)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("만약 권한을 설정 하지 않는다면 서비스 이용에 제한이 있을 수 있습니다.\n\n권한을 설정해 주세요 [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();


    }
}
