package com.example.iot_app.ui.bluetooth;

import com.clj.fastble.data.BleDevice;

public class AppBluetoothDevice {
    public static BleDevice mBleDevice;

    public static BleDevice getmBleDevice() {
        return mBleDevice;
    }

    public static void setmBleDevice(BleDevice mBleDevice) {
        AppBluetoothDevice.mBleDevice = mBleDevice;
    }
}
