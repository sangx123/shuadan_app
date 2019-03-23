package com.xinfu.qianxiaozhuang.utils;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Spannable工具类，用于设置文字的前景色、背景色、Typeface、粗体、斜体、字号、超链接、删除线、下划线、
 * Created by FanBei on 2016/4/10.
 */
public class SpannableUtils {

    public SpannableUtils() {

    }

    /**
     * 改变字符串中某一段文字的字号
     * setTextSize("",24,0,2) = null;
     * setTextSize(null,24,0,2) = null;
     * setTextSize("abc",-2,0,2) = null;
     * setTextSize("abc",24,0,4) = null;
     * setTextSize("abc",24,-2,2) = null;
     * setTextSize("abc",24,0,2) = normal string
     */
    public static SpannableString setTextSize(String content, int startIndex, int endIndex, int fontSize) {
        if (TextUtils.isEmpty(content) || fontSize <= 0 || startIndex >= endIndex || startIndex < 0 || endIndex >= content.length()) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new AbsoluteSizeSpan(fontSize), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    public static SpannableString setTextSub(String content, int startIndex, int endIndex) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new SubscriptSpan(), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    public static SpannableString setTextSuper(String content, int startIndex, int endIndex) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new SuperscriptSpan(), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    public static SpannableString setTextStrikethrough(String content, int startIndex, int endIndex) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new StrikethroughSpan(), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    public static SpannableString setTextUnderline(String content, int startIndex, int endIndex) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new UnderlineSpan(), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    public static SpannableString setTextBold(String content, int startIndex, int endIndex) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    public static SpannableString setTextItalic(String content, int startIndex, int endIndex) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    public static SpannableString setTextBoldItalic(String content, int startIndex, int endIndex) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    public static SpannableString setTextForeground(String content, int startIndex, int endIndex, int foregroundColor) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex > content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new ForegroundColorSpan(foregroundColor), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    public static SpannableString setTextBackground(String content, int startIndex, int endIndex, int backgroundColor) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new BackgroundColorSpan(backgroundColor), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    /**
     * 设置文本的超链接
     *
     * @param content    需要处理的文本
     * @param startIndex
     * @param endIndex   被处理文本中需要处理字串的开始和结束索引
     * @param url        文本对应的链接地址，需要注意格式：
     *                   （1）电话以"tel:"打头，比如"tel:02355692427"
     *                   （2）邮件以"mailto:"打头，比如"mailto:zmywly8866@gmail.com"
     *                   （3）短信以"sms:"打头，比如"sms:02355692427"
     *                   （4）彩信以"mms:"打头，比如"mms:02355692427"
     *                   （5）地图以"geo:"打头，比如"geo:68.426537,68.123456"
     *                   （6）网络以"http://"打头，比如"http://www.google.com"
     */
    public static SpannableString setTextURL(String content, int startIndex, int endIndex, String url) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new URLSpan(url), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    public static SpannableString setTextImg(String content, int startIndex, int endIndex, Drawable drawable) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new ImageSpan(drawable), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }


    /**
     * 截取字符串中的数字
     * 仅对字符串中数字进行变化
     *
     * @param content
     * @param textView
     */
    public static String getNumbers(String content, TextView textView, ColorStateList color) {
        if (TextUtils.isEmpty(content) || textView == null) {
            return null;
        }
        int indexstart;//截取数字的起始下标
        int indexend;//截取出的数字的长度
        ArrayList<Integer> indexStart = new ArrayList<Integer>();//一个字符串中 数字下标的集合
        ArrayList<Integer> indexEnd = new ArrayList<Integer>();//一个字符串中 数字长度的集合
        Pattern pattern = Pattern.compile("[0-9\\.]+");
        Matcher matcher = pattern.matcher(content);
        if (indexStart.size() > 0) {
            indexStart.clear();
            indexEnd.clear();
        }
        while (matcher.find()) {
            indexstart = content.indexOf(matcher.group());//截取到的数字开始的下标
            indexend = indexstart + matcher.group().length();//截取到的数字结束的下标  长度
            indexStart.add(indexstart);
            indexEnd.add(indexend);
        }
        SpannableStringBuilder spannableString = new SpannableStringBuilder(content);
        if (indexStart.size() > 0) {//字符串中有数字
            for (int i = 0; i < indexStart.size(); i++) {
                spannableString.setSpan(new TextAppearanceSpan(null, 0, 0,color, null), indexStart.get(i), indexEnd.get(i), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            }
            textView.setText(spannableString);
        } else {//字符串中无数字时 显示字符串
            textView.setText(content);
        }
        return content;
    }
}
