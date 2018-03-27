package io.kuban.teamscreen.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.Log;
import android.view.View;

import io.leao.codecolors.CodeColors;
import io.leao.codecolors.core.color.CcColorStateList;
import io.leao.codecolors.core.editor.CcEditorSet;

/**
 * 动态的修改xml中的drawable文件的solid颜色
 * Created by wangxuan on 17/12/6.
 */

public class AccentColorUtils {
    public static String TAG = "AccentColorUtils";
    public static int DIFFERENCE = 50;
    public static int DIFFERENCE_20 = 20;

    public static void setPrimaryColor(int resId, int color) {
        CcColorStateList accentColor = CodeColors.getColor(resId);
        CcEditorSet editor = accentColor.set();
        editor.setColor(color);
        editor.submit();
    }

    public static int colorToInt(String color) {
        return Color.parseColor(color);
    }

    public static void buttonColor(View view, int argb) {
        GradientDrawable drawable = (GradientDrawable) view.getBackground();
        drawable.setColor(argb);
    }

    public static void buttonColor(View view, int width, int argb) {
        GradientDrawable drawable = (GradientDrawable) view.getBackground();
        drawable.setColor(argb);
        drawable.setStroke(width, argb);
    }

    public static Drawable drawableColorResId(Context context, int colorResId, int drawableId) {
        DrawableColorChange normalDrawable = new DrawableColorChange(context);
        normalDrawable.setDrawable(drawableId);
        normalDrawable.setColorResId(colorResId);
        return normalDrawable.getColorChangedDrawable();
    }

    /**
     * 改变drawable的Color
     *
     * @param context
     * @param colorResId
     * @param drawableId
     * @return
     */
    public static Drawable drawableColor(Context context, int colorResId, int drawableId) {
        DrawableColorChange normalDrawable = new DrawableColorChange(context);
        normalDrawable.setDrawable(drawableId);
        normalDrawable.setColor(colorResId);
        return normalDrawable.getColorChangedDrawable();
    }

