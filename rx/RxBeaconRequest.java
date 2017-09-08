package com.example.administrator.arnavigatedemo.rx;

import com.example.administrator.arnavigatedemo.model.BeaconInfo;
import com.example.administrator.arnavigatedemo.model.HasBeaconsMapInfo;
import com.example.administrator.arnavigatedemo.utils.CacheUtils;
import com.palmaplus.nagrand.data.DataList;
import com.palmaplus.nagrand.data.DataSource;
import com.palmaplus.nagrand.data.MapModel;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2017/9/7/007.
 */

public final class RxBeaconRequest {

    public static Observable<List> requestNativeBeacons(CacheUtils earthParking,String mapName) {
        return Observable.create(new NativeBeaconInfoRequestOnSubscribe(earthParking,mapName));
    }

    public static Observable<List> saveNativeBeacons(List< HasBeaconsMapInfo > beaconMapsInfoList) {
        return Observable.create(new NativeBeaconInfoSaveOnSubscribe(beaconMapsInfoList));
    }

    public static Observable<List> requestServerBeacons(int versionId) {
        return Observable.create(new ServerBeaconInfoRequestOnSubscribe(versionId));
    }

    public static Observable<List> refreshServerBeacons(long mapId) {
        return Observable.create(new ServerBeaconInfoRefreshOnSubscribe(mapId));
    }
}
