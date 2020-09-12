package com.example.iot_app.ui.others;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpacingDecorator extends RecyclerView.ItemDecoration {
    private int mColNum;
    private int mHorizontalSpace;
    private int mVerticalSpace;
    private boolean mHasSpaceAround;

    public SpacingDecorator(int mColNum, int mVerticalSpace, int mHorizontalSpace, boolean hasSpaceAround) {
        this.mColNum = mColNum;
        this.mHorizontalSpace = mVerticalSpace;
        this.mVerticalSpace = mHorizontalSpace;
        this.mHasSpaceAround = hasSpaceAround;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildPosition(view);
        outRect.top += mHorizontalSpace;
        if (pos % mColNum != mColNum - 1){
            outRect.right += mVerticalSpace;
        }
        if (mHasSpaceAround){
            if (pos % mColNum == 0)
                outRect.left += mVerticalSpace;
            else if (pos % mColNum == mColNum - 1)
                outRect.right += mVerticalSpace;
        }
    }
}
