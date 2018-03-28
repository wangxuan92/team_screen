package io.kuban.teamscreen.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.view.WindowManager;

import java.net.NetworkInterface;
import java.net.SocketException;

/**
 * Created by wangxuan on 17/11/15.
 */

public class EquipmentInformationUtil {
    public static final String MODEL = "model";//型号
    public static final String MANUFACTURER = "manufacturer";//制造商
    public static final String BRAND = "brand";//品牌
    public static final String RELEASE = "release";//系统版本号

    //    String mac = EquipmentInformationUtil.getMacAddress();
//    String macValue = String.valueOf(Long.parseLong(mac, 16));
//    String deviceCode = macValue.substring(macValue.length() - 6);
    public static String getMacAddress() {
 /*获取mac地址有一点需要注意的就是android 6.0版本后，以下注释方法不再适用，不管任何手机都会返回"02:00:00:00:00:00"这个默认的mac地址，这是googel官方为了加强权限管理而禁用了getSYstemService(Context.WIFI_SERVICE)方法来获得mac地址。*/
        //        String macAddress= "";
//        WifiManager wifiManager = (WifiManager) MyApp.getContext().getSystemService(Context.WIFI_SERVICE);
//        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        macAddress = wifiInfo.getMacAddress();
//        return macAddress;

        String macDefault = "02:00:00:00:00:02";
        String macAddress = null;
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return macDefault.replace(":", "");
            }
            byte[] addr = networkInterface.getHardwareAddress();
            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macAddress = buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return macDefault.replace(":", "");
        }
        return macAddress.replace(":", "");
    }

    public static String getVersionName(Context context) {
        String result = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            result = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int getVersionCode(Context context) {
        int result = 0;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            result = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    //    设备ID
    public static String getDeviceId(Context context) {
//        return Settings.Secure.getString(context.getContentResolver(),
//                Settings.Secure.ANDROID_ID);
        return "er35geerf2d5s2grd";
    }

    public static String getDeviceInformation(String returnType) {
        Build bd = new Build();
        if (returnType.equals(MODEL)) {
            return bd.MODEL;
        } else if (returnType.equals(MANUFACTURER)) {
            return bd.MANUFACTURER;
        } else if (returnType.equals(BRAND)) {
            return bd.BRAND;
        } else if (returnType.equals(RELEASE)) {
            return Build.VERSION.RELEASE;
        }
        return "";
    }

    public static String getScreenSize(Activity activity) {
        WindowManager wm = activity.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        return width + "x" + height;
    }
}
