package io.kuban.teamscreen.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.kuban.teamscreen.receiver.NetBroadcastReceiver;
import io.kuban.teamscreen.CustomerApplication;
import io.kuban.teamscreen.R;
import io.kuban.teamscreen.db.UserDBDao;
import io.kuban.teamscreen.dialog.LoadingDialog;
import io.kuban.teamscreen.event.NetworkRequestStatusEvent;
import io.kuban.teamscreen.event.ReturnHomePageEvent;
import io.kuban.teamscreen.manager.ActivityManager;
import io.kuban.teamscreen.manager.KBUploadManager;
import io.kuban.teamscreen.model.UserDB;
import io.kuban.teamscreen.model.UserModel;
import io.kuban.teamscreen.model.VisitsModel;
import io.kuban.teamscreen.service.AlwaysOnService.Bootstrap;
import io.kuban.teamscreen.service.KuBanHttpClient;
import io.kuban.teamscreen.utils.ACache;
import io.kuban.teamscreen.utils.AccentColorUtils;
import io.kuban.teamscreen.utils.ErrorUtil;
import io.kuban.teamscreen.utils.JsonUtils;
import io.kuban.teamscreen.utils.NetUtil;
import io.leao.codecolors.CodeColors;
import io.leao.codecolors.core.CcCore;
import io.leao.codecolors.core.view.CcLayoutInflater;
import retrofit2.Response;

/**
 * Created by wangxuan on 16/9/22.
 */
