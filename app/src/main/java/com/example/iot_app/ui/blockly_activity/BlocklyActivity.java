package com.example.iot_app.ui.blockly_activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.example.iot_app.R;
import com.example.iot_app.ui.blockly_activity.webview.WebAppInterface;
import com.example.iot_app.ui.bluetooth.AppBluetoothDevice;
import com.example.iot_app.ui.bluetooth.ble.BleActivity;
import com.example.iot_app.ui.bluetooth.hc05.BlueToothFragment;
import com.example.iot_app.ui.main.MainActivity;
import com.example.iot_app.utils.JavascriptUtil;
import com.google.blockly.android.codegen.CodeGenerationRequest;
import com.google.blockly.model.Block;
import com.iot.blockly.RainbowHatBlocklyBaseActivity;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;
import com.shreyaspatil.material.navigationview.MaterialNavigationView;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;


public class BlocklyActivity extends RainbowHatBlocklyBaseActivity {
    private static final String TAG = "MainActivity";
    private static final int PERMISSION_REQUEST_CODE = 199;
    public static final String KEY_DATA = "key_data";
    public static final String BLOCKLY_ACTIVITY = "BLOCKLY_ACTIVITY";
    public static  final String SERVICE_UUID =  "0000ffe0-0000-1000-8000-00805f9b34fb";
    public static  final String NOR_CHAR_UUID    =    "0000ffe1-0000-1000-8000-00805f9b34fb";
    public static  final String WRITE_CHAR_UUID    =    "0000ffe1-0000-1000-8000-00805f9b34fb";
    private final Handler mHandler = new Handler();
    Button button;

    String bluetoothResponse="";
    //View
    WebView webView;
    //bluetooth
    String address = null;
    private  BleDevice mBleDevice = null;
    private ProgressDialog progress ;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    public static BluetoothGattService mService;
    //data
    public static int sensorData = 0;
    public static boolean touchData = true;
    public static int temperatureData = 0;
    public static int soundData = 0;
    public static int lightData = 0;
    public static int isStop = 0;

