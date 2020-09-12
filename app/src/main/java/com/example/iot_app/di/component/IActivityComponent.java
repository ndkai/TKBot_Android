package com.example.iot_app.di.component;

import com.example.iot_app.di.annotation.PerActivity;
import com.example.iot_app.di.module.ActivityModule;
import com.example.iot_app.ui.main.MainActivity;
import com.example.iot_app.ui.splash_activity.SplashActivity;

import dagger.Component;

@PerActivity
@Component(modules = ActivityModule.class, dependencies = IAppComponent.class)
public interface IActivityComponent {
    void inject(MainActivity mainActivity);

    void inject(SplashActivity mainActivity);


}


