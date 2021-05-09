package com.daytrading;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class Conf {

    private static SharedPreferences getRPTSharedPreference(Context context){

        final String NAME = "RPTSharedPreferences";

        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);

        return sharedPreferences;
    }


    private static SharedPreferences getSharedPreference(Context context){
        final String NAME = "SetMarginSharedPreferences";

        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);

        return sharedPreferences;
    }

    private static SharedPreferences getLotSizeSharedPreference(Context context){

        final String NAME = "LotSizeSharedPreferences";

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

    public static void setRPT(Context context, float rpt){

        if(context == null || rpt <= 0.0f){
            return;
        }

        SharedPreferences sharedPreferences =  getRPTSharedPreference(context);

        if(sharedPreferences == null){
            return;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(editor == null){
            return;
        }

        editor.putFloat("RPT", rpt);

        editor.commit();

    }

    public static void setLotSize(Context context, int lotSize){

        if(context == null || lotSize <= 0){
            return;
        }

        SharedPreferences sharedPreferences =  getLotSizeSharedPreference(context);

        if(sharedPreferences == null){
            return;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(editor == null){
            return;
        }

        editor.putInt("LOTSIZE", lotSize);

        editor.commit();

    }

    public static void setCapital(Context context, long capital){

        if(context == null || capital <= 0){
            return;
        }

        SharedPreferences sharedPreferences =  getRPTSharedPreference(context);

        if(sharedPreferences == null){
            return;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(editor == null){
            return;
        }

        editor.putLong("CAPITAL", capital);

        editor.commit();

    }

    public static long getCapital(Context context){
        if(context == null){
            return 0;
        }

        SharedPreferences sharedPreferences = getRPTSharedPreference(context);

        if(sharedPreferences == null){
            return 0;
        }

        return sharedPreferences.getLong("CAPITAL", 0);
    }

    public static float getRPT(Context context){
        if(context == null){
            return 0.0f;
        }

        SharedPreferences sharedPreferences = getRPTSharedPreference(context);

        if(sharedPreferences == null){
            return 0.0f;
        }

        return sharedPreferences.getFloat("RPT", 0.0f);
    }

    public static int getLotSize(Context context){
        if(context == null){
            return 0;
        }

        SharedPreferences sharedPreferences = getLotSizeSharedPreference(context);

        if(sharedPreferences == null){
            return 0;
        }

        return sharedPreferences.getInt("LOTSIZE", 0);
    }

}
