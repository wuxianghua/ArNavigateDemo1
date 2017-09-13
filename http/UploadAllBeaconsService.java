package com.example.administrator.arnavigatedemo.http;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/8/16/016.
 */

public interface UploadAllBeaconsService {
    String BASE_URL = "http://cindy.palmap.cn";
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("/webapi/BeaconInfo/PostAddBeaconInfoList")
    Call<HttpResult> uploadBeaconsInfo(@Body RequestBody route);
}
