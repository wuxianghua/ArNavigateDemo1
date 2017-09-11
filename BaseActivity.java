package com.example.administrator.arnavigatedemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.arnavigatedemo.di.ActivityModule;
import com.example.administrator.arnavigatedemo.di.DaggerActivityComponent;
import com.example.administrator.arnavigatedemo.widget.ProgressDialogDelegate;

import javax.inject.Inject;

import dagger.Lazy;

/**
 * Created by Administrator on 2017/9/7/007.
 */

public class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerActivityComponent.builder().activityModule(new ActivityModule(this)).build().inject(this);
    }

    @Inject
    protected Lazy<ProgressDialogDelegate> proDelegate;

    public void showProDialog() {
        proDelegate.get().show();
    }

    public void showProDialog(String title, String msg){
        proDelegate.get().show(title, msg);
    }

    public void hideProDialog() {
        try {
            proDelegate.get().hide();
        }catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            proDelegate.get().hide();
            proDelegate = null;
        } catch (Exception e) {
        }
    }

    public void showLoading() {
        showProDialog();
    }

    public void showLoading(String title, String msg) {
        showProDialog(title, msg);
    }

    public void hideLoading() {
        hideProDialog();
    }

}
