package com.example.iot_app.ui.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.iot_app.di.MyApp;
import com.example.iot_app.di.component.DaggerIActivityComponent;
import com.example.iot_app.di.component.IActivityComponent;
import com.example.iot_app.di.module.ActivityModule;
import com.example.iot_app.ui.others.MyProgressDialog;

public class BaseActivity extends AppCompatActivity implements IBaseView{

    IActivityComponent mIActivityComponent;

    private MyProgressDialog mMyProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        MyApp myApp = (MyApp) getApplication();
        mIActivityComponent = DaggerIActivityComponent.builder()
                .iAppComponent(myApp.getIAppComponent())
                .activityModule(new ActivityModule(this))
                .build();
        mMyProgressDialog = new MyProgressDialog();
    }


    public IActivityComponent getIActivityComponent() {
        return mIActivityComponent;
    }


    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(int stringResId) {
        Toast.makeText(this, stringResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressDialog() {
        mMyProgressDialog.show(getSupportFragmentManager(), "progress");
    }

    @Override
    public void hideProgressDialog() {
        mMyProgressDialog.dismiss();
    }

    @Override
    public BaseActivity getBaseActivity() {
        return this;
    }

    @Override
    protected void onDestroy() {
        if (mMyProgressDialog != null){
            mMyProgressDialog.dismiss();
            mMyProgressDialog = null;
        }
        super.onDestroy();
    }

    public  void replaceFragment(int containerId, Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(fragment.getTag())
                .commit();
    }

    public void replaceFragment(int containerId, Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .replace(containerId, fragment,tag)
                .addToBackStack(fragment.getTag())
                .commit();
    }

    public void addFragment(int containerId, Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .add(containerId, fragment)
                .addToBackStack(fragment.getTag())
                .commit();
    }

    public void addFragment(int containerId, Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .add(containerId, fragment, tag)
                .addToBackStack(fragment.getTag())
                .commit();
    }

    public void backFragment() {
        getSupportFragmentManager().popBackStack();
    }

    public void showKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public void setAutoShowHideKeyboardFor(EditText editText) {
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (hasFocus)
                inputMethodManager.showSoftInput(editText, 0);
            else
                inputMethodManager.hideSoftInputFromWindow(
                    editText.getWindowToken(), 0);
        });
    }


    public void hideKeyboard() {
        View v = getCurrentFocus();
        if (v != null) {
            InputMethodManager manager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(
                    v.getWindowToken(),
                    InputMethodManager.RESULT_UNCHANGED_SHOWN
            );
        }
    }

}
