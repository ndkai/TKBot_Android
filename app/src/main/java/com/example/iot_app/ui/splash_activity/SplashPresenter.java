package com.example.iot_app.ui.splash_activity;

import com.example.iot_app.data.IDataManager;
import com.example.iot_app.ui.base.BasePresenter;
import com.example.iot_app.ui.base.IBaseView;

public class SplashPresenter<T extends  ISplashActivity> extends BasePresenter<T> {


    public SplashPresenter(IDataManager iDataManager) {
        super(iDataManager);
    }
}
