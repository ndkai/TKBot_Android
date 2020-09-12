package com.example.iot_app.data.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.iot_app.di.annotation.AppContext;

import java.util.concurrent.atomic.LongAccumulator;

import javax.inject.Inject;

public class Pref implements IPref{
    private static final String PREF_NAME = "IOT_APP";
    private static final String PREF_CURRENT_LANGUAGE = "PREF_CURRENT_LANGUAGE";
    private static final String PREF_USER_EMAIL = "USER_EMAIL";
    private SharedPreferences mPref;

    @Inject
    public Pref(@AppContext Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) ;
    }


    @Override
    public void setCurrentLanguage(String language) {
        mPref.edit().putString(PREF_CURRENT_LANGUAGE, language).commit();
        Log.d("TAG", "setCurrentLanguage: ");
    }

    @Override
    public String getCurrentLanguage() {
        Log.d("TAG",  "setCurrentLanguage: ");
        return mPref.getString(PREF_CURRENT_LANGUAGE,"");
    }
}
