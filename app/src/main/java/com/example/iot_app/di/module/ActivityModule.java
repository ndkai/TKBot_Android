package com.example.iot_app.di.module;

import android.content.Context;
import android.content.DialogInterface;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.iot_app.data.IDataManager;
import com.example.iot_app.ui.base.BaseActivity;
import com.example.iot_app.ui.main.MainPresenter;
import com.example.iot_app.ui.splash_activity.SplashPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    BaseActivity mBaseActivity;

    public ActivityModule(BaseActivity baseActivity) {
        this.mBaseActivity = baseActivity;
    }


    @Provides
    GridLayoutManager provideGridLayoutManager(){
        return new GridLayoutManager(mBaseActivity, 2);
    }

    @Provides
    StaggeredGridLayoutManager provideStaggeredGridLayoutManager(){
        return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    @Provides
    MainPresenter provideMainPresenter(IDataManager iDataManager){
        return new MainPresenter(iDataManager);
    }

    @Provides
    SplashPresenter provideSplashPresenter(IDataManager iDataManager){
        return new SplashPresenter(iDataManager);
    }

}
