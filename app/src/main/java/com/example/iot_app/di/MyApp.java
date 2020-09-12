package com.example.iot_app.di;

import android.app.Application;

import androidx.annotation.ColorInt;

import com.example.iot_app.data.IDataManager;
import com.example.iot_app.di.component.DaggerIAppComponent;
import com.example.iot_app.di.component.IAppComponent;
import com.example.iot_app.di.module.AppModule;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import javax.inject.Inject;

/*
* hold global components in all lifetime of app
* */
public class MyApp extends Application {
    @Inject
    IDataManager mIDataManager;

    IAppComponent mIAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mIAppComponent = DaggerIAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        mIAppComponent.inject(this);
        
    }

    public IAppComponent getIAppComponent() {
        return mIAppComponent;
    }
    
}
