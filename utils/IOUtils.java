package com.example.administrator.arnavigatedemo.utils;

import android.os.Handler;
import android.os.Looper;

import java.io.Closeable;

/**
 * Created by 王天明 on 2016/4/28.
 */
public class IOUtils {

    /**
     * 检测是否在主线程
     *
     * @return
     */
    public static synchronized boolean checkMainThread() {
        return Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId();
    }

    public static void closeIO(Closeable...closeables) {
        if (closeables != null && closeables.length>0) {
            for (int i = 0; i < closeables.length; i++) {
                try {
                    closeables[i].close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static Handler handler = new Handler(Looper.getMainLooper());

    public static void postMainThread(Runnable runnable){
        handler.post(runnable);
    }

    public static void postMainThreadDelayed(Runnable runnable, long time){
        handler.postDelayed(runnable,time);
    }

}
