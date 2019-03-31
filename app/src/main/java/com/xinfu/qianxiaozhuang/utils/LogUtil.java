package com.xinfu.qianxiaozhuang.utils;

import android.util.Log;

/**
 * 10/08/2017  4:06 PM
 * Created by Zhang.
 */

public class LogUtil {


//    public static boolean showLog = BuildConfig.DEBUG;
    public static boolean showLog = true;
    public static final int maxLog = 2000;

    public static void i(String tag, String log) {
        if (showLog)
            Log.i(tag, log);
    }

    public static void w(String tag, String log) {
        if (showLog)
            Log.w(tag, log);
    }

    public static void e(String tag, String log) {
        if (showLog) {
            if(log.length() > maxLog) {
                for(int i=0;i<log.length();i+=maxLog){
                    if(i+maxLog<log.length())

                        Log.e(tag,log.substring(i, i+maxLog));
                    else
                        Log.e(tag,log.substring(i, log.length()));
                }
            } else
                Log.e(tag,log);
        }
//            Log.e(tag, log);
    }
}
