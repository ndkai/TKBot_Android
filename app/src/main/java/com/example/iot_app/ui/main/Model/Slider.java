package com.example.iot_app.ui.main.Model;

/**
 * Created by HaiLan on Fragment2.
 */
public class Slider {
    private int res;
    private String name;

    public Slider(int res, String name) {
        this.res = res;
        this.name = name;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
