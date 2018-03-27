package io.kuban.teamscreen.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.IOException;

import io.kuban.teamscreen.CustomerApplication;
import io.kuban.teamscreen.R;
import retrofit2.Response;

/**
 * Created by taohuang on 2/5/17.
 */

public class ErrorUtil {
    private static final Handler MAIN_LOOPER_HANDLER = new Handler(Looper.getMainLooper());
    public static final String ERROR_ = "_error";
    public static final String MESSAGE = "message";

    public static void handleError(final Activity activity, final Response<?> response) {
        MAIN_LOOPER_HANDLER.post(new Runnable() {
            @Override
            public void run() {
                String message = "";
                try {
                    String errorBody = response.errorBody().string();
                    JSONObject jsonObject = new JSONObject(errorBody);
                    JSONObject errorObject = jsonObject.getJSONObject(ERROR_);
                    if (errorObject != null) {
                        message = errorObject.getString(MESSAGE);
                        if (TextUtils.isEmpty(message)) {
                            message = CustomerApplication.getStringResources(R.string.unknown_error);
                        }
                    } else {
                        message = CustomerApplication.getStringResources(R.string.service_error);
                    }
                } catch (Exception e) {
                    message = CustomerApplication.getStringResources(R.string.service_error);
                }
                if (response.code() == 401) {
                    EventBus.getDefault().post(new HttpUnauthorizedEvent(message));
                    return;
                }
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void handleError(final Activity activity, final Throwable t) {
        MAIN_LOOPER_HANDLER.post(new Runnable() {
            @Override
            public void run() {
                if (t instanceof IOException) {
                    Toast.makeText(activity, CustomerApplication.getStringResources(R.string.network_not_connect), Toast.LENGTH_SHORT).show();
                }
                if (t instanceof JsonSyntaxException) {
                    Toast.makeText(activity, CustomerApplication.getStringResources(R.string.data_parsing_errors), Toast.LENGTH_SHORT).show();
                } else {
//                    Tips.showShort(activity, CustomerApplication.getResouceString(R.string.get_verification_code_error_try_again), true);
                    Toast.makeText(activity, CustomerApplication.getStringResources(R.string.service_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Authorization failure
    public static class HttpUnauthorizedEvent {
        public String message;

        public HttpUnauthorizedEvent(String message) {
            this.message = message;
        }
    }
}
