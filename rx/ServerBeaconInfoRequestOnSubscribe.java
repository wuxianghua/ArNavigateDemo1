package com.example.administrator.arnavigatedemo.rx;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.administrator.arnavigatedemo.http.GetBeaconInfosService;
import com.example.administrator.arnavigatedemo.http.ServiceFactory;
import com.example.administrator.arnavigatedemo.model.BeaconInfo;
import com.example.administrator.arnavigatedemo.model.HasBeaconsMapInfo;
import com.example.administrator.arnavigatedemo.utils.CacheUtils;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 王天明 on 2016/4/26.
 */
public class ServerBeaconInfoRequestOnSubscribe implements ObservableOnSubscribe<List> {

    private GetBeaconInfosService getBeaconInfosService;
    private int versionId;

    public ServerBeaconInfoRequestOnSubscribe(int versionId) {
        this.versionId = versionId;
    }

    @Override
    public void subscribe(@NonNull final ObservableEmitter<List> e) throws Exception {
        if (getBeaconInfosService == null) {
            getBeaconInfosService = ServiceFactory.getInstance().createService(GetBeaconInfosService.class);
        }
        Call<List<BeaconInfo>> beaconsInfo = getBeaconInfosService.getBeaconById(versionId);
        beaconsInfo.enqueue(new Callback<List<BeaconInfo>>() {
            @Override
            public void onResponse(Call<List<BeaconInfo>> call, Response<List<BeaconInfo>> response) {
                if (response == null || response.body() == null) return;
                e.onNext(response.body());
                e.onComplete();
            }

            @Override
            public void onFailure(Call<List<BeaconInfo>> call, Throwable t) {
                e.onError(t);
            }
        });
    }
}
