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
import com.bgrummitt.notes.controller.adapters.CompletedAdapter;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback{

    private CompletedAdapter mListAdapter;
    private Drawable iconDelete;
    private final ColorDrawable background;

    public SwipeToDeleteCallback(CompletedAdapter listAdapter) {
        super(0, ItemTouchHelper.LEFT);

        mListAdapter = listAdapter;

        iconDelete = ContextCompat.getDrawable(mListAdapter.getContext(), R.drawable.baseline_delete_white_36);

        background = new ColorDrawable(Color.RED);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        int iconMargin = (itemView.getHeight() - iconDelete.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - iconDelete.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + iconDelete.getIntrinsicHeight();

        if(dX < 0){
            // Being swiped to left
            int iconLeft = itemView.getRight() - iconMargin - iconDelete.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            iconDelete.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setColor(Color.RED);
            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else{
            // Reset icons / background when not being swiped
            background.setBounds(0, 0, 0, 0);
            iconDelete.setBounds(0, 0, 0, 0);
        }

        background.draw(c);
        iconDelete.draw(c);
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

}
