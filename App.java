package com.example.administrator.arnavigatedemo;

import android.Manifest;
import android.app.Application;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.example.administrator.arnavigatedemo.utils.Utils;
import com.palmaplus.nagrand.core.Engine;

import java.io.File;

/**
 * Created by Administrator on 2017/7/28/028.
 */

public class App extends Application {

    private static final String TAG = "App";
    private static App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        FileUtilsTools.copyDirToSDCardFromAsserts(this, "Nagrand/lua", "font");
        FileUtilsTools.copyDirToSDCardFromAsserts(this, "Nagrand/lua", "Nagrand/lua");
        mInstance = this;
        // init Engine
        Utils.init(this);
        Engine instance = Engine.getInstance();
        instance.startWithLicense(Constants.AppKey, this);
    }

    public static App getInstance() {
        return mInstance;
    }
    @Override
    public File getCacheDir() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File cacheDir = getExternalCacheDir();
            if (cacheDir != null && (cacheDir.exists() || cacheDir.mkdirs())) {
                return cacheDir;
            }
        }
        return super.getCacheDir();
    }
}