public class BaseCompatActivity extends FragmentActivity implements NetBroadcastReceiver.NetEvevt {
    public Activity activity;
    public static String TAG;
    public static KuBanHttpClient kuBanHttpClient;
    public UserDBDao mUserDao;
    public static long userDaoId;//记录存入数据库的最新条目id
    public static NetBroadcastReceiver.NetEvevt evevt;
    public static boolean isNetConnect;//判断是否有网络
    /**
     * 网络类型
     */
    private int netMobile;
    public ACache cache;//存储数据
    private CcLayoutInflater mLayoutInflater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //去掉虚拟按键全屏显示
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //让虚拟键盘一直不显示
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        window.setAttributes(params);
//添加Activity到堆栈
        AtyContainer.getInstance().addActivity(this);
        activity = this;
        evevt = this;
        inspectNet();
        isNetConnect = inspectNet();
        CcCore.getActivityManager().onActivityCreated(this);
        super.onCreate(savedInstanceState);
        cache = CustomerApplication.getCache();
        EventBus.getDefault().register(this);
        TAG = "============" + this.getClass().getSimpleName();
        kuBanHttpClient = KuBanHttpClient.getInstance();
        mUserDao = CustomerApplication.getInstances().getDaoSession().getUserDBDao();
        Log.e("============", "######################" + this.getClass().getSimpleName() + "###################");

    }

    private void setStatusBarUpperAPI21() {
        Window window = getWindow();
        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        //由于setStatusBarColor()这个API最低版本支持21, 本人的是15,所以如果要设置颜色,自行到style中通过配置文件设置
//        window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 预留出系统 View 的空间.
            ViewCompat.setFitsSystemWindows(mChildView, true);
        }
    }

    public void showProgressDialog() {
//        StyledDialog.buildLoading().show();
        LoadingDialog.showDialog(this);
    }

    public void hiddenKeyboard(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
    }

    public void dismissProgressDialog() {
        LoadingDialog.dismissDialog();
//        StyledDialog.dismissLoading();
    }

    public void updateButtonColor(ImageView view, String colorRes, int pressedId, int defaultId) {
        view.setImageDrawable(AccentColorUtils.selectorDrawable(CustomerApplication.getContext(), colorRes, pressedId, defaultId));
    }

    public void updateButtonColorIncluding(ImageView view, String colorRes, int pressedId, int defaultId) {
        view.setImageDrawable(AccentColorUtils.selectorDrawableIncluding(CustomerApplication.getContext(), colorRes, pressedId, defaultId));
    }

    public void updateButtonColor(ImageView view, String colorRes, int drawableId) {
        view.setImageDrawable(AccentColorUtils.drawableColor(CustomerApplication.getContext(), Color.parseColor(colorRes), drawableId));
    }

    /**
     * 初始化时判断有没有网络
     */

    public boolean inspectNet() {
        this.netMobile = NetUtil.getNetWorkState(this);
        return isNetConnect();
//        if (netMobile == 1) {
//        } else if (netMobile == 0) {
//        } else if (netMobile == -1) {
//        }
    }

    public void checkDatabase() {
        List<UserDB> userDBs = mUserDao.loadAll();
        if (userDBs.size() > 0) {
            for (UserDB userDB : userDBs) {
                UserModel userModel = new UserModel(userDB.getId(), JsonUtils.toMapObject(userDB.userMap), userDB.visitorsId, userDB.isSignin);
                KBUploadManager.upLoadToken(userModel, true, new File((String) userModel.userMap.get(ActivityManager.AVATAR)));
            }
        }
    }

    /**
     * 网络变化之后的类型
     */
    @Override
    public void onNetChange(int netMobile) {
        // TODO Auto-generated method stub
        this.netMobile = netMobile;
        isNetConnect = isNetConnect();
        if (isNetConnect) {
            checkDatabase();
        }
    }

    /**
     * 判断有无网络 。
     *
     * @return true 有网, false 没有网络.
     */

    public boolean isNetConnect() {
        if (netMobile == 1) {
            return true;
        } else if (netMobile == 0) {
            return true;
        } else if (netMobile == -1) {
            return false;

        }
        return false;
    }


    /**
     * 检测网络是否可用
     *
     * @return
     */
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }


    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
     */
    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;

    public int getNetworkType() {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!TextUtils.isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }

    public void finish(View view) {
        if (null != view) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }

    public void returnHomePage() {
        finish();
        EventBus.getDefault().post(new ReturnHomePageEvent(true));
    }

    @Override
    protected void onResume() {
        CcCore.getActivityManager().onActivityResumed(this);
        super.onResume();
        isNetConnect = inspectNet();
    }

    @Override
    protected void onPause() {
        super.onPause();
        CcCore.getActivityManager().onActivityPaused(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CcCore.getActivityManager().onActivityDestroyed(this);
        AtyContainer.getInstance().removeActivity(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        CcCore.getActivityManager().onConfigurationChanged(newConfig, getResources());
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Object getSystemService(@NonNull String name) {
        Object systemService = super.getSystemService(name);

        if (CodeColors.isActive() && LAYOUT_INFLATER_SERVICE.equals(name)) {
            if (mLayoutInflater == null) {
                LayoutInflater layoutInflater = (LayoutInflater) systemService;
                // Copy the existing layout inflater and clone it to this context.
                // That also allows its factory to be reset.
                mLayoutInflater = CcLayoutInflater.copy(layoutInflater).cloneInContext(this);
            }
            return mLayoutInflater;
        } else {
            return systemService;
        }
    }

    @Subscribe
    public void scanNetworkRequestStatusEvent(NetworkRequestStatusEvent networkRequestStatusEvent) {
        if (networkRequestStatusEvent.isAutomatic()) {
            Response<VisitsModel> response = networkRequestStatusEvent.getVisitsModel();
            Throwable throwable = networkRequestStatusEvent.getThrowable();
            if (null != response) {
                if (response.isSuccessful()) {
                    mUserDao.deleteByKey(networkRequestStatusEvent.getDbID());
                } else {
//                    ErrorUtil.handleError(this, response);
                }
            } else if (null != throwable) {
//                ErrorUtil.handleError(this, throwable);
            }
        }
        List<UserDB> userDBs = mUserDao.loadAll();
        Log.e(TAG, "提交完成，本地数据库剩余数据" + userDBs.size());
    }

    public static class AtyContainer {

        private AtyContainer() {
        }

        private static AtyContainer instance = new AtyContainer();
        private static List<Activity> activityStack = new ArrayList<>();

        public static AtyContainer getInstance() {
            return instance;
        }

        public void addActivity(Activity aty) {
            activityStack.add(aty);
        }

        public void removeActivity(Activity aty) {
            activityStack.remove(aty);
        }

        /**
         * 结束所有Activity
         */
        public void finishAllActivity() {
            for (int i = 0, size = activityStack.size(); i < size; i++) {
                if (null != activityStack.get(i)) {
                    activityStack.get(i).finish();
                }
            }
            activityStack.clear();
        }

    }

    @Subscribe
    public void onAuthFailure(ErrorUtil.HttpUnauthorizedEvent event) {
        cache.remove(ActivityManager.TO_KEN);
        cache.remove(ActivityManager.PADS_MODEL);
        cache.remove(ActivityManager.APP_PASSWORD);
        Bootstrap.stopAlwaysOnService(this);
        finish();
        ActivityManager.startInActivity(activity);
    }
}
