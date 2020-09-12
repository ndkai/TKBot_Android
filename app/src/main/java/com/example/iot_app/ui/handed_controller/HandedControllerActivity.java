package com.example.iot_app.ui.handed_controller;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.example.iot_app.R;
import com.example.iot_app.game.GameFragment;
import com.example.iot_app.ui.base.BaseActivity;
import com.example.iot_app.ui.blockly_activity.BlocklyActivity;
import com.example.iot_app.ui.blockly_activity.webview.WebAppInterface;
import com.example.iot_app.ui.bluetooth.AppBluetoothDevice;
import com.example.iot_app.ui.bluetooth.ble.BleActivity;
import com.example.iot_app.ui.bluetooth.hc05.BlueToothFragment;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.controlwear.virtual.joystick.android.JoystickView;

public class HandedControllerActivity extends BaseActivity {
    private static final int PERMISSION_REQUEST_CODE = 199;
    public static final String HANDEDACTIVITY = "HandedControllerActivity";
    public static final String KEY_DATA = "key_data";
    
     @BindView(R.id.gamepad_button)
     JoystickView mJoystickView;
     
    //bluetooth
    String address = null;
    public  BleDevice mBleDevice;
    private ProgressDialog progress ;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    public static BluetoothGattService mService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handed_controller_activity2);
        ButterKnife.bind(this);
        //
        BlocklyActivity.requestBlePermissions(this, PERMISSION_REQUEST_CODE);
        checkingBluetooth();
        setupGamePad();
        try{
            address = getIntent().getStringExtra(BlueToothFragment.EXTRA_ADDRESS);
            AppBluetoothDevice.mBleDevice = getIntent().getParcelableExtra(KEY_DATA);
        }
        catch(Exception e){}

        if(AppBluetoothDevice.mBleDevice == null){
            showAlertDialog();
        }
        else{
            mBleDevice = AppBluetoothDevice.mBleDevice;
        }
    }

    public void writeBle(String data){

        BleManager.getInstance().write(
                mBleDevice,
                "0000ffe1-0000-1000-8000-00805f9b34fb",
                "0000ffe3-0000-1000-8000-00805f9b34fb" ,
                HexUtil.hexStringToBytes(data),
                new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                        Log.d("TAG", "onWriteSuccess: "+  data);
                    }
                    @Override
                    public void onWriteFailure(final BleException exception) {
                        Log.d("TAG", "onWriteFailure: "+ exception.getDescription());
                    }
                });

    }




    private void setupGamePad(){
        mJoystickView.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                if(mBleDevice != null){
                    if(angle == 0 ){
                        BlocklyActivity.writeBle("ff550700020500000000");
                    }
                    if(45 <= angle && angle<= 135){
                        //move forward
                        writeBle("ff550700020501ffff00");
                        Log.d("TAG", "onMove: MF");
                    }

                    if(135 < angle && angle >= 225){
                        //TL
                        writeBle("ff5507000205ff00ff00");
                        Log.d("TAG", "onMove: TL");
                    }

                    if(225 < angle && angle <= 315){
                        //back
                        writeBle("ff5507000205ff0001ff");
                        Log.d("TAG", "onMove: MB");
                    }

                    if(315 < angle && angle <= 360){
                        //right
                        writeBle("ff550700020501ff01ff");
                        Log.d("TAG", "onMove: TR");
                    }
                }

            }
        });
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

    private void  showAlertDialog(){

        new FancyGifDialog.Builder(this)
                .setTitle("Choosing Connected Device")
                .setNegativeBtnText("Cancel")
                .setPositiveBtnBackground("#FF4081")
                .setPositiveBtnText("Ok")
                .setGifResource(R.drawable.bluetooth)
                .setNegativeBtnBackground("#FFA9A7A8")
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        Intent intent = new Intent(HandedControllerActivity.this, BleActivity.class) ;
                        intent.putExtra("activity",HANDEDACTIVITY);
                        startActivity(intent);
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        Toast.makeText(HandedControllerActivity.this,"Cancel",Toast.LENGTH_SHORT).show();
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
}
