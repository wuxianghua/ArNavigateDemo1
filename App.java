package com.example.administrator.arnavigatedemo;

import android.Manifest;
import android.app.Application;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.example.administrator.arnavigatedemo.utils.Utils;
import com.palmaplus.nagrand.core.Engine;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;

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
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        CrashReport.initCrashReport(this, "1edeedbd8f", true);
        // Normal app init code...
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
