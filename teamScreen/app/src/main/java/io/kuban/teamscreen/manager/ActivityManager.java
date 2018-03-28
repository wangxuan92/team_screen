package io.kuban.teamscreen.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import io.kuban.teamscreen.MainActivity;
import io.kuban.teamscreen.R;
import io.kuban.teamscreen.activity.BindingActivity;
import io.kuban.teamscreen.activity.BindingSuccessfulActivity;
import io.kuban.teamscreen.base.BaseCompatActivity;
import io.kuban.teamscreen.model.PadsModel;

/**
 * intent跳转管理
 * Created by wangxuan on 17/1/17.
 */

public class ActivityManager {
    public static final int ALARM_REPEAT_INTERVAL = 10;
    public static final String STARTUP_ACTION_NAME = "visitor_pad";
    public static final String SHAREDPREF_APP_STRING = "io.kuban.visitorpad.manager.sharedpref";
    public static final String SHAREDPREF_RUNNINGTIMECOUNT_STRING = "io.kuban.visitorpad.manager.runningtimecount";

    public static final String APP_PASSWORD = "app_password";
    public static final String CUSTOM_COLOR = "custom_color";
    public static final String TO_KEN = "token";
    public static final String AVATAR = "avatar";
    public static final String FEATURE_DATA = "feature_data";
    public static final String PADS_MODEL = "PadsModel";
    public static final String VISITOR = "Visitor";

    //-----------------------------------------匹配成功页
    public static void startBindingSuccessfulActivity(Activity activity, PadsModel padsModel) {
        Intent intent = new Intent();
        intent.setClass(activity, BindingSuccessfulActivity.class);
        intent.putExtra(PADS_MODEL, padsModel);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
    }

    public static void startInActivity(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, BindingActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
    }

    public static void toLogInActivity(Context context) {
        BaseCompatActivity.AtyContainer.getInstance().finishAllActivity();
        Intent mBootIntent = new Intent(context, BindingActivity.class);
        //下面这句话必须加上才能开机自动运行app的界面
        mBootIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mBootIntent);
        android.os.Process.killProcess(android.os.Process.myPid());  //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
    }

    //-----------------------------------------首页
    public static void startMainActivity(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
    }
}
