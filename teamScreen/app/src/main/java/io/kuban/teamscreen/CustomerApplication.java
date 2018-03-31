package io.kuban.teamscreen;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Environment;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;

import com.dariopellegrini.formbuilder.MApplication;
import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.flattener.ClassicFlattener;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.Printer;
import com.elvishew.xlog.printer.file.FilePrinter;
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator;
import com.facebook.stetho.Stetho;

import java.io.File;

import io.kuban.teamscreen.manager.ActivityManager;
import io.kuban.teamscreen.utils.ACache;
import io.kuban.teamscreen.utils.AccentColorUtils;
import io.kuban.teamscreen.utils.FileUtils;
import io.kuban.teamscreen.utils.ScreenUtil;
import io.leao.codecolors.CodeColors;

/**
 * Created by wang on 2016/8/2.
 */

public class CustomerApplication extends MyApplication {
    private static Context context;
    public static String token = "";
    public static String spaceId = "";
    public static String locationId = "";
    public static Typeface typeface;
    public static String appPassword;
    public static String customColor = "#28C97C";

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        ScreenUtil.init(this);
        Stetho.initializeWithDefaults(context);
        //----------------保持长亮
        MApplication.init(this);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        String storageColor = getCache().getAsString(ActivityManager.CUSTOM_COLOR);
        if (!TextUtils.isEmpty(storageColor)) {
            customColor = storageColor;
        }
//        typeface = Typeface.createFromAsset(context.getAssets(), "font/pingfangfine.ttf");
        FileUtils.copyAddrFile(this, FileUtils.ONE);
        FileUtils.copyAddrFile(this, FileUtils.SECOND);
        FileUtils.copyAddrFile(this, FileUtils.THIRD);
        initXlog();
        CodeColors.start(this, new CodeColors.Callback() {
            @Override
            public void onCodeColorsStarted() {
                // Add custom callback adapters (optional).
//                CodeColors.addAttrCallbackAdapter(new CcStatusBarColorAnchorCallbackAdapter());
//                CodeColors.addViewDefStyleAdapter(new CcCoordinatorLayoutDefStyleAdapter());
            }

            @Override
            public void onCodeColorsFailed(Exception e) {
                Log.e("=================", "     " + e);
                // Log exceptions in your tracker.
            }
        });
        primaryColor(customColor);
    }


    /**
     * Initialize XLog
     */
    private void initXlog() {
        LogConfiguration config = new LogConfiguration.Builder()
                .logLevel(BuildConfig.DEBUG
                        ? LogLevel.ALL             // 指定日志级别,日志低于这一水平不会打印出来,默认值:LogLevel.ALL
                        : LogLevel.INFO)
                .tag("KUBAN_LOG")                   // 指定标签,默认:“X-LOG”
                // .t()                                                // 默认情况下启用线程信息,禁用
                // .st(2)                                              // 与深度2启用堆栈跟踪信息,默认禁用
                // .b()                                                // 使边境,默认禁用
                // .jsonFormatter(new MyJsonFormatter())               // 默认值:默认Json格式化程序
                // .xmlFormatter(new MyXmlFormatter())                 // 默认值:默认Xml格式化程序
                // .throwableFormatter(new MyThrowableFormatter())     // 默认值:默认Throwable格式化程序
                // .threadFormatter(new MyThreadFormatter())           // 默认值:默认线程格式化程序
                // .stackTraceFormatter(new MyStackTraceFormatter())   // 默认值:默认的堆栈跟踪格式化器
                // .borderFormatter(new MyBoardFormatter())            // 默认值:违约边界格式化程序
                // .addObjectFormatter(AnyClass.class,                 // 为特定类的对象添加格式化程序
                //     new AnyClassObjectFormatter())                  // 在默认情况下使用Object.toString()
                //TODO: what is blacklist1? should we remove these?
                //.addInterceptor(new BlacklistTagsFilterInterceptor(  // 添加黑名单标签过滤器
                //        "blacklist1", "blacklist2", "blacklist3"))
                // .addInterceptor(new WhitelistTagsFilterInterceptor( // 添加白名单标签过滤器
                //     "whitelist1", "whitelist2", "whitelist3"))
                // .addInterceptor(new MyInterceptor())                // 添加一个记录拦截器
                .build();

        Printer androidPrinter = new AndroidPrinter();             // 使用android.util.Log打印机,打印日志
        Printer filePrinter = new FilePrinter                    // 打印机,打印日志到文件系统
                .Builder(new File(Environment.getExternalStorageDirectory(), "visitorPadlLog").getPath())       // 指定的路径保存日志文件
                .fileNameGenerator(new DateFileNameGenerator())        // Default: ChangelessFileNameGenerator("log")
                // .backupStrategy(new MyBackupStrategy())             // Default: FileSizeBackupStrategy(1024 * 1024)
                .logFlattener(new ClassicFlattener())                  // Default: DefaultFlattener
                .build();

        XLog.init(                                                 // 初始化XLog
                config,                                                // 指定日志配置,如果未指定,将使用新的LogConfiguration.Builder().build()
                androidPrinter,                                        // 指定打印机,如果没有指定打印机,AndroidPrinter(Android)/ ConsolePrinter(java)将被使用。
                filePrinter);
    }


    public static Context getContext() {
        return context;
    }

    public static void primaryColor(String colorRes) {
        AccentColorUtils.setPrimaryColor(R.color.but_background_green, Color.parseColor(colorRes));
        AccentColorUtils.setPrimaryColor(R.color.text_color_green, Color.parseColor(colorRes));
        AccentColorUtils.setPrimaryColor(R.color.but_background_green_pressed, AccentColorUtils.caculateColor(colorRes, AccentColorUtils.DIFFERENCE));
        AccentColorUtils.setPrimaryColor(R.color.but_background_green_pressed_2, AccentColorUtils.caculateColor(colorRes, AccentColorUtils.DIFFERENCE_20));
    }

    public static String getStringResources(int id) {
        return getContext().getResources().getString(id);
    }

    /**
     * @return Global Application-wise cache
     */
    public static ACache getCache() {
        return ACache.get(CustomerApplication.getContext());
    }

    public static Typeface getTypeface() {
        return typeface;
    }

}
