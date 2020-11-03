package com.example.iot_app.ui.main.Model;

import com.example.iot_app.R;

import java.util.ArrayList;


public class SliderModel {
    private static ArrayList<Slider> items;

    public static ArrayList<Slider> getItems(){
        if(items == null){
            items = new ArrayList<Slider>();
            items.add(new Slider(R.drawable.robot1, "Play With Block"));
            items.add(new Slider(R.drawable.robot2, "Handed Controller"));
            items.add(new Slider(R.drawable.robot3, "robot3"));
        }
        return items;
    }

}

