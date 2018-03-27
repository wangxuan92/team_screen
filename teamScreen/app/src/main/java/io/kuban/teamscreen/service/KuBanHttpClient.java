package io.kuban.teamscreen.service;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import io.kuban.teamscreen.BuildConfig;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wangxuan on 17/10/12.
 */

public class KuBanHttpClient {

    private static KuBanHttpClient instance = null;

    private KuBanHttpClient() {
    }

    public static KuBanHttpClient getInstance() {
        if (instance == null) {
            instance = new KuBanHttpClient();
        }
        return instance;
    }

    public KuBanApi getKubanApi() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new CommonHeaderInterceptor())
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        KuBanApi kuBanApi = retrofit.create(KuBanApi.class);
        return kuBanApi;
    }
}
