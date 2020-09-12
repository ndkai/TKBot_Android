package com.example.iot_app.di.component;

import com.example.iot_app.data.IDataManager;
import com.example.iot_app.di.MyApp;
import com.example.iot_app.di.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface IAppComponent {
    void inject(MyApp myApp);

    IDataManager getIDataManager();

}
