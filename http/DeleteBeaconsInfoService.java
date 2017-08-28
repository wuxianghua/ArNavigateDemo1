package com.example.administrator.arnavigatedemo.http;

import com.example.administrator.arnavigatedemo.model.GetBeaconsInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2017/8/17/017.
 */

public interface DeleteBeaconsInfoService {
    String BASE_URL = "http://10.0.10.161:8010";
    @GET("/api/BeaconInfo/GetDeleteBeaconInfo/{minor}")
    Call<HttpResult> deleteBeaconsInfo(@Path("minor") long minor);
}
