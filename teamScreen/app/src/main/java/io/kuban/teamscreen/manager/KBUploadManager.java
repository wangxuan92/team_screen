package io.kuban.teamscreen.manager;

import android.text.TextUtils;
import android.util.Log;

import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.kuban.teamscreen.BuildConfig;
import io.kuban.teamscreen.CustomerApplication;
import io.kuban.teamscreen.base.BaseCompatActivity;
import io.kuban.teamscreen.event.NetworkRequestStatusEvent;
import io.kuban.teamscreen.model.CommonResult;
import io.kuban.teamscreen.model.FacesModel;
import io.kuban.teamscreen.model.UserModel;
import io.kuban.teamscreen.model.Visitors;
import io.kuban.teamscreen.model.VisitsModel;
import io.kuban.teamscreen.utils.FileUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by wangxuan on 17/11/8.
 */

public class KBUploadManager {
    /**
     * @param userModel
     * @param isAutomatic 是否自动提交该操作会删除数据库暂存数据
     * @param file
     */
    public static void upLoadToken(final UserModel userModel, final boolean isAutomatic, final File file) {
        Call<CommonResult> commonResultCall = BaseCompatActivity.kuBanHttpClient.getKubanApi().postUploadToken();
        commonResultCall.enqueue(new Callback<CommonResult>() {
            @Override
            public void onResponse(Call<CommonResult> call, Response<CommonResult> response) {
                CommonResult result = response.body();
                if (result != null && !TextUtils.isEmpty(result.token)) {
                    uploadLogo(userModel, isAutomatic, result.token, file);
                }
            }

            @Override
            public void onFailure(Call<CommonResult> call, Throwable t) {
            }
        });
    }

    private static void uploadLogo(final UserModel userModel, final boolean isAutomatic, final String uploadToken, File file) {
        UploadManager uploadManager = null;
        if (BuildConfig.LOG_DEBUG) {
            uploadManager = new UploadManager();
        } else {
            uploadManager = new UploadManager(new Configuration.Builder().zone(Zone.zone1).build());
        }
        uploadManager.put(file, FileUtils.getFileName(file.getAbsolutePath()), uploadToken,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        //开始上传服务器
                        String logoUrl = BuildConfig.ASSET_BASE_URL + key;
                        Log.e("KBUploadManager", " avatar " + logoUrl);
                        userModel.userMap.put(ActivityManager.AVATAR, logoUrl);
                        if (userModel.isSignin) {
                            putSignin(userModel, isAutomatic);
                        } else {
                            postVisitors(userModel, isAutomatic);
                        }
                    }
                }, null);
    }

    //访客签到
    private static void putSignin(final UserModel userModel, final boolean isAutomatic) {
        Visitors v = new Visitors();
        v.location_id = CustomerApplication.locationId;
        v.info = userModel.userMap;
        Call<VisitsModel> createSessionCall = BaseCompatActivity.kuBanHttpClient.getKubanApi().putSignin(userModel.visitorsId, v);
        createSessionCall.enqueue(new Callback<VisitsModel>() {
            @Override
            public void onResponse(Call<VisitsModel> call, Response<VisitsModel> response) {
                EventBus.getDefault().post(new NetworkRequestStatusEvent(response, null, isAutomatic));
                if (response.isSuccessful()) {
                    Object featureDataObj = userModel.userMap.get(ActivityManager.FEATURE_DATA);
                    if (null != featureDataObj) {
                        String featureData = featureDataObj.toString();
                        if (!TextUtils.isEmpty(featureData)) {
                            postFaces(response.body(), userModel.userMap.get(ActivityManager.AVATAR).toString(), userModel.userMap.get(ActivityManager.FEATURE_DATA).toString());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<VisitsModel> call, Throwable t) {
                EventBus.getDefault().post(new NetworkRequestStatusEvent(null, t, isAutomatic));
            }
        });
    }

    //临时访客签到
    private static void postVisitors(final UserModel userModel, final boolean isAutomatic) {
        Visitors v = new Visitors();
        v.location_id = CustomerApplication.locationId;
        v.info = userModel.userMap;
        Call<VisitsModel> createSessionCall = BaseCompatActivity.kuBanHttpClient.getKubanApi().postVisitors(v);
        createSessionCall.enqueue(new Callback<VisitsModel>() {
            @Override
            public void onResponse(Call<VisitsModel> call, Response<VisitsModel> response) {
                EventBus.getDefault().post(new NetworkRequestStatusEvent(response, null, isAutomatic, userModel.dbId));
                if (response.isSuccessful()) {
                    Object featureDataObj = userModel.userMap.get(ActivityManager.FEATURE_DATA);
                    if (null != featureDataObj) {
                        String featureData = featureDataObj.toString();
                        if (!TextUtils.isEmpty(featureData)) {
                            postFaces(response.body(), userModel.userMap.get(ActivityManager.AVATAR).toString(), userModel.userMap.get(ActivityManager.FEATURE_DATA).toString());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<VisitsModel> call, Throwable t) {
                EventBus.getDefault().post(new NetworkRequestStatusEvent(null, t, isAutomatic, userModel.dbId));
            }
        });
    }

    //上传人脸识别信息
    private static void postFaces(final VisitsModel userModel, String avatar, String feature_data) {
        Map<String, String> queries = new HashMap<>();
        queries.put("faceable_id", userModel.id);
        queries.put("faceable_type", ActivityManager.VISITOR);
        queries.put("location_id", CustomerApplication.locationId);
        queries.put("photo_url", avatar);
        queries.put("feature_data", feature_data);
        Call<FacesModel> createSessionCall = BaseCompatActivity.kuBanHttpClient.getKubanApi().postFaces(queries);
        createSessionCall.enqueue(new Callback<FacesModel>() {
            @Override
            public void onResponse(Call<FacesModel> call, Response<FacesModel> response) {

            }

            @Override
            public void onFailure(Call<FacesModel> call, Throwable t) {

            }
        });
    }
}
