package com.example.iot_app.ui.bluetooth.hc05;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.iot_app.R;
import com.example.iot_app.ui.blockly_activity.BlocklyActivity;
import com.example.iot_app.ui.handed_controller.HandedControllerActivity;
import com.example.iot_app.ui.qr_activity.QrActivity;
import com.example.iot_app.ui.speech_activity.SpeechActivity;

import java.util.ArrayList;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BlueToothActivity extends Activity {

    @BindView(R.id.device_listView)
    ListView deviceList;

    //Bluetooth
    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADDRESS = "device_address";
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        setContentView(R.layout.bluetooth_fragment);
        ButterKnife.bind(this);
        if(myBluetooth == null)
        {
            Toast.makeText(this, "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
        }
        else if(!myBluetooth.isEnabled())
        {
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon,1);
        }
        else {
            getPairedDeviceList();
        }
    }

    @OnClick(R.id.button)
    public void pairedDevicesList()
    {
        getPairedDeviceList();
    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick (AdapterView<?> av, View v, int arg2, long arg3)
        {
            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            // Make an intent to start next activity.
            Intent previousIntent = getIntent();
            String s = previousIntent.getStringExtra("activity");
            //open blockly activity
            if (s.equals(BlocklyActivity.BLOCKLY_ACTIVITY)){
                Intent i = new Intent(BlueToothActivity.this, BlocklyActivity.class);
                //Change the activity.
                i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
                Log.d("TAG", "onItemClick:" +address);
                startActivity(i);
                finish();
            }
            //open controller activity
            if (s.equals(HandedControllerActivity.HANDEDACTIVITY)){
                Intent i = new Intent(BlueToothActivity.this, HandedControllerActivity.class);
                //Change the activity.
                i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
                Log.d("TAG", "onItemClick:" +address);
                startActivity(i);
                finish();
            }
            //open speech activity
            if (s.equals(SpeechActivity.SPEECHACTIVITY)){
                Intent i = new Intent(BlueToothActivity.this, SpeechActivity.class);
                //Change the activity.
                i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
                Log.d("TAG", "onItemClick:" +address);
                startActivity(i);
                finish();
            }
            //open qr activity
            if (s.equals(QrActivity.QRACTIVITY)){
                Intent i = new Intent(BlueToothActivity.this, QrActivity.class);
                //Change the activity.
                i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
                Log.d("TAG", "onItemClick:" +address);
                startActivity(i);
                finish();
            }

        }
    };

    private  void getPairedDeviceList(){
        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if (pairedDevices.size()>0)
        {
            for(BluetoothDevice bt : pairedDevices)
            {
                list.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
            }
        }
        else
        {
            Toast.makeText(this, "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        deviceList.setAdapter(adapter);
        deviceList.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked
    }

}
