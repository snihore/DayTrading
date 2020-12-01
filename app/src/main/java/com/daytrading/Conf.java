package com.daytrading;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class Conf {

    private static SharedPreferences getSharedPreference(Context context){
        final String NAME = "SetMarginSharedPreferences";

        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);

        return sharedPreferences;
    }

    public static void setMargin(Context context, int margin){

        if(context == null || margin <= 0){
            Toast.makeText(context, "Can't Set", Toast.LENGTH_SHORT).show();
            return;
        }

        final String MARGIN = "Margin";

        SharedPreferences sharedPreferences = getSharedPreference(context);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(MARGIN, margin);

        editor.commit();

        //GET
        int m = getMargin(context);

        if(m == margin){
            Toast.makeText(context, "Set :)", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Can't Set, Try Again ...", Toast.LENGTH_SHORT).show();
        }
    }

    public static int getMargin(Context context){

        if(context == null){

            return 15; // Default Margin is 15 ...
        }

        final String MARGIN = "Margin";

        SharedPreferences sharedPreferences = getSharedPreference(context);

        int margin = sharedPreferences.getInt(MARGIN, 15);

        return margin;
    }
}
