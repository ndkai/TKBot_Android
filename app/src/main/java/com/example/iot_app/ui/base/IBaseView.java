package com.example.iot_app.ui.base;

/*
* The abstraction for all IView like IMainActivity, later
* */
public interface IBaseView {

    void showToast(String msg);

    void showToast(int stringResId);

    void showProgressDialog();

    void hideProgressDialog();

    BaseActivity getBaseActivity();

}
