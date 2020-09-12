package com.example.iot_app.ui.base;

import android.content.Context;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import butterknife.Unbinder;


public class BaseFragment extends Fragment implements IBaseView{
    private BaseActivity mBaseActivity;
    private Unbinder mUnbinder; //  Butterknife.bind()

    public BaseFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity)
            mBaseActivity = (BaseActivity) context;
    }

    @Override
    public void onDestroyView() {
        if (mUnbinder != null) mUnbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        if (mBaseActivity != null) mBaseActivity = null;
        super.onDetach();
    }

    @Override
    public void showToast(String msg) {
        mBaseActivity.showToast(msg);
    }

    @Override
    public void showProgressDialog() {
        getBaseActivity().showProgressDialog();
    }

    @Override
    public void hideProgressDialog() {
        getBaseActivity().hideProgressDialog();
    }

    public void setUnbinder(Unbinder mUnbinder) {
        this.mUnbinder = mUnbinder;
    }

    public BaseActivity getBaseActivity() {
        return mBaseActivity;
    }

    @Override
    public void showToast(int stringResId) {
        mBaseActivity.showToast(stringResId);
    }

}