    /**
     * 设置selectorDrawable
     * 只改变default
     *
     * @param context
     * @param colorRes  改变颜色值
     * @param pressedId 按压的状态drawableId
     * @param defaultId 默认的状态drawableId
     * @return
     */
    public static Drawable selectorDrawable(Context context, String colorRes, int pressedId, int defaultId) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, context.getResources().getDrawable(pressedId));
        stateListDrawable.addState(new int[]{}, drawableColor(context, Color.parseColor(colorRes), defaultId));
        return stateListDrawable;
    }

    /**
     * 设置selectorDrawable
     * 包括改变default
     *
     * @param context
     * @param colorRes  改变颜色值
     * @param pressedId 按压的状态drawableId
     * @param defaultId 默认的状态drawableId
     * @return
     */
    public static Drawable selectorDrawableIncluding(Context context, String colorRes, int pressedId, int defaultId) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, drawableColor(context, AccentColorUtils.caculateColor(colorRes, DIFFERENCE), pressedId));
        stateListDrawable.addState(new int[]{}, drawableColor(context, Color.parseColor(colorRes), defaultId));
        return stateListDrawable;
    }

    /**
     * 设置selectorDrawable
     *
     * @param context
     * @param colorRes
     * @param pressedId 按压的状态drawableId
     * @param defaultId 默认的状态drawableId
     * @return
     */
    public static Drawable selectorDrawable(Context context, String colorRes, int pressedId, int defaultId, int enabledId) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, drawableColor(context, AccentColorUtils.caculateColor(colorRes, DIFFERENCE), pressedId));
        //-代表 android:state_enabled="false"
        stateListDrawable.addState(new int[]{-android.R.attr.state_enabled}, context.getDrawable(enabledId));
        stateListDrawable.addState(new int[]{}, drawableColor(context, Color.parseColor(colorRes), defaultId));
        return stateListDrawable;
    }

    /**
     * 计算压下颜色  startGreen改变difference值的色值
     *
     * @param startColor
     * @param difference
     * @return
     */
    public static int caculateColor(String startColor, int difference) {
        int startRed = Integer.parseInt(startColor.substring(1, 3), 16);
        int startGreen = Integer.parseInt(startColor.substring(3, 5), 16);
        int currentBlue = Integer.parseInt(startColor.substring(5, 7), 16);
        Log.e(TAG, "startRed  " + startRed + "  startGreen " + startGreen + " currentBlue  " + currentBlue);
//        int currentRed = startRed - 30;
        if (difference > 0) {
            if (startGreen >= difference) {
                startGreen -= difference;
            } else {
                startGreen += difference;
            }
        } else {
            if (startGreen >= difference) {
                startGreen += difference;
            } else {
                startGreen -= difference;
            }
        }
        if (startGreen >= 255) {
            startGreen = 255;
        } else if (startGreen <= 0) {
            startGreen = 0;
        }
        Log.e(TAG, "startRed  " + startRed + "  startGreen " + startGreen + " currentBlue  " + currentBlue);
        Log.e(TAG, "   " + ("#" + getHexString(startRed) + getHexString(startGreen) + getHexString(currentBlue)));
        return Color.parseColor("#" + getHexString(startRed) + getHexString(startGreen)
                + getHexString(currentBlue));

    }

    public static int caculateTransparentColor(String startColor, int difference) {
        int startAlpha = Integer.parseInt("5d", 16);
        int startRed = Integer.parseInt(startColor.substring(1, 3), 16);
        int startGreen = Integer.parseInt(startColor.substring(3, 5), 16);
        int currentBlue = Integer.parseInt(startColor.substring(5, 7), 16);
        Log.e(TAG, "startRed  " + startRed + "  startGreen " + startGreen + " currentBlue  " + currentBlue);
//        int currentRed = startRed - 30;
        if (difference > 0) {
            if (startGreen >= difference) {
                startGreen -= difference;
            } else {
                startGreen += difference;
            }
        } else {
            if (startGreen >= difference) {
                startGreen += difference;
            } else {
                startGreen -= difference;
            }
        }

        if (startGreen >= 255) {
            startGreen = 255;
        } else if (startGreen <= 0) {
            startGreen = 0;
        }
        Log.e(TAG, "startRed  " + startRed + "  startGreen " + startGreen + " currentBlue  " + currentBlue);
        Log.e(TAG, "   " + ("#" + getHexString(startAlpha) + getHexString(startRed) + getHexString(startGreen) + getHexString(currentBlue)));
        return Color.parseColor("#" + getHexString(startRed) + getHexString(startGreen)
                + getHexString(currentBlue));

    }

    /**
     * 计算从startColor过度到endColor过程中百分比为franch时的颜色值
     *
     * @param startColor 起始颜色 int类型
     * @param endColor   结束颜色 int类型
     * @param franch     franch 百分比0.5
     * @return 返回int格式的color
     */
    public static int caculateColor(int startColor, int endColor, float franch) {
        String strStartColor = "#" + Integer.toHexString(startColor);
        String strEndColor = "#" + Integer.toHexString(endColor);
        return caculateColor(strStartColor, strEndColor, franch);
    }

    /**
     * 计算从startColor过度到endColor过程中百分比为franch时的颜色值
     *
     * @param startColor 起始颜色 （格式#FFFFFFFF）
     * @param endColor   结束颜色 （格式#FFFFFFFF）
     * @param franch     百分比0.5
     * @return 返回String格式的color（格式#FFFFFFFF）
     */
    public static int caculateColor(String startColor, String endColor, float franch) {
        int startAlpha = Integer.parseInt(startColor.substring(1, 3), 16);
        int startRed = Integer.parseInt(startColor.substring(3, 5), 16);
        int startGreen = Integer.parseInt(startColor.substring(5, 7), 16);
//        int startBlue = Integer.parseInt(startColor.substring(7), 16);

        int endAlpha = Integer.parseInt(endColor.substring(1, 3), 16);
        int endRed = Integer.parseInt(endColor.substring(3, 5), 16);
        int endGreen = Integer.parseInt(endColor.substring(5, 7), 16);
//        int endBlue = Integer.parseInt(endColor.substring(7), 16);
        int currentAlpha = (int) ((endAlpha - startAlpha) * franch + startAlpha);
        int currentRed = (int) ((endRed - startRed) * franch + startRed);
        int currentGreen = (int) ((endGreen - startGreen) * franch + startGreen);

//        int currentBlue = (int) ((endBlue - startBlue) * franch + startBlue);
        return Color.parseColor("#" + getHexString(currentAlpha) + getHexString(currentRed)
                + getHexString(currentGreen));

    }

    /**
     * 将10进制颜色值转换成16进制。
     */
    public static String getHexString(int value) {
        String hexString = Integer.toHexString(value);
        if (hexString.length() == 1) {
            hexString = "0" + hexString;
        }
        return hexString;
    }
}
