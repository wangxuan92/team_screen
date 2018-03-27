package io.kuban.teamscreen.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

/**
 * Created by wangxuan on 17/11/8.
 */

public class NetUtil {
    /**
     * 没有连接网络
     */
    private static final int NETWORK_NONE = -1;
    /**
     * 移动网络
     */
    private static final int NETWORK_MOBILE = 0;
    /**
     * 无线网络
     */
    private static final int NETWORK_WIFI = 1;

    public static int getNetWorkState(Context context) {
        // 得到连接管理器对象
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {

            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                return NETWORK_WIFI;
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                return NETWORK_MOBILE;
            }
        } else {
            return NETWORK_NONE;
        }
        return NETWORK_NONE;
    }


    /**
     * @param ip 检测ip
     *           -c：表示次数，1 为1次
     *           -w：表示deadline, time out的时间，单位为秒，100为100秒。
     *           -i：表示秒数，设定间隔几秒送一个网络封包给一台机器，预设值是一秒送一次。
     * @return 是否ping成功
     */
    public static boolean startPing(String ip) {
        boolean isexist = false;
        Process process = null;

        try {
            process = Runtime.getRuntime().exec("ping -c 1 -i 0.5 -W 1 " + ip);
            int status = process.waitFor();
            if (status == 0) {
                isexist = true;
            } else {
                isexist = false;
            }
        } catch (IOException e) {
            isexist = false;
        } catch (InterruptedException e) {
            isexist = false;
        } finally {
            process.destroy();
        }
        return isexist;
    }


}
