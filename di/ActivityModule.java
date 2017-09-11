package com.example.administrator.arnavigatedemo.di;

import android.content.Intent;

import com.example.administrator.arnavigatedemo.BaseActivity;
import com.example.administrator.arnavigatedemo.Mark;
import com.example.administrator.arnavigatedemo.model.BeaconInfo;
import com.example.administrator.arnavigatedemo.widget.ProgressDialogDelegate;
import com.google.gson.Gson;
import com.palmaplus.nagrand.view.MapOptions;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/9/6/006.
 */
@Module
public class ActivityModule {

    private final BaseActivity activity;

    public ActivityModule(BaseActivity activity) {
        this.activity = activity;
    }

    @Provides
    BaseActivity activity() {
        return this.activity;
    }


    @Provides
    Gson providesGson() {
        return new Gson();
    }

    @Provides
    ArrayList<String> providesArrayListKey() {
        return new ArrayList<>();
    }

    @Provides
    ArrayList<BeaconInfo> providesArrayListBea() {
        return new ArrayList<>();
    }

    @Provides
    MapOptions providesMapOptions() {
        return new MapOptions();
    }

    @Provides
    Intent providesIntent() {
        return new Intent();
    }

    @Provides
    ArrayList<Mark> providesArrayListMark() {
        return new ArrayList<>();
    }

    @Provides
    ProgressDialogDelegate providesDelegate(BaseActivity activity) {
        return new ProgressDialogDelegate(activity, "提示", "加载中...");
    }

}
