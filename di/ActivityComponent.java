package com.example.administrator.arnavigatedemo.di;

import com.example.administrator.arnavigatedemo.BaseActivity;

import dagger.Component;

/**
 * Created by Administrator on 2017/9/6/006.
 */
@Component(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(BaseActivity activity);
}
