package com.example.administrator.arnavigatedemo.presenter;

import android.app.Activity;

import com.example.administrator.arnavigatedemo.model.BeaconInfo;

/**
 * Created by Administrator on 2017/9/6/006.
 */

public interface BleScanResultPresenter {

    void attachView(Activity activity);

    void uploadBeaconsInfo(BeaconInfo beaconInfo);
}
