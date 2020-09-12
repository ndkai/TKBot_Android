package com.example.bluetoothlibrary;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class BluetoothHelper {

    private static final String EXTRA_DATA = "EXTRA_DATA";
    private static String TAG = "BluetoothHelper";

    private static final int REQUEST_ENABLE_BT = 999;

    private static final String ACTION_GATT_CONNECTED = "ACTION_GATT_CONNECTED";

    private static final String ACTION_GATT_DISCONNECTED = "ACTION_GATT_DISCONNECTED";

    private static final String ACTION_GATT_SERVICES_DISCOVERED = "ACTION_GATT_SERVICES_DISCOVERED";

    public static  Activity activity;

    public static BluetoothManager bluetoothManager;

    public static BluetoothAdapter bluetoothAdapter;

    public static BluetoothLeScanner bluetoothLeScanner =
            BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();

    public static ArrayList<String> scannedDevices = new ArrayList<>();

    public static boolean mScanning = false;

    public static ArrayList<BluetoothDevice> bluetoothDevices = new ArrayList<>();

    public static Handler handler = new Handler();

    public static BluetoothGatt mBluetoothGatt;

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    public static ArrayList<BluetoothGattService> leDeviceList;
    public static void getBluetoothAdapter(Activity activity) {
        bluetoothManager = (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
    }

    public static void EnableBluetooth(Activity activity) {
        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

   public static ScanCallback callback = new ScanCallback() {
       @Override
       public void onScanResult(int callbackType, ScanResult result) {
           super.onScanResult(callbackType, result);
           Log.d(TAG, "Device discover: "+result.getDevice().getName());
           if (  bluetoothDevices.contains(result.getDevice()) && result.getDevice().getName() != null) {
               Log.d(TAG, "Devicexxx : " +   result.getDevice().getName());
               bluetoothDevices.add( result.getDevice());
           }

       }

       @Override
       public void onScanFailed(int errorCode) {
           Log.d(TAG, "Duy1: ");
           super.onScanFailed(errorCode);
       }
   };

    public static void scanLeDevice() {

        if (!mScanning) {
            // Stops scanning after a pre-defined scan period.
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    bluetoothLeScanner.stopScan(callback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            bluetoothLeScanner.startScan(callback);
        } else {
            mScanning = false;
            bluetoothLeScanner.stopScan((ScanCallback) callback);
        }
    }

    public static void connectDevice(){

    }


    public static BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            String intentAction;
            if(newState == BluetoothProfile.STATE_CONNECTED){
                intentAction = ACTION_GATT_CONNECTED;
                broadcastUpdate(intentAction);
                Log.d(TAG, "Connected to gatt server: ");
                Log.d(TAG, "Attempting to start service directory: ");
            }
            else{
                if(newState == BluetoothProfile.STATE_DISCONNECTED){
                    intentAction = ACTION_GATT_DISCONNECTED;
                    broadcastUpdate(intentAction);
                    Log.d(TAG, "Disconnect from Gatt server: ");
                }
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if(status == BluetoothGatt.GATT_SUCCESS){
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            }
            else{
                Log.d(TAG, "onServicesDiscovered received: "+ status);
            }
        }
    };

    public static void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        activity.sendBroadcast(intent);
    }

    public static void broadcastUpdate(final String action, final BluetoothGattCharacteristic characteristic) {

        final Intent intent = new Intent(action);

        final byte[] data = characteristic.getValue();
        if (data != null && data.length > 0) {
            final StringBuilder stringBuilder = new StringBuilder(data.length);
            for(byte byteChar : data)
                stringBuilder.append(String.format("%02X", byteChar));
            intent.putExtra(EXTRA_DATA, new String(data) + "\n" + stringBuilder.toString());
        }
        activity.sendBroadcast(intent);
    }

}


