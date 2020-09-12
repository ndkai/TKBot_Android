package com.example.iot_app.ui.blockly_activity.webview;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.example.iot_app.ui.blockly_activity.BlocklyActivity;
import com.example.iot_app.utils.HexUtil;
import com.google.android.gms.common.util.Hex;

public class WebAppInterface {
    private static String TAG = "WebAppInterface";
    Context mContext;

    /** Instantiate the interface and set the context */
    public WebAppInterface(Context c) {
        mContext = c;
    }

    @JavascriptInterface
    public int getSensor() {
        return BlocklyActivity.sensorData;
    }

    @JavascriptInterface
    public void moveBackward(int data) {
        Log.d(TAG, "moveBackward: "+ data);
        switch (data){
            case 255:
                BlocklyActivity.writeBle("ff5507000205ff0001ff");
                break;
            case 100:
                BlocklyActivity.writeBle("ff550700020564009cff");
                break;
            case 50:
                BlocklyActivity.writeBle("ff55070002053200ceff");
                break;
            case 0:
                BlocklyActivity.writeBle("ff550700020500000000");
                break;
            case -50:
                BlocklyActivity.writeBle("ff5507000205ceff3200");
                break;
            case -100:
                BlocklyActivity.writeBle("ff55070002059cff6400");
                break;
            case -255:
                BlocklyActivity.writeBle("ff550700020501ffff00");
                break;

        }

    }

    @JavascriptInterface
    public void moveForward(int data) {
        Log.d(TAG, "moveForward: "+ data);
        switch (data){
            case 255:
                BlocklyActivity.writeBle("ff550700020501ffff00");
                break;
            case 100:
                BlocklyActivity.writeBle("ff55070002059cff6400");
                break;
            case 50:
                BlocklyActivity.writeBle("ff5507000205ceff3200");
                break;
            case 0:
                BlocklyActivity.writeBle("ff550700020500000000");
                break;
            case -50:
                BlocklyActivity.writeBle("ff55070002053200ceff");
                break;
            case -100:
                BlocklyActivity.writeBle("ff550700020564009cff");
                break;
            case -255:
                BlocklyActivity.writeBle("ff5507000205ff0001ff");
                break;

        }
    }

    @JavascriptInterface
    public int requestSensor(String port) {
        String mess = "ff5504000101"+ port;
//        String mess = "ff5504000136"+ port;
        BlocklyActivity.writeBle(mess);
        return BlocklyActivity.sensorData;
    }

    @JavascriptInterface
    public void turnRight(int data) {
        Log.d(TAG, "turnRight: "+ data);
        switch (data){
            case 255:
                BlocklyActivity.writeBle("ff550700020501ff01ff");
                break;
            case 100:
                BlocklyActivity.writeBle("ff55070002059cff9cff");
                break;
            case 50:
                BlocklyActivity.writeBle("ff5507000205ceffceff");
                break;
            case 0:
                BlocklyActivity.writeBle("ff550700020500000000");
                break;
            case -50:
                BlocklyActivity.writeBle("ff550700020532003200");
                break;
            case -100:
                BlocklyActivity.writeBle("ff550700020564006400");
                break;
            case -255:
                BlocklyActivity.writeBle("ff5507000205ff00ff00");
                break;

        }
    }

    @JavascriptInterface
    public void turnLeft(int data) {
        Log.d(TAG, "turnLeft: "+ data);
        switch (data){
            case 255:
                BlocklyActivity.writeBle("ff5507000205ff00ff00");
                break;
            case 100:
                BlocklyActivity.writeBle("ff550700020564006400");
                break;
            case 50:
                BlocklyActivity.writeBle("ff550700020532003200");
                break;
            case 0:
                BlocklyActivity.writeBle("ff550700020500000000");
                break;
            case -50:
                BlocklyActivity.writeBle("ff5507000205ceffceff");
                break;
            case -100:
                BlocklyActivity.writeBle("ff55070002059cff9cff");
                break;
            case -255:
                BlocklyActivity.writeBle("ff550700020501ff01ff");
                break;

        }
    }

    @JavascriptInterface
    public void moveMotor1(int data) {
        Log.d(TAG, "moveMotor1: "+ data);
        switch (data){
            case 255:
                BlocklyActivity.writeBle("ff550600020a0901ff");
                break;
            case 100:
                BlocklyActivity.writeBle("ff550600020a099cff");
                break;
            case 50:
                BlocklyActivity.writeBle("ff550600020a09ceff");
                break;
            case 0:
                BlocklyActivity.writeBle("ff550600020a090000");
                break;
            case -50:
                BlocklyActivity.writeBle("ff550600020a093200");
                break;
            case -100:
                BlocklyActivity.writeBle("ff550600020a096400");
                break;
            case -255:
                BlocklyActivity.writeBle("ff550600020a09ff00");
                break;
        }
    }

    @JavascriptInterface
    public void moveMotor2(int data) {
        Log.d(TAG, "moveMotor2: "+ data);
        switch (data){
            case 255:
                BlocklyActivity.writeBle("ff550600020a0aff00");
                break;
            case 100:
                BlocklyActivity.writeBle("ff550600020a0a6400");
                break;
            case 50:
                BlocklyActivity.writeBle("ff550600020a0a3200");
                break;
            case 0:
                BlocklyActivity.writeBle("ff550600020a0a0000");
                break;
            case -50:
                BlocklyActivity.writeBle("ff550600020a0aceff");
                break;
            case -100:
                BlocklyActivity.writeBle("ff550600020a0a9cff");
                break;
            case -255:
                BlocklyActivity.writeBle("ff550600020a0a01ff");
                break;
        }
    }

    @JavascriptInterface
    public void servo(String port, String slot,int angle){
        //3 cặp cuối lần lượt là porti, slot, angle
        String mess = "ff550600020b";
        String  ag = Integer.toHexString(angle);
        mess += port;
        mess += slot;
        if(angle == 0){
            mess += "00";
        }
        else{
            mess += ag;
        }
       Toast.makeText(mContext, mess, Toast.LENGTH_SHORT).show();

        BlocklyActivity.writeBle(mess);
    }

    @JavascriptInterface
    public void lineFollow1(String port){
       // Toast.makeText(mContext, "lineFollow1" +port, Toast.LENGTH_SHORT).show();
        // BlocklyActivity.writeBle("ff550600020b04015a");
    }

    @JavascriptInterface
    public void lineFollow2(String port,String side, String color){
      //  Toast.makeText(mContext, "lineFollow2" + port+side+color, Toast.LENGTH_SHORT).show();

        // BlocklyActivity.writeBle("ff550600020b04015a");
    }


    @JavascriptInterface
    public boolean  getTouchVal(String port){
          //yêu cầu trả về touch
        String mess = "ff5504000133"+ port;
        BlocklyActivity.writeBle(mess);
        return BlocklyActivity.touchData;
    }
    @JavascriptInterface
    public void requestTouch(String port){
        String mess = "ff5504000133"+ port;
        BlocklyActivity.writeBle(mess);
    }

    @JavascriptInterface
    public int lightSensor(String port){
            return BlocklyActivity.lightData;
    }


    @JavascriptInterface
    public int getSoundVal(String port){


        return BlocklyActivity.soundData;
    }

    @JavascriptInterface
    public int getTemVal(String port, String slot){
        return BlocklyActivity.temperatureData;
    }

    @JavascriptInterface
    public int isStop(){
         return BlocklyActivity.isStop;
    }

}
