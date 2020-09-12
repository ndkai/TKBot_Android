package com.example.iot_app.data;

import com.example.iot_app.data.api.IApi;
import com.example.iot_app.data.db.IDb;
import com.example.iot_app.data.pref.IPref;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

/*
* Implementation of IDataManager which is the data port
* for all presenters accessing
* */
@Singleton
public class DataManager implements IDataManager {
    
    private IDb mIDb;
    private IPref mIPref;
    private IApi mIApi;
    
    @Inject
    public DataManager(IDb iDb, IPref iPref, IApi iApi) {
        this.mIDb = iDb;
        this.mIPref = iPref;
        this.mIApi = iApi;
    }

    @Override
    public void setCurrentLanguage(String language) {
        mIPref.setCurrentLanguage(language);
    }

    @Override
    public String getCurrentLanguage() {
        return mIPref.getCurrentLanguage();
    }
}
