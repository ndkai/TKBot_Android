package com.example.iot_app.ui.blockly_activity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.example.iot_app.R;
import com.example.iot_app.ui.blockly_activity.webview.WebAppInterface;
import com.example.iot_app.ui.bluetooth.AppBluetoothDevice;
import com.example.iot_app.ui.bluetooth.ble.BleActivity;
import com.example.iot_app.ui.bluetooth.hc05.BlueToothActivity;
import com.example.iot_app.ui.handed_controller.HandedControllerActivity;
import com.example.iot_app.ui.main.MainActivity;
import com.example.iot_app.utils.ConvertUtils;
import com.example.iot_app.utils.JavascriptUtil;
import com.google.blockly.android.codegen.CodeGenerationRequest;
import com.iot.blockly.RainbowHatBlocklyBaseActivity;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.UUID;

import static com.example.iot_app.utils.ConvertUtils.getAvoidValue;
import static com.example.iot_app.utils.ConvertUtils.getLinevalue;
import static com.example.iot_app.utils.ConvertUtils.getPotenl;
import static com.example.iot_app.utils.ConvertUtils.getSensor;
import static com.example.iot_app.utils.ConvertUtils.getTemperature;
import static com.example.iot_app.utils.ConvertUtils.getUltra;
import static com.example.iot_app.utils.ConvertUtils.requestBlePermissions;


public class BlocklyActivity extends RainbowHatBlocklyBaseActivity {
    private static final String TAG = "MainActivity";
    private static final String PREF_CURRENT_LANGUAGE = "PREF_CURRENT_LANGUAGE";
    private static final int PERMISSION_REQUEST_CODE = 199;
    public static final String KEY_DATA = "key_data";
    public static final String BLOCKLY_ACTIVITY = "BLOCKLY_ACTIVITY";
    public static final String SERVICE_UUID = "0000ffe0-0000-1000-8000-00805f9b34fb";
    public static final String NOR_CHAR_UUID = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public static final String WRITE_CHAR_UUID = "0000ffe1-0000-1000-8000-00805f9b34fb";
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final Handler mHandler = new Handler();
    private SharedPreferences mPref;
    String bluetoothResponse = "";
    //View
    WebView webView;
    //bluetooth
    String hc05Address = null;
    private BleDevice mBleDevice = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    static BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    public static BluetoothGattService mService;
    public static TypeConnected typeConnected = null;
    //data
    public static int sensorData = 0;
    public static boolean touchData = false;
    public static boolean avoidData = false;
    public static boolean lineValue = false;
    public static String lineValueS2 = "";
    public static String lineValueS1 = "";
    public static int temperatureData = 0;
    public static int potenlData = 0;
    public static int soundData = 0;
    public static int lightData = 0;
    public static int isStop = 0;
    Menu optionsMenu;
    //menu
    static MenuItem bluetoothMenu;
    //runable
    Runnable mRunable;
    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkingBluetooth();
        requestBlePermissions(this, PERMISSION_REQUEST_CODE);
        try {
            //hc05
            Intent newint = getIntent();
            hc05Address = newint.getStringExtra(BlueToothActivity.EXTRA_ADDRESS);

            //ble
            AppBluetoothDevice.setmBleDevice(getIntent().getParcelableExtra(KEY_DATA));
            mBleDevice = AppBluetoothDevice.getmBleDevice();
            if (mBleDevice != null) {
                writeBle("ff550400010103");
            }

        } catch (Exception e) {
        }

