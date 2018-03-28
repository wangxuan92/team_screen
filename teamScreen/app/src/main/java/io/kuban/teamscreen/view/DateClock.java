package io.kuban.teamscreen.view;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 显示日期的控件
 * Created by wangxuan on 16/10/25.
 */
public class DateClock extends TextView {
    private final static String TAG = "DigitalClock";

    private Calendar mCalendar;
    private String mFormat = "yyyy-MM-dd";

    private Runnable mTicker;
    private Handler mHandler;

    private boolean mTickerStopped = false;

    public DateClock(Context context) {
        super(context);
        initClock(context);
    }

    public DateClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        initClock(context);
    }

    private void initClock(Context context) {
        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        mTickerStopped = false;
        super.onAttachedToWindow();
        mHandler = new Handler();

        mTicker = new Runnable() {
            public void run() {
                if (mTickerStopped)
                    return;
                Date d1 = new Date(System.currentTimeMillis());
                String dateWeek = getDateWeekString(d1);
                setText(dateWeek);
                invalidate();
                long now = SystemClock.uptimeMillis();
                long next = now + (1000 - System.currentTimeMillis() % 1000);
                // TODO
                mHandler.postAtTime(mTicker, next);
            }
        };
        mTicker.run();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTickerStopped = true;
    }

    public void setFormat(String format) {
        mFormat = format;
    }


    /**
     * example: 7月13日 周三
     *
     * @return
     */
    public static String getDateWeekString(Date start_at) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start_at);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        return String.format(
                "%02d月%02d日\n星期%s",
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH), getChineseNumber((week - 1))
        );
    }

    public static List<String> NUMBER_CHINESE_MAPPING = new ArrayList<>();

    static {
        NUMBER_CHINESE_MAPPING.add("日");
        NUMBER_CHINESE_MAPPING.add("一");
        NUMBER_CHINESE_MAPPING.add("二");
        NUMBER_CHINESE_MAPPING.add("三");
        NUMBER_CHINESE_MAPPING.add("四");
        NUMBER_CHINESE_MAPPING.add("五");
        NUMBER_CHINESE_MAPPING.add("六");
    }

    public static String getChineseNumber(int num) {
        return NUMBER_CHINESE_MAPPING.get(num);
    }

}