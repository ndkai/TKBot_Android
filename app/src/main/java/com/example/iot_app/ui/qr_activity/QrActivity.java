package com.example.iot_app.ui.qr_activity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.clj.fastble.data.BleDevice;
import com.clj.fastble.utils.HexUtil;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.example.iot_app.R;
import com.example.iot_app.ui.bluetooth.hc05.BlueToothActivity;
import com.example.iot_app.ui.main.MainActivity;
import com.example.iot_app.ui.qr_activity.qr_reader.DecoderActivity;
import com.example.iot_app.ui.qr_activity.qr_reader.PointsOverlayView;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QrActivity extends DecoderActivity implements QRCodeReaderView.OnQRCodeReadListener {
    public static final String QRACTIVITY = "QrActivity";
    final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    final String TAG = "QrActivity";

    @BindView(R.id.qrdecoderview)
    QRCodeReaderView qrCodeReaderView;
    @BindView(R.id.points_overlay_view)
    PointsOverlayView pointsOverlayView;
    //bluetooth
    String address = null;
    public BleDevice mBleDevice;
    private ProgressDialog progress ;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    public static BluetoothGattService mService;
    public String hc05Address = null;
    Menu optionsMenu;
    //menu
    MenuItem bluetoothMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        checkingBluetooth();
        setupViews();
        try{
            //hc05
            Intent newint = getIntent();
            hc05Address = newint.getStringExtra(BlueToothActivity.EXTRA_ADDRESS);
        }
        catch(Exception e){}

        if(hc05Address == null){
            showConnectDialog();
        }
        else{
            new QrActivity.ConnectBT().execute();
        }


    }

    private void setupViews(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setUpQR();
    }

    private void setUpQR(){
        // Use this function to set front camera preview
        qrCodeReaderView.setBackCamera();

        // Use this function to set back camera preview
       // qrCodeReaderView.setBackCamera();
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        Log.d(TAG, "onQRCodeRead: "+text);
        if(hc05Address != null){
            if(text.equals("di thang")){
                hc05Send("ff550700020501ffff00");
            } else
            if(text.equals("lui")){
                hc05Send("ff5507000205ff0001ff");
            } else
            if(text.equals("trai")){
                hc05Send("ff5507000205ff00ff00");
            } else
            if(text.equals("phai")){
                hc05Send("ff550700020501ff01ff");
            } else
            if(text.equals("dung")){
                hc05Send("ff550700020500000000");
            }
        }

        pointsOverlayView.setPoints(points);
    }

    private class  ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(QrActivity.this, "Connecting...", getResources().getString(R.string.wait));  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(hc05Address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                Toast.makeText(getApplicationContext(),"Connection Failed. Is it a SPP Bluetooth? Try again.", Toast.LENGTH_SHORT).show();
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Connected", Toast.LENGTH_SHORT).show();
                bluetoothMenu.setIcon(R.drawable.white_bluetooth);
                isBtConnected = true;
                //  new BlocklyActivity.ReceiveData().start();
            }
            progress.dismiss();
        }
    }

    public void hc05Send(String s){
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write(HexUtil.hexStringToBytes(s));
            }
            catch (IOException e)
            {
                showDisConnectDialog();
            }
        }
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

    private void  showConnectDialog(){
        new FancyGifDialog.Builder(this)
                .setTitle(getResources().getString(R.string.choosing_connected))
                .setNegativeBtnText(getResources().getString(R.string.cancel))
                .setPositiveBtnBackground(R.color.cfdialog_positive_button_pressed_color)
                .setPositiveBtnText(getResources().getString(R.string.ok))
                .setGifResource(R.drawable.bluetooth)
                .setNegativeBtnBackground(R.color.cfdialog_negative_button_color)
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        Intent intent = new Intent(QrActivity.this, BlueToothActivity.class) ;
                        intent.putExtra("activity",QRACTIVITY);
                        startActivity(intent);
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        Toast.makeText(QrActivity.this,getResources().getString(R.string.cancel),Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
    }


    private void  showDisConnectDialog(){

        new FancyGifDialog.Builder(this)
                .setTitle("You realy want to disconnect device")
                .setNegativeBtnText(getResources().getString(R.string.cancel))
                .setPositiveBtnBackground(R.color.cfdialog_positive_button_pressed_color)
                .setPositiveBtnText(getResources().getString(R.string.ok))
                .setGifResource(R.drawable.bluetooth_sleep)
                .setNegativeBtnBackground(R.color.cfdialog_negative_button_color)
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        hc05Address = null;
                        try {
                            btSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        bluetoothMenu.setIcon(R.drawable.black_bluetooth);
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        Toast.makeText(QrActivity.this,getResources().getString(R.string.cancel),Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.controller_menu, menu);
        optionsMenu = menu;
        bluetoothMenu = optionsMenu.findItem(R.id.controler_bluetooth);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBack();
        }  else
        if(id == R.id.controler_bluetooth){
            if(hc05Address == null){
                showConnectDialog();
            }
            else{
                showDisConnectDialog();
            }

        }
        return super.onOptionsItemSelected(item);
    }


    public void onBack(){
        Intent intent = new Intent(QrActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

