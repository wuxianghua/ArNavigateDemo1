package com.example.administrator.arnavigatedemo.rx;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.administrator.arnavigatedemo.adapter.HasBeaconsMapAdapter;
import com.example.administrator.arnavigatedemo.model.BeaconInfo;
import com.example.administrator.arnavigatedemo.model.HasBeaconsMapInfo;
import com.example.administrator.arnavigatedemo.model.MapInfo;
import com.example.administrator.arnavigatedemo.utils.CacheUtils;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;


/**
 * Created by 王天明 on 2016/4/26.
 */
public class NativeBeaconInfoSaveOnSubscribe implements ObservableOnSubscribe<List> {

    private File absoluteFile;
    private HasBeaconsMapInfo mapInfo;
    private List<HasBeaconsMapInfo> mBeaconMapsInfoList;

    public NativeBeaconInfoSaveOnSubscribe(List<HasBeaconsMapInfo> beaconMapsInfoList) {
        absoluteFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"beacontool");
        mapInfo = new HasBeaconsMapInfo();
        mBeaconMapsInfoList = beaconMapsInfoList;
    }

    @Override
    public void subscribe(@NonNull ObservableEmitter<List> e) throws Exception {
        mBeaconMapsInfoList.clear();

        if (absoluteFile.listFiles() == null) return;
        for (File file : absoluteFile.listFiles()) {
            if (file.listFiles() != null&&file.listFiles().length != 0) {
                mapInfo.beacons = file.listFiles().length;
                String[] split = file.getName().split("-");
                mapInfo.mapName = split[0];
                CacheUtils instance = CacheUtils.getInstance(file.getName());
                String string = instance.getString(split[0]);
                Gson gson = new Gson();
                List<Double> list = gson.fromJson(string, List.class);
                if (list == null) return;
                for (double i : list) {
                    BeaconInfo beaconInfo = (BeaconInfo) instance.getSerializable(String.valueOf(i).substring(0,5));
                    if (beaconInfo == null) return;
                    if (beaconInfo.uploadSuccess) {

                    }else {
                        mapInfo.isUploadSuccess = true;
                    }
                }
                mapInfo.mapId = Integer.valueOf(split[1]);
                mBeaconMapsInfoList.add(mapInfo);
                e.onNext(mBeaconMapsInfoList);
                e.onComplete();
            }
        }
    }
}
