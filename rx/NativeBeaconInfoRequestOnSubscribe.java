package com.example.administrator.arnavigatedemo.rx;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.administrator.arnavigatedemo.utils.CacheUtils;
import com.google.gson.Gson;

import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;


/**
 * Created by 王天明 on 2016/4/26.
 */
public class NativeBeaconInfoRequestOnSubscribe implements ObservableOnSubscribe<List> {

    private CacheUtils earthParking;
    private String mapName;
    private Gson gson;

    public NativeBeaconInfoRequestOnSubscribe(CacheUtils earthParking,String mapName) {
        this.earthParking = earthParking;
        this.mapName = mapName;
        gson = new Gson();
    }

    @Override
    public void subscribe(@NonNull ObservableEmitter<List> e) throws Exception {
        List earthparking = gson.fromJson(earthParking.getString(mapName), List.class);
        if (!e.isDisposed()) {
            Log.e("haha",Thread.currentThread().getName());
            e.onNext(earthparking);
            e.onComplete();
        }
    }
}
