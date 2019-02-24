

package com.vcc.hackathon.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * ItemOffsetDecoration Class - helps us to set spacing/margin between two item in recyclerview.
 */
public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {
    private int mItemOffset;

    /**
     * Constructor
     *
     * @param itemOffset - offset in dp
     */
    public ItemOffsetDecoration(int itemOffset) {
        mItemOffset = itemOffset;
    }

    /**
     * Constructor
     *
     * @param context To get system resource.
     * @param itemOffsetId In dp
     */
    public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    /**
     * Override Method
     *
     * @param outRect Rect to receive the output.
     * @param view    The child view to decorate
     * @param parent  RecyclerView this ItemDecoration is decorating
     * @param state   The current state of RecyclerView.
     */
    @Override
    public void getItemOffsets(
			Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
    }


}
