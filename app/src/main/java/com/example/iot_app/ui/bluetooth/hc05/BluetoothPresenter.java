package com.example.iot_app.ui.bluetooth.hc05;

import com.example.iot_app.data.IDataManager;
import com.example.iot_app.ui.base.BasePresenter;

public class BluetoothPresenter<T extends IBluetoothFragment> extends BasePresenter<T> {
    public BluetoothPresenter(IDataManager iDataManager) {
        super(iDataManager);
    }
}
