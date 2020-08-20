package com.bgrummitt.notes.controller.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.bgrummitt.notes.R;
import com.bgrummitt.notes.activities.MainActivity;
import com.bgrummitt.notes.controller.databse.DatabaseHelper;
import com.bgrummitt.notes.model.CompletedNote;
import com.bgrummitt.notes.model.Note;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CompletedAdapter extends ListAdapter {

    private static final String TAG = TODOAdapter.class.getSimpleName();

    public CompletedAdapter(Context context, List<Note> notes) {
        super(context, notes);
    }

    @Override
    public void deleteItem(int position) {
        mRecentlyDeletedItem = mNotes.get(position);
        mNotes.remove(position);
        changeLinks(position);
        ((MainActivity)mContext).deleteNoteFromCompleted(mRecentlyDeletedItem);
        notifyItemRemoved(position);
        showUndoSingleSnackBar(position);
    }

    @Override
    public void deleteSelected(){
        super.deleteSelected();

        for(Note note : mDeletedNotes) {
            ((MainActivity) mContext).deleteNoteFromCompleted(note);
        }

        notifyDataSetChanged();

        showUndoMultipleSnackBar(DatabaseHelper.COMPLETED_TABLE_NAME);
    }

    @Override
    protected void showUndoSingleSnackBar(final int position) {
        View view = ((Activity) mContext).findViewById(R.id.list);
        Snackbar snackbar = Snackbar.make(view, R.string.snack_bar_undo,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reinsert note to db and get returned id of note in db
                int newDBID = ((MainActivity)mContext).insertIntoDB(DatabaseHelper.COMPLETED_TABLE_NAME, mRecentlyDeletedItem);
                undoLastDelete(mRecentlyDeletedItem, position, newDBID);
            }
        });

        snackbar.show();
    }

    public ListTypes getType(){
        return ListTypes.COMPLETED_LIST;
    }

}
