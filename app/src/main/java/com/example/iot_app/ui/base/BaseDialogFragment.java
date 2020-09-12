package com.example.iot_app.ui.base;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import butterknife.Unbinder;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;


public class BaseDialogFragment extends DialogFragment implements IBaseView {
    private BaseActivity mBaseActivity;
    private Unbinder mUnbinder;
    private boolean isBackgroundTransparent;

    public BaseDialogFragment() {
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
    public void showToast(int stringResId) {
        mBaseActivity.showToast(stringResId);
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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(MATCH_PARENT, MATCH_PARENT);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public boolean isBackgroundTransparent() {
        return isBackgroundTransparent;
    }

    public void setBackgroundTransparent(boolean backgroundTransparent) {
        isBackgroundTransparent = backgroundTransparent;
    }

    // override to ignore Can not perform this action after onSaveInstanceState exception
    @SuppressLint("LongLogTag")
    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        try {
            super.show(manager, tag);
        } catch (IllegalStateException e){
            Log.d("Exception in BaseDialogFrament ", "show: " + e.getMessage());
        }
    }

    // override to ignore Can not perform this action after onSaveInstanceState exception
    @SuppressLint("LongLogTag")
    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (IllegalStateException e){
            Log.d("Exception in BaseDialogFrament ", "dismiss: " + e.getMessage());
        }
    }

}
