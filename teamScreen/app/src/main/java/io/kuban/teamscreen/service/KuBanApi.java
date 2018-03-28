package io.kuban.teamscreen.service;

import java.util.Map;

import io.kuban.teamscreen.model.AreasModel;
import io.kuban.teamscreen.model.CommonResult;
import io.kuban.teamscreen.model.FacesModel;
import io.kuban.teamscreen.model.PadsModel;
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

/**
 * Created by wang on 2016/8/3.
 */


public interface KuBanApi {

    /**
     * ----------注册pad-----------------------------
     */
    @POST("pads/register")
    Call<ToKenModel> postRegister(@Body Map<String, String> queries);

    /**
     * ----------获取pad绑定信息-----------------------------
     */
    @GET("pads/{id}")
    Call<PadsModel> getPads(@Path("id") String id);

    /**
     * ----------区域详情-----------------------------
     */
    @GET("areas/{id}")
    Call<AreasModel> getAreas(@Path("id") String id, @Query("includes") String includes);


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

}
