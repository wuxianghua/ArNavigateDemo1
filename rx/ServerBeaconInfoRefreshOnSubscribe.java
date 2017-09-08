package com.example.administrator.arnavigatedemo.rx;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.example.administrator.arnavigatedemo.MainActivity;
import com.example.administrator.arnavigatedemo.http.GetBeaconInfosService;
import com.example.administrator.arnavigatedemo.http.GetRefreshBeaconService;
import com.example.administrator.arnavigatedemo.http.ServiceFactory;
import com.example.administrator.arnavigatedemo.model.BeaconInfo;
import com.example.administrator.arnavigatedemo.utils.SPUtils;

import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 王天明 on 2016/4/26.
 */
public class ServerBeaconInfoRefreshOnSubscribe implements ObservableOnSubscribe<List> {

    private GetRefreshBeaconService getRefreshBeaconService;
    private long mapId;

    public ServerBeaconInfoRefreshOnSubscribe(long mapId) {
        this.mapId = mapId;
    }

    @Override
    public void subscribe(@NonNull final ObservableEmitter<List> e) throws Exception {
        if (getRefreshBeaconService == null) {
            getRefreshBeaconService = ServiceFactory.getInstance().createService(GetRefreshBeaconService.class);
        }
        final Call<List<BeaconInfo>> refreshBeaconByMapId = getRefreshBeaconService.getRefreshBeaconByMapId(mapId);
        refreshBeaconByMapId.enqueue(new Callback<List<BeaconInfo>>() {
            @Override
            public void onResponse(Call<List<BeaconInfo>> call, Response<List<BeaconInfo>> response) {
                List<BeaconInfo> body = response.body();
                if (body != null) {
                    e.onNext(body);
                    e.onComplete();
                }
            }

            @Override
            public void onFailure(Call<List<BeaconInfo>> call, Throwable t) {
                e.onError(t);
            }
        });
    }
}
