package com.example.iot_app.ui.base;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    protected int bindViewposition;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void bindView(int position){
        this.bindViewposition = position;
    }
}
