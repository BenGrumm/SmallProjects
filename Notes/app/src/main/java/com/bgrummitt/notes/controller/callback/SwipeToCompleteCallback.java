package com.bgrummitt.notes.controller.callback;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.bgrummitt.notes.R;
import com.bgrummitt.notes.controller.adapters.TODOAdapter;

/**
 * Class that allows the views in the recycler view to be swiped and the swipes have a background
 * with image behind view
 */
public class SwipeToCompleteCallback extends ItemTouchHelper.SimpleCallback {

    private TODOAdapter mListAdapter;
    private Drawable iconComplete;
    private final ColorDrawable background;

    public SwipeToCompleteCallback(TODOAdapter adapter){
        super(0, ItemTouchHelper.RIGHT);

        mListAdapter = adapter;

        background = new ColorDrawable(Color.BLUE);
        iconComplete = ContextCompat.getDrawable(mListAdapter.getContext(), R.drawable.baseline_done_outline_white_36);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        mListAdapter.deleteItem(position); 
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        int iconMargin = (itemView.getHeight() - iconComplete.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - iconComplete.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + iconComplete.getIntrinsicHeight();

        if(dX > 0){
            // Being swiped to right
            int iconLeft = itemView.getLeft() + iconMargin + iconComplete.getIntrinsicWidth();
            int iconRight = itemView.getLeft() + iconMargin;
            iconComplete.setBounds(iconRight, iconTop, iconLeft, iconBottom);

            background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
        }else{
            // Reset icons / background when not being swiped
            background.setBounds(0, 0, 0, 0);
            iconComplete.setBounds(0, 0, 0, 0);
        }

        background.draw(c);
        iconComplete.draw(c);

    }
}
