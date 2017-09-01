package com.example.administrator.arnavigatedemo.http;

import com.example.administrator.arnavigatedemo.model.ServiceMapInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/9/1/001.
 */

public interface GetProjectStartService {
    String BASE_URL = "http://10.0.10.161:8010";
    @GET("/BeaconInfo/GetProjectStart/{mapId}")
    Call<HttpResult> getProjectStart(@Path("mapId") long mapId,
                                               @Query("versionId") int versionId);
}
