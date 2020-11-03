package com.example.iot_app.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.iot_app.ui.blockly_activity.BlocklyActivity;

import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class ConvertUtils {


    public static int getSensor(String hex){
        //ff 55 00 02 8d b0 cc 40 0d 0a
        String sa[] = hex.split(" ");
        int sensorVal =   com.example.iot_app.utils.HexUtil.getDecimal("0x"+ sa[7] + sa[6] + sa[5] + sa[4]);
        return (int) com.example.iot_app.utils.HexUtil.hexToFloat(sensorVal);
    }

    public static void getUltra(String s){
        //ff 55 00 ff 55 00 16 2c f7 04 41 0d 0a ff 55 0d 0a 00 16 2c f7 04
//        String s = "ff 55 00 ff 55 00 16 2c f7 04 41 0d 0a ff 55 0d 0a 00 16 2c f7 04";
        try{
            if(s.contains("55 00 16")){
                int index = s.indexOf("55 00 16") + "55 00 16".length();
                String sa[] = s.substring(index+1,s.length()).split(" ");
                Log.d("TAG", "getUltra: yyy"+sa[0] + sa[1] + sa[2] + sa[3]);
                int sensorVal =   com.example.iot_app.utils.HexUtil.getDecimal("0x"+ sa[3] + sa[2] + sa[1] + sa[0]);
                BlocklyActivity.sensorData = (int) HexUtil.hexToFloat(sensorVal);
                Log.d("TAG", "getUltra: xxx"+BlocklyActivity.sensorData);
            }
        }
        catch (Exception e){
        }
    }

    public static void getPotenl(String s){
        //ff 55 00 ff 55 00 16 2c f7 04 41 0d 0a ff 55 0d 0a 00 16 2c f7 04
//        String s = "ff 55 00 ff 55 00 16 2c f7 04 41 0d 0a ff 55 0d 0a 00 16 2c f7 04";
        try{
            if(s.contains("55 00 0c")){
                int index = s.indexOf("55 00 0c") + "55 00 0c".length();
                String sa[] = s.substring(index+1,s.length()).split(" ");
                Log.d("TAG", "getPotenl: yyy"+sa[0] + sa[1] + sa[2] + sa[3]);
                int sensorVal =   com.example.iot_app.utils.HexUtil.getDecimal("0x"+ sa[3] + sa[2] + sa[1] + sa[0]);
                BlocklyActivity.potenlData = (int) HexUtil.hexToFloat(sensorVal);
                Log.d("TAG", "getPotenl: xxx"+BlocklyActivity.potenlData);
            }
        }
        catch (Exception e){
        }
    }

    public static void getLinevalue (String s){
        //ff 55 00 ff 55 00 16 2c f7 04 41 0d 0a ff 55 0d 0a 00 16 2c f7 04
//        String s = "ff 55 00 ff 55 00 16 2c f7 04 41 0d 0a ff 55 0d 0a 00 16 2c f7 04";
        try{
            if(s.contains("11 00 00")){
                int index = s.indexOf("11 00 00") + "11 00 00".length();
                String sa[] = s.substring(index+1,s.length()).split(" ");
                BlocklyActivity.lineValueS1 = sa[0];
                BlocklyActivity.lineValueS2 = sa[1];
                Log.d("TAG", "getLinevalue:"+ BlocklyActivity.lineValueS1 +  BlocklyActivity.lineValueS2);
            }
        }
        catch (Exception e){
        }
    }

    public static void getTemperature(String s){
        //ff 55 00 ff 55 00 16 2c f7 04 41 0d 0a ff 55 0d 0a 00 16 2c f7 04
//        String s = "ff 55 00 ff 55 00 16 2c f7 04 41 0d 0a ff 55 0d 0a 00 16 2c f7 04";
        try{
            if(s.contains("55 00 0b")){
                int index = s.indexOf("55 00 0b") + "55 00 0b".length();
                String sa[] = s.substring(index+1,s.length()).split(" ");
                Log.d("TAG", "getTemperature: yyy"+sa[0] + sa[1] + sa[2] + sa[3]);
                int sensorVal =   com.example.iot_app.utils.HexUtil.getDecimal("0x"+ sa[3] + sa[2] + sa[1] + sa[0]);
                BlocklyActivity.temperatureData = (int) HexUtil.hexToFloat(sensorVal);
                Log.d("TAG", "getTemperature: xxx"+BlocklyActivity.temperatureData);
            }
        }
        catch (Exception e){
        }
    }

    public static void getAvoidValue(String s) {
        if (s.contains("ff 55 00 02 00 0d 0a") && !s.contains("ff 55 00 02 01 0d 0a")) {
            Log.d("TAG", "getAvoidValue: true");
           BlocklyActivity.avoidData = true;   // touch
        } else {
            if (s.contains("ff 55 00 02 01 0d 0a") && !s.contains("ff 55 00 02 00 0d 0a")) {
                Log.d("TAG", "getAvoidValue: false");
                BlocklyActivity.avoidData = false;  //no touch
            } else if (s.indexOf("ff 55 00 02 00 0d 0a") < s.indexOf("ff 55 00 02 01 0d 0a")) {
                Log.d("TAG", "getAvoidValue: true");
                BlocklyActivity.avoidData = true;   // touch
            } else if (s.indexOf("ff 55 00 02 00 0d 0a") > s.indexOf("ff 55 00 02 01 0d 0a")) {
                Log.d("TAG", "getAvoidValue: false");
                BlocklyActivity.avoidData = false;
            }
        }

    }


    public static void requestBlePermissions(final Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, requestCode);
    }

    public static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

}
