package com.example.iot_app.ui.base;

import android.util.Log;

import com.example.iot_app.R;
import com.example.iot_app.data.IDataManager;

import javax.inject.Inject;

/*
* Each Presenter must to extend this class, MainPresenter for example
* The derived class can use available components from BasePresenter
* like IDataManager, the interface to view ...
* */

public class BasePresenter<T extends IBaseView>{

    protected T mIView;

    private IDataManager mIDataManager;

    @Inject
    public BasePresenter(IDataManager iDataManager) {
        this.mIDataManager = iDataManager;
    }

    public void onAttached(T iBaseView) {
        this.mIView = iBaseView;
    }

    public void onDetached() {
        if (mIView != null) mIView = null;
    }

    public IDataManager getIDataManager() {
        return mIDataManager;
    }

    public void log(String msg){
        Log.d("AAA", msg);
    }

}
