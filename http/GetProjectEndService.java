package com.example.administrator.arnavigatedemo.http;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2017/9/1/001.
 */

public interface GetProjectEndService {
    String BASE_URL = "http://cindy.palmap.cn";
    @GET("/webapi/BeaconInfo/GetProjectEnd/{mapId}")
    Call<HttpResult> getProjectEnd(@Path("mapId") long mapId);
}
