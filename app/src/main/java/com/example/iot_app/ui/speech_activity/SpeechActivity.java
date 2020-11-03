package com.example.iot_app.ui.speech_activity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothSocket;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.clj.fastble.data.BleDevice;
import com.clj.fastble.utils.HexUtil;
import com.example.iot_app.R;
import com.example.iot_app.ui.base.BaseActivity;
import com.example.iot_app.ui.bluetooth.hc05.BlueToothActivity;
import com.example.iot_app.ui.handed_controller.HandedControllerActivity;
import com.example.iot_app.ui.main.MainActivity;
import com.example.iot_app.utils.ConvertUtils;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SpeechActivity extends BaseActivity {
    private final int REQ_CODE_SPEECH_INPUT = 190;
    public static final String SPEECHACTIVITY = "SpeechActivity";
    final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    final String TAG = "SpeechActivity";

    @BindView(R.id.txtSpeechInput)
    TextView txtSpeechInput;

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
        setContentView(R.layout.speeach_activity_layout);
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
            new ConnectBT().execute();
        }
        

    }

    private void setupViews(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private class  ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(SpeechActivity.this, "Connecting...", getResources().getString(R.string.wait));  //show a progress dialog
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
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));

        intent.putExtra("android.speech.extra.DICTATION_MODE", true);
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false);
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    Log.d(TAG, "onActivityResult: "+result.get(0));
                    processVoice(result.get(0));
                }
                break;
            }

        }
    }

    private void processVoice(String command){
        Log.d(TAG, "processVoice: "+ConvertUtils.deAccent(command));
        if(ConvertUtils.deAccent(command).equals("đi thang")){
            hc05Send("ff550700020501ffff00");
            Log.d(TAG, "processVoice: forward");
        } else
        if(ConvertUtils.deAccent(command).equals("lui")){
            hc05Send("ff5507000205ff0001ff");
            Log.d(TAG, "processVoice: back");
        } else
        if(ConvertUtils.deAccent(command).equals("re trai")){
            hc05Send("ff5507000205ff00ff00");
            Log.d(TAG, "processVoice: left");
        } else
        if(ConvertUtils.deAccent(command).equals("re phai")){
            hc05Send("ff550700020501ff01ff");
            Log.d(TAG, "processVoice: right");
        } else
        if(ConvertUtils.deAccent(command).equals("dung lai")){
            hc05Send("ff550700020500000000");
            Log.d(TAG, "processVoice: stop");
        }

    }

    @OnClick(R.id.btnSpeak)
    public void btnSpeakClick(View view){
        promptSpeechInput();
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
                        Intent intent = new Intent(SpeechActivity.this, BlueToothActivity.class) ;
                        intent.putExtra("activity",SPEECHACTIVITY);
                        startActivity(intent);
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        Toast.makeText(SpeechActivity.this,getResources().getString(R.string.cancel),Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SpeechActivity.this,getResources().getString(R.string.cancel),Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(SpeechActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
