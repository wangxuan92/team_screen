package io.kuban.teamscreen.service;

import java.util.List;
import java.util.Map;

import io.kuban.teamscreen.model.CommonResult;
import io.kuban.teamscreen.model.FacesModel;
import io.kuban.teamscreen.model.PadsModel;
import io.kuban.teamscreen.model.SettingsModel;
import io.kuban.teamscreen.model.ToKenModel;
import io.kuban.teamscreen.model.Visitors;
import io.kuban.teamscreen.model.VisitsModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by wang on 2016/8/3.
 */


public interface KuBanApi {

    /**
     * ----------注册pad-----------------------------
     */
    @POST("pads/register")
    Call<ToKenModel> postRegister(@QueryMap Map<String, String> queries);

    /**
     * ----------获取pad绑定信息-----------------------------
     */
    @GET("pads/{id}")
    Call<PadsModel> getPads(@Path("id") String id);

    /**
     * ----------获取settings信息----------------------------
     */
    @GET("visitors/settings")
    Call<SettingsModel> getSettings();

    /**
     * ----------确认访客到达(无需token)-----------------------------
     */
    @GET("visitors/search")
    Call<List<VisitsModel>> getArrivedConfirm(@Query("location_id") String location_id, @Query("visits_code") String visits_code);

    /**
     * ----------七牛上传token-----------------------------
     */
    @POST("uploads/token")
    Call<CommonResult> postUploadToken();

    /**
     * ----------创建访客-----------------------------
     */
    @POST("visitors")
    Call<VisitsModel> postVisitors(@Body Visitors v);


    /**
     * ----------签到-----------------------------
     */
    @PUT("visitors/{id}/signin")
    Call<VisitsModel> putSignin(@Path("id") String id, @Body Visitors v);

    /**
     * ----------上传人脸识别信息-----------------------------
     */
    @POST("faces")
    Call<FacesModel> postFaces(@Body Map<String, String> queries);

    /**
     * ----------更新人脸识别信息-----------------------------
     */
    @PUT("faces/{id}")
    Call<FacesModel> putFaces(@Path("id") String id, @Body Map<String, String> queries);

}
