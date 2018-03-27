package io.kuban.teamscreen.utils;

import android.content.res.Resources;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.kuban.teamscreen.CustomerApplication;

/**
 * Created by wangxuan on 17/11/14.
 */

public class StringUtil {
    public static String getString(int stringId, Object... formatArgs) {
        Resources res = CustomerApplication.getContext().getResources();
        return res.getString(stringId, formatArgs);
    }

    /**
     * 字符串补齐位数
     *
     * @param str     原字符串
     * @param length  规定字符串长度
     * @param filling 补齐字符
     * @return
     */
    public static String formatStr(String str, int length, String filling) {

        if (str == null) {
            str = "";
        }
        int strLen = str.getBytes().length;
        if (strLen == length) {
            return str;
        } else if (strLen < length) {
            int temp = length - strLen;
            String tem = "";
            for (int i = 0; i < temp; i++) {
                tem = tem + filling;
            }
            return str + tem;
        } else {
            return str.substring(0, length);
        }

    }

    /**
     * 标记String中指定字符串颜色
     *
     * @param srcString 源字符串
     * @param markStr   需要标记的部分字符串
     * @return 标记完的字符串，切不可对结果toString操作，否则可能导致标记失效
     */
    public static CharSequence markSomeStringColor(String srcString, String markStr, int color) {
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        SpannableStringBuilder builder = new SpannableStringBuilder(srcString);

        int startPos = srcString.indexOf(markStr);
        int endPos = startPos + markStr.length();
        builder.setSpan(colorSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    /**
     * 标记String中指定字符串颜色
     *
     * @param srcString 源字符串
     * @param markStr   需要标记的部分字符串
     * @return 标记完的字符串，切不可对结果toString操作，否则可能导致标记失效
     */
    public static SpannableString markSomeStringColor1(String srcString, String markStr, int color) {
        SpannableString spannableString = new SpannableString(srcString);
        BackgroundColorSpan colorSpan = new BackgroundColorSpan(color);
        spannableString.setSpan(colorSpan, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }


    /**
     * 通过正则表达式的方式获取字符串中指定字符的个数
     *
     * @param text 指定的字符串
     * @return 指定字符的个数
     */
    public static int pattern(String text, String markStr) {
        // 根据指定的字符构建正则
        Pattern pattern = Pattern.compile(markStr);
        // 构建字符串和正则的匹配
        Matcher matcher = pattern.matcher(text);
        int count = 0;
        // 循环依次往下匹配
        while (matcher.find()) { // 如果匹配,则数量+1
            count++;
        }
        return count;
    }

    /**
     * @param str 被查找的字符串
     * @param key 查找字符
     */

    public static List<Integer> searchAllIndex(String str, String key) {
        List<Integer> list = new ArrayList<>();
        int a = str.indexOf(key);//*第一个出现的索引位置
        if (a > -1) {
            list.add(a);
        }
        while (a != -1) {
            a = str.indexOf(key, a + 1);//*从这个索引往后开始第一个出现的位置
            if (a > -1) {
                list.add(a);
            }
        }
        return list;
    }

}
