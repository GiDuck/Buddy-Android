package com.example.gdtbg.buddy.buddy.Services;

import android.content.Context;
import android.content.Intent;

/**
 * Created by gdtbg on 2017-11-22.
 */

public class ReciverManager {

    Intent[] intentArray;
    Context context;

    public ReciverManager(Context context) {

        this.context = context;
        intentArray = new Intent[]{new Intent(context, ActivityReciver.class),
                new Intent(context, ChatReciver.class),
                new Intent(context, PostReciver.class)};

    }

    public ReciverManager() {
    }

    public void startServices() {


        for (int i = 0; i < intentArray.length; i++) {

            context.startService(intentArray[i]);

        }


    }

    public void stopServices(){


        for (int i = 0; i < intentArray.length; i++) {

            context.stopService(intentArray[i]);

        }

    }


}