        if (hc05Address != null && !hc05Address.equals("")) {
            new ConnectBT().execute();
            typeConnected = TypeConnected.HC05;
            Toast.makeText(this, "HC05 Connected Successful", Toast.LENGTH_SHORT);
            Log.d(TAG, "onCreateContentView: " + typeConnected);
        } else {
            if (AppBluetoothDevice.getmBleDevice() != null) {
                typeConnected = TypeConnected.HM10;
                Toast.makeText(this, "HM10 Connected Successful", Toast.LENGTH_SHORT);
                Log.d(TAG, "onCreateContentView: " + typeConnected);
            } else {
                showAlertDialog();
            }
        }
    }



    @NonNull
    @Override
    protected CodeGenerationRequest.CodeGeneratorCallback getCodeGenerationCallback() {
        return mCodeGeneratorCallback;
    }

    private CodeGenerationRequest.CodeGeneratorCallback mCodeGeneratorCallback =
            new CodeGenerationRequest.CodeGeneratorCallback() {
                @Override
                public void onFinishCodeGeneration(final String generatedCode) {
                    Log.d(TAG, "onFinishCodeGeneration:" + generatedCode);
                    if (mBleDevice == null && hc05Address == null) {
                        showAlertDialog();
                    } else {
                        if (mBleDevice != null) {
                            if (count == 1) {
                                notifyEnable();
                                count++;
                            }
                        }
                      if (hc05Address != null || mBleDevice != null) {
                            if (generatedCode.contains("Turtle.isStop")) {
                                isStop = 0;
                                // Sample callback.
//                                Toast.makeText(getApplicationContext(), generatedCode,
//                                        Toast.LENGTH_LONG).show();
                                mRunable = new Runnable() {
                                    @Override
                                    public void run() {
                                        webView.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                // webView.stopLoading();
                                                String encoded = "Turtle.execute("
                                                        + JavascriptUtil.makeJsString(generatedCode) + ")";
                                                webView.loadUrl("javascript:" + encoded);
                                            }
                                        });

                                    }
                                };
                                mHandler.post(mRunable);
                            } else {
                                Toast.makeText(BlocklyActivity.this, "Add some blocks on it, please!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                }
            };


    public static void hc05Send(String s) {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write(HexUtil.hexStringToBytes(s));
            } catch (IOException e) {
//                isStop = 1;
//                bluetoothMenu.setIcon(R.drawable.black_bluetooth);
            }
        }
    }


    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(BlocklyActivity.this, "Connecting...", getResources().getString(R.string.wait));  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(hc05Address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            } catch (IOException e) {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
//                finish();
            } else {
                msg("Connected.");
                isBtConnected = true;
                bluetoothMenu.setIcon(R.drawable.white_bluetooth);
                new ReceiveData().start();
            }
            progress.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@Nullable Menu menu) {
        getMenuInflater().inflate(R.menu.controller_menu, menu);
        optionsMenu = menu;
        bluetoothMenu = optionsMenu.findItem(R.id.controler_bluetooth);
        bluetoothMenu.setIcon(R.drawable.black_bluetooth);
        return  super.onCreateOptionsMenu(menu);
    }

    @Override
    protected View onCreateContentView(int parentId) {

        View root = getLayoutInflater().inflate(R.layout.turtle_content, null);
        webView = (WebView) root.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

        webView.addJavascriptInterface(new WebAppInterface(this), "Android");
        webView.loadUrl("file:///android_asset/titbot.html");


        return root;
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if(item.getItemId() == R.id.controler_bluetooth){
            if(hc05Address == null)
            {
                showAlertDialog();
            }
            else{
                showDisConnectDialog();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkingBluetooth() {
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        if (myBluetooth == null) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.bluetooth_not_available), Toast.LENGTH_LONG).show();
            finish();
        } else if (!myBluetooth.isEnabled()) {
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon, 1);
        }
    }

    private void  showDisConnectDialog(){

        new FancyGifDialog.Builder(this)
                .setTitle(getResources().getString(R.string.disconnect_device))
                .setNegativeBtnText(getResources().getString(R.string.cancel))
                .setPositiveBtnText(getResources().getString(R.string.ok))
                .setGifResource(R.drawable.bluetooth_sleep)
                .isCancellable(true)
                .setPositiveBtnBackground(R.color.cfdialog_positive_button_pressed_color)
                .setNegativeBtnBackground(R.color.cfdialog_negative_button_color)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        isStop = 1;
                        hc05Send("ff550700020500000000");
                        hc05Address = null;
                        try {
                            btSocket.close();
                            bluetoothMenu.setIcon(R.drawable.black_bluetooth);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        Toast.makeText(BlocklyActivity.this,getResources().getString(R.string.cancel),Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
    }
    private void showAlertDialog() {
        try{
            bluetoothMenu.setIcon(R.drawable.black_bluetooth);
        }
        catch (Exception e){
            
        }
        // Create Alert using Builder
        new FancyGifDialog.Builder(this)
                .setNegativeBtnText(getResources().getString(R.string.cancel))
                .setTitle(getResources().getString(R.string.choosing_connected))
                .setPositiveBtnBackground(R.color.cfdialog_positive_button_pressed_color)
                .setGifResource(R.drawable.bluetooth)
                .setPositiveBtnText(getResources().getString(R.string.ok))
                .setNegativeBtnBackground(R.color.cfdialog_negative_button_color)
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        Intent intent = new Intent(BlocklyActivity.this, BlueToothActivity.class);
                        intent.putExtra("activity", BLOCKLY_ACTIVITY);
                        startActivity(intent);
                        finish();
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        Toast.makeText(BlocklyActivity.this, getResources().getString(R.string.cancel), Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
    }

    public void showBluetoothTypeDialog() {
        // Create Alert using Builder
        new FancyGifDialog.Builder(this)
                .setNegativeBtnText("HM10")
                .setTitle("Choosing Connected Type")
                .setPositiveBtnBackground(R.color.cfdialog_positive_button_pressed_color)
                .setGifResource(R.drawable.bluetooth)
                .setPositiveBtnText("HC05")
                .setNegativeBtnBackground(R.color.cfdialog_negative_button_color)
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        Intent intent = new Intent(BlocklyActivity.this, BlueToothActivity.class);
                        intent.putExtra("activity", BLOCKLY_ACTIVITY);
                        startActivity(intent);
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        Intent intent = new Intent(BlocklyActivity.this, BleActivity.class);
                        intent.putExtra("activity", BLOCKLY_ACTIVITY);
                        startActivity(intent);
                        Toast.makeText(BlocklyActivity.this, "Ok", Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
    }


    public static void writeBle(String data) {

        BleManager.getInstance().write(
                AppBluetoothDevice.getmBleDevice(),
                SERVICE_UUID,
                WRITE_CHAR_UUID,
                HexUtil.hexStringToBytes(data),
                new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                        Log.d(TAG, "onWriteSuccess: " + data);
                    }

                    @Override
                    public void onWriteFailure(final BleException exception) {
                        Log.d(TAG, "onWriteFailure: " + exception.getDescription());
                    }
                });

    }

    public static void notifyEnable() {
        BluetoothGatt bluetoothGatt = BleManager.getInstance().getBluetoothGatt(AppBluetoothDevice.getmBleDevice());
        BluetoothGattService service = bluetoothGatt.getService(UUID.fromString(SERVICE_UUID));
        BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(NOR_CHAR_UUID));

        BleManager.getInstance().notify(
                AppBluetoothDevice.getmBleDevice(),
                SERVICE_UUID,
                NOR_CHAR_UUID,
                new BleNotifyCallback() {
                    @Override
                    public void onNotifySuccess() {
                        Log.d(TAG, "onNotifySuccess: ");
                    }

                    @Override
                    public void onNotifyFailure(final BleException exception) {
                        Log.d(TAG, "onNotifyFailure: " + exception.getDescription());
                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {
                        // stopNotify();

                        String val = HexUtil.formatHexString(characteristic.getValue(), true);
                        Log.d(TAG, "onCharacteristicChanged: " + val);

                        //touch sensor
                        processTouchSensor(val);

                        if (val.length() > 55) {
                            String s1 = val.substring(0, 29);
                            String s2 = val.substring(30, val.length());
//                                        Log.d(TAG, "onCharacteristicChanged: s1"+s1);
//                                        Log.d(TAG, "onCharacteristicChanged: s2"+s2);
                            if (s1.contains("ff 55 00 02")) {
                                try {
                                    ;
                                    int t = getSensor(s1);
                                    if (t > 1 && t <= 1023)
                                        sensorData = t;
                                    Log.d(TAG, "ultra sensor: " + sensorData);
                                } catch (Exception e) {
                                }
                            }

                            if (s2.contains("ff 55 00 02")) {
                                try {
                                    int t = getSensor(s2);
                                    if (t > 0 && t <= 1023)
                                        sensorData = t;
                                    Log.d(TAG, "ultra sensor: " + sensorData);
                                } catch (Exception e) {
                                }
                            }
                        } else {
                            if (val.contains("ff 55 00 02")) {
                                try {
                                    int t = getSensor(val);
                                    if (t > 0 && t <= 1023)
                                        sensorData = t;
                                    Log.d(TAG, "ultra sensor: " + sensorData);
                                } catch (Exception e) {
                                }
                            } else {
                            }
                        }
                    }
                });
    }

    private static void processTouchSensor(String s) {
        if (s.contains("ff 55 00 01 00 0d 0a") && !s.contains("ff 55 00 01 01 0d 0a")) {
            Log.d(TAG, "processTouchSensor: true");
            touchData = true;   // touch
        } else {
            if (s.contains("ff 55 00 01 01 0d 0a") && !s.contains("ff 55 00 01 00 0d 0a")) {
                Log.d(TAG, "processTouchSensor: false");
                touchData = false;  //no touch
            } else if (s.indexOf("ff 55 00 01 00 0d 0a") < s.indexOf("ff 55 00 01 01 0d 0a")) {
                Log.d(TAG, "processTouchSensor: true");
                touchData = true;   // touch
            } else if (s.indexOf("ff 55 00 01 00 0d 0a") > s.indexOf("ff 55 00 01 01 0d 0a")) {
                Log.d(TAG, "processTouchSensor: false");
                touchData = false;
            }
        }

    }

    private static void processLineSensor(String s) {

    }

    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }



    @Override
    protected void onStop() {
        super.onStop();
        if (btSocket != null) {
            try {
                btSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onClear() {
        Toast.makeText(this, "Stop", Toast.LENGTH_SHORT).show();
        isStop = 1;
        hc05Send("ff550700020500000000");
    }

    private class ReceiveData extends Thread {
        InputStream socketInputStream;
        byte[] buffer = new byte[22];
        int bytes;

        @Override
        public void run() {

//            while(true){
            while (isBtConnected) {
                Log.d(TAG, "run: xx");
                try {
                    socketInputStream = btSocket.getInputStream();
                    bytes = socketInputStream.read(buffer);            //read bytes from input buffer
                    //read mesage là chuổi nhận về từ board,`
                    String readMessage = new String(buffer, 0, bytes);
                    String s = HexUtil.formatHexString(buffer, true);
                    Log.i("logging", s + "");
                    processTouchSensor(s);
                    //ultra
                    getUltra(s);
                    //potenl
                    getPotenl(s);
                    //temperature
                    getTemperature(s);
                    //linevalue
                    getLinevalue(s);
                    //avoid
                    getAvoidValue(s);

                } catch (IOException e) {
                    Log.d(TAG, "ReceiveData:" + e.getMessage());
                    Intent intent = new Intent(BlocklyActivity.this, BlocklyActivity.class);
                    startActivity(intent);
                    finish();
                    try {
                        btSocket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        bluetoothMenu.setIcon(R.drawable.black_bluetooth);
                    }
                }
            }
        }
    }

    @Override
    public void openMainActivity() {

        Intent intent = new Intent(BlocklyActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
        if (btSocket != null) {
            try {
                btSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        hc05Address = null;
        isBtConnected = false;
        myBluetooth = null;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (btSocket != null) {
            try {
                btSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        hc05Address = null;
        isBtConnected = false;
        myBluetooth = null;
        AppBluetoothDevice.setmBleDevice(null);


    }

    @Override
    protected void onPause() {
        super.onPause();
        isBtConnected = false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mBleDevice != null) {

        }

    }

    private void setPrefCurrentLanguage(String language) {
        mPref.edit().putString(PREF_CURRENT_LANGUAGE, language).commit();
        Log.d("TAG", "setCurrentLanguage: ");
    }

    private String getPrefCurrentLanguage() {
        Log.d("TAG", "setCurrentLanguage: ");
        return mPref.getString(PREF_CURRENT_LANGUAGE, "");
    }
    
}
