package com.bgrummitt.notes.controller.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.bgrummitt.notes.R;
import com.bgrummitt.notes.activities.MainActivity;
import com.bgrummitt.notes.controller.databse.DatabaseHelper;
import com.bgrummitt.notes.model.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for the list of items that still need to be completed
 */
public class TODOAdapter extends ListAdapter {

    private static final String TAG = TODOAdapter.class.getSimpleName();

    private List<Integer> mDeletedNoteIDs;

    public TODOAdapter(Context context, List<Note> notes) {
        super(context, notes);
    }

    /**
     * Function to delete item from both db and list, change pointers and show undo snackbar
     * @param position in the list to delete
     */
    public void deleteItem(int position){
        mRecentlyDeletedItem = mNotes.get(position);
        mNotes.remove(position);
        changeLinks(position);
        int idOFCompletedNote = ((MainActivity)mContext).markNoteCompleted(mRecentlyDeletedItem);
        mRecentlyDeletedItem.setDatabaseID(idOFCompletedNote);
        notifyItemRemoved(position);
        showUndoSingleSnackBar(position);
    }

    /**
     * Function to show snackbar and provide actions for when clicked
     * @param position of item in list before deletion
     */
    protected void showUndoSingleSnackBar(final int position) {
        View view = ((Activity) mContext).findViewById(R.id.list);
        Snackbar snackbar = Snackbar.make(view, R.string.snack_bar_undo,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newDBID = ((MainActivity)mContext).insertIntoDB(DatabaseHelper.TO_COMPLETE_TABLE_NAME, mRecentlyDeletedItem);
                Note fakeNote = new Note("Subject", "Note", true, mRecentlyDeletedItem.getDatabaseID(), -1);
                undoLastDelete(mRecentlyDeletedItem, position, newDBID);
                // TODO DELETE FROM COMPLETED LIST
                ((MainActivity) mContext).deleteNoteFromCompleted(fakeNote);
            }
        });
        snackbar.show();
    }

    /**
     * Get type of the adapter this is
     * @return List type from enum
     */
    public ListTypes getType() {
        return ListTypes.TODO_LIST;
    }

    @Override
    public void deleteSelected() {
        super.deleteSelected();

        mDeletedNoteIDs = new ArrayList<>();

        for(Note note : mDeletedNotes) {
            mDeletedNoteIDs.add(((MainActivity) mContext).markNoteCompleted(note));
        }

        notifyDataSetChanged();

        showUndoMultipleSnackBar(DatabaseHelper.TO_COMPLETE_TABLE_NAME);
    }

    @Override
    public void undoRecentSelectedDeleted(String tableToUndo){
        super.undoRecentSelectedDeleted(tableToUndo);

        //TODO delete from completed
        for(int i = mDeletedNoteIDs.size() - 1; i >= 0; i--){
            Note fakeNote = new Note("subj", "note", true, mDeletedNoteIDs.get(i), -1);
            ((MainActivity)mContext).deleteNoteFromCompleted(fakeNote);
        }
    }

}