    //runable
    Runnable mRunable;
    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkingBluetooth();
        requestBlePermissions(this, PERMISSION_REQUEST_CODE);
        button = findViewById(R.id.reset_click);
        try{
            address = getIntent().getStringExtra(BlueToothFragment.EXTRA_ADDRESS);
            AppBluetoothDevice.setmBleDevice(getIntent().getParcelableExtra(KEY_DATA));
            mBleDevice =  AppBluetoothDevice.getmBleDevice();
            writeBle("ff550400010103");
        }
        catch(Exception e){}
        if(AppBluetoothDevice.getmBleDevice() == null){
            showAlertDialog();
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
                    //setup sever
                     if(mBleDevice != null){
                         if(count == 1)   {
                             notifyEnable();
                             count++;
                         }
                         if(generatedCode.contains("Turtle.isStop")){
                             isStop = 0;
                             // Sample callback.
                             Toast.makeText(getApplicationContext(), generatedCode,
                                     Toast.LENGTH_LONG).show();
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
                                     }) ;

                                 }
                             };
                             mHandler.post(mRunable);
                         }
                         else{
                             Toast.makeText(BlocklyActivity.this, "Add some blocks on it", Toast.LENGTH_SHORT).show();
                         }

                     }
                     else{
                           showAlertDialog();
                     }
                }
            };


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




    private void checkingBluetooth(){
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        if(myBluetooth == null)
        {
            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
            finish();
        }
        else if(!myBluetooth.isEnabled())
        {
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon,1);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
        webView = null;

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mBleDevice != null){

        }

    }

    private void  showAlertDialog(){
        // Create Alert using Builder
        new FancyGifDialog.Builder(this)
                .setTitle("Choosing Connected Device")
                .setNegativeBtnText("Cancel")
                .setMessage("   ")
                .setPositiveBtnBackground("#FF4081")
                .setGifResource(R.drawable.bluetooth)
                .setPositiveBtnText("Ok")
                .setNegativeBtnBackground("#FFA9A7A8")
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        Intent intent = new Intent(BlocklyActivity.this, BleActivity.class);
                        intent.putExtra("activity", BLOCKLY_ACTIVITY);
                        startActivity(intent);
                        Toast.makeText(BlocklyActivity.this,"Ok",Toast.LENGTH_SHORT).show();
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        Toast.makeText(BlocklyActivity.this,"Cancel",Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
    }
    
    public  void replaceFragment(int containerId, Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(fragment.getTag())
                .commit();
    }

    public static void writeBle(String data){

            BleManager.getInstance().write(
                    AppBluetoothDevice.getmBleDevice(),
                    SERVICE_UUID,
                    WRITE_CHAR_UUID ,
                    HexUtil.hexStringToBytes(data),
                    new BleWriteCallback() {
                        @Override
                        public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                            Log.d(TAG, "onWriteSuccess: "+  data);
                        }
                        @Override
                        public void onWriteFailure(final BleException exception) {
                            Log.d(TAG, "onWriteFailure: "+ exception.getDescription());
                        }
                    });

    }

    public static void  notifyEnable(){
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
                                    Log.d(TAG, "onNotifyFailure: "+ exception.getDescription());
                                }

                                @Override
                                public void onCharacteristicChanged(byte[] data) {
                                    // stopNotify();

                                    String val = HexUtil.formatHexString(characteristic.getValue(), true);
                                    Log.d(TAG, "onCharacteristicChanged: "+ val);

                                    //touch sensor
                                    processTouchSensor(val);

                                    if(val.length() > 55){
                                        String s1 = val.substring(0,29);
                                        String s2 = val.substring(30,val.length());
//                                        Log.d(TAG, "onCharacteristicChanged: s1"+s1);
//                                        Log.d(TAG, "onCharacteristicChanged: s2"+s2);
                                        if(s1.contains("ff 55 00 02")){
                                            try{                                            ;
                                                int t = getSensor(s1);
                                                if(t>1 && t<=399)
                                                sensorData = t;
                                                Log.d(TAG, "ultra sensor: "+ sensorData);
                                            }
                                            catch (Exception e){
                                            }
                                        }

                                        if(s2.contains("ff 55 00 02")){
                                            try{
                                                int t = getSensor(s2);
                                                if(t>0 && t<=399)
                                                    sensorData = t;
                                                Log.d(TAG, "ultra sensor: "+ sensorData);
                                            }
                                            catch (Exception e){
                                            }
                                        }
                                    }
                                    else{
                                        if(val.contains("ff 55 00 02")){
                                            try{
                                                int t = getSensor(val);
                                                if(t>0 && t<=399)
                                                    sensorData = t;
                                                Log.d(TAG, "ultra sensor: "+ sensorData);
                                            }
                                            catch (Exception e){
                                            }
                                        }
                                        else{
                       }
                                    }
                                }
                            }) ;
        }

        private static void processTouchSensor(String s){
            if(s.contains("ff 55 00 01 00 0d 0a")){
                Log.d(TAG, "processTouchSensor: true");
                touchData = true;   // touch
            }else{
                if(s.contains("ff 55 00 01 01 0d 0a")){
                    Log.d(TAG, "processTouchSensor: false");
                    touchData = false;  //no touch
                }
            }


        }

    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }


    public  void stopNotify(){
        BleManager.getInstance().stopNotify(mBleDevice,
                SERVICE_UUID,
                NOR_CHAR_UUID);
    }

    public static int getSensor(String hex){
        //ff 55 00 02 8d b0 cc 40 0d 0a
        String sa[] = hex.split(" ");
        int sensorVal =   com.example.iot_app.utils.HexUtil.getDecimal("0x"+ sa[7] + sa[6] + sa[5] + sa[4]);
        return (int) com.example.iot_app.utils.HexUtil.hexToFloat(sensorVal);
    }

    public static void requestBlePermissions(final Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, requestCode);
    }

    @Override
    protected void onStop() {
        super.onStop();
        
    }

    @Override
    public void onClear() {
        Toast.makeText(BlocklyActivity.this,"Stop", Toast.LENGTH_SHORT).show();
        isStop = 1;
        Log.d(TAG, "blockly:isstop "+ isStop);
    }

    public void showStartMenu(){
       showStartButton();
    }

    public void showStopMenu(){
        showStopMenu();
    }

    @Override
    public void openMainActivity() {
        Intent intent = new Intent(BlocklyActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
