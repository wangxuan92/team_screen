package io.kuban.teamscreen.service;

import android.content.Context;
import android.text.TextUtils;

import java.io.IOException;

import io.kuban.teamscreen.CustomerApplication;
import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * Created by wang on 2016/8/9.
 */

public class CommonHeaderInterceptor implements Interceptor {

    //    private UserManager mUserManager;
//    private SpacesModel spacesModel;
    private Context context;

    public CommonHeaderInterceptor(Context context) {
//        mUserManager = UserManager.getInstance();
        this.context = context;
    }

    public CommonHeaderInterceptor() {
//        mUserManager = UserManager.getInstance();
    }

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {

        Request originRequest = chain.request();
        Request.Builder builder = originRequest
                .newBuilder()
                .addHeader("Accept", "application/json");
        if (!TextUtils.isEmpty(CustomerApplication.spaceId)) {
            builder.addHeader("X-space-id", CustomerApplication.spaceId);
        }
//                .addHeader("X-location-id", CustomerApplication.X_LOCATION_ID);
        if (!TextUtils.isEmpty(CustomerApplication.token)) {
            builder.addHeader("Authorization", " Bearer " + CustomerApplication.token);
        }
//        builder.addHeader("Authorization", " Bearer " + TEST_TOKEN);

        Request newRequest = builder.build();
        return chain.proceed(newRequest);
    }
}
