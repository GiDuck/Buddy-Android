package com.example.gdtbg.buddy.buddy;

import android.content.Context;

/**
 * Created by gdtbg on 2017-11-23.
 */

public class Tools {

    private Context context;

    public Tools(Context context) {
        this.context = context;
    }

    public Tools() {
    }

    public int FlagTypeCaster(String str) {

        String image = "flag_" + str;
        String package_name = context.getPackageName();

        int resource = context.getResources().getIdentifier(image, "drawable", package_name);

        return resource;




    }
}
