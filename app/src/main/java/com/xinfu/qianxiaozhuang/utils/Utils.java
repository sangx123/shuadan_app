package com.xinfu.qianxiaozhuang.utils;

import android.content.Context;
import android.support.annotation.ArrayRes;

import com.xinfu.qianxiaozhuang.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Utils {
    public static ArrayList<String> result(Context context,@ArrayRes int id) {
        return (ArrayList<String>) Arrays.asList(context.getResources().getStringArray(id));
    }

    /**
     * 日期格式字符串转换成时间戳
     * @param date 字符串日期
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date getDate(String date_str, String format){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date_str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
