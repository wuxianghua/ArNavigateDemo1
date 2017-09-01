package com.example.administrator.arnavigatedemo.http;

import com.example.administrator.arnavigatedemo.model.BeaconInfo;
import com.example.administrator.arnavigatedemo.model.GetBeaconsInfo;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Single;

/**
 * Created by Administrator on 2017/8/16/016.
 */

public interface GetBeaconInfosService {
    String BASE_URL = "http://10.0.10.161:8010";
    @GET("/BeaconInfo/GetBeaconsById")
    Call<List<BeaconInfo>> getBeaconById(@Query("id") int id);
}
