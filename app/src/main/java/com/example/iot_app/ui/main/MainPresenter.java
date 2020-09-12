package com.example.iot_app.ui.main;

import com.example.iot_app.data.IDataManager;
import com.example.iot_app.ui.base.BasePresenter;

public class MainPresenter<T extends IMainActivity> extends BasePresenter<T> {
    public MainPresenter(IDataManager iDataManager) {
        super(iDataManager);
    }


    public void setLanguagePref(String language) {
        getIDataManager().setCurrentLanguage(language);
    }


    public String getLanguagePref() {
        return getIDataManager().getCurrentLanguage();
    }
}
