package com.example.iot_app.di.module;


import android.content.Context;

import com.example.iot_app.data.DataManager;
import com.example.iot_app.data.IDataManager;
import com.example.iot_app.data.api.Api;
import com.example.iot_app.data.api.IApi;
import com.example.iot_app.data.db.Db;
import com.example.iot_app.data.db.IDb;
import com.example.iot_app.data.pref.IPref;
import com.example.iot_app.data.pref.Pref;
import com.example.iot_app.di.MyApp;
import com.example.iot_app.di.annotation.AppContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private MyApp mMyApp;

    public AppModule(MyApp myApp) {
        this.mMyApp = myApp;
    }

    @Provides
    IDataManager provideIDataManager(DataManager dataManager){
        return dataManager;
    }

    @Provides
    IApi provideIApi(Api api) {return api;}

    @Provides
    IDb provideIDb(Db db) {return db;}

    @Provides
    IPref provideIPref(Pref pref) {return pref;}

    @Provides
    @AppContext
    Context provideAppContext() {return mMyApp;}

}
