package com.example.administrator.arnavigatedemo.http;

import com.example.administrator.arnavigatedemo.model.BeaconInfo;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/9/1/001.
 */

public interface GetBeaconsByIdService {
    String BASE_URL = "http://10.0.10.161:8010";
    @GET("/BeaconInfo/GetBeaconsById")
    Call<List<BeaconInfo>> getVersionByMapId(@Query("id") int id);
}
