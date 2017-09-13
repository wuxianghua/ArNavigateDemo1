package com.example.administrator.arnavigatedemo.http;

import com.example.administrator.arnavigatedemo.model.BeaconInfo;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/8/16/016.
 */

public interface GetBeaconInfosService {
    String BASE_URL = "http://cindy.palmap.cn";
    @GET("/webapi/BeaconInfo/GetBeaconsById")
    Call<List<BeaconInfo>> getBeaconById(@Query("id") int id);
}
