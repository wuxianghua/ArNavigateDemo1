package com.example.administrator.arnavigatedemo.http;

import com.example.administrator.arnavigatedemo.model.BeaconInfo;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;


/**
 * Created by Administrator on 2017/8/16/016.
 */

public interface UploadBeaconsService {
    String BASE_URL = "http://cindy.palmap.cn";
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("/webapi/BeaconInfo/PostBeaconInfo")
    Call<HttpResult> uploadBeaconsInfo(@Body RequestBody route);
}
