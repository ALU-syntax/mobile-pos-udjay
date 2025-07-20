package com.udjaya.kasirudjay.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class PaddingDividerItemDecoration extends RecyclerView.ItemDecoration {

    private final Drawable mDivider;
    private final int mOrientation;
    private final int paddingLeft;
    private final int paddingRight;

    public PaddingDividerItemDecoration(Context context, int orientation,
                                        int paddingLeft, int paddingRight) {
        mDivider = ContextCompat.getDrawable(context, android.R.drawable.divider_horizontal_bright); // Atau drawable custom
        mOrientation = orientation; // Simpan orientasi di sini
        this.paddingLeft = paddingLeft;
        this.paddingRight = paddingRight;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent,
                       @NonNull RecyclerView.State state) {
        if (mOrientation == RecyclerView.VERTICAL) {
            drawVerticalWithPadding(c, parent);
        } else {
            drawHorizontalWithPadding(c, parent);
        }
    }

    private void drawVerticalWithPadding(Canvas c, RecyclerView parent) {
        if (mDivider == null) return;

        int left = parent.getPaddingLeft() + paddingLeft;
        int right = parent.getWidth() - parent.getPaddingRight() - paddingRight;

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    private void drawHorizontalWithPadding(Canvas c, RecyclerView parent) {
        // Implementasi jika RecyclerView orientasi horizontal, mirip dengan vertical namun menggambar secara horizontal
    }
}
