package com.example.administrator.arnavigatedemo.http;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2017/9/1/001.
 */

public interface GetDelBeaconInfoService {
    String BASE_URL = "http://cindy.palmap.cn";
    @GET("/webapi/BeaconInfo/GetDelBeaconInfo/{mapId}/{minor}")
    Call<HttpResult> getDelBeaconInfo(@Path("mapId") long mapId,
                                      @Path("minor") int minor);
}
