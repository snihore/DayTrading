package com.daytrading;

import android.content.Context;
import android.text.TextUtils;

import org.w3c.dom.Text;

import java.io.File;

public class Common {

    public static String getAppPath(Context context){

        File dir = new File(String.valueOf(android.os.Environment.getExternalStorageDirectory())+"/DCIM/Screenshots/");

        if(!dir.exists())
            dir.mkdir();
        return dir.getPath()+File.separator;
    }

    public static String excelFileLoc(Context context){

        File dir = new File(String.valueOf(android.os.Environment.getExternalStorageDirectory())+"/DayTrading/");

        if(!dir.exists())
            dir.mkdir();
        return dir.getPath()+File.separator;
    }


}
