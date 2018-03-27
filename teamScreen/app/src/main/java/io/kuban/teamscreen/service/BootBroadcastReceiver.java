package io.kuban.teamscreen.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import io.kuban.teamscreen.manager.ActivityManager;

/**
 * Created by wangxuan on 17/11/24.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {
            ActivityManager.toLogInActivity(context);
        }
    }

}