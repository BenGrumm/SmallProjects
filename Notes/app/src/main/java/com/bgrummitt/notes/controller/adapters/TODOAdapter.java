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

import java.util.List;

public class TODOAdapter extends ListAdapter {

    private static final String TAG = TODOAdapter.class.getSimpleName();

    public TODOAdapter(Context context, List<Note> notes) {
        super(context, notes);
    }

    public void deleteItem(int position){
        mRecentlyDeletedItem = mNotes.get(position);
        mNotes.remove(position);
        notifyItemRemoved(position);
        showUndoSingleSnackBar(position);
    }

    protected void showUndoSingleSnackBar(final int idToUndo) {
        View view = ((Activity) mContext).findViewById(R.id.list);
        Snackbar snackbar = Snackbar.make(view, R.string.snack_bar_undo,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertNoteIntoList(mRecentlyDeletedItem, idToUndo);
            }
        });
        snackbar.addCallback(new Snackbar.Callback(){

            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                Log.d(TAG, "Snackbar Dismissed");
                if(event != Snackbar.Callback.DISMISS_EVENT_ACTION){
                    Log.d(TAG, "Snackbar Dismissed By Timeout / New SnackBar / Swipe");
                    removeItemFromDB(mRecentlyDeletedItem);
                }
            }

        });
        snackbar.show();
    }

    private void removeItemFromDB(Note note){
        changeIDs(note.getDatabaseID(), -1);
        ((MainActivity)mContext).markNoteCompleted(note);
    }

    public ListTypes getType() {
        return ListTypes.TODO_LIST;
    }

    @Override
    public void deleteSelected() {
        super.deleteSelected();
        notifyDataSetChanged();
        showUndoMultipleSnackBar();
    }

    protected void showUndoMultipleSnackBar() {
        View view = ((Activity) mContext).findViewById(R.id.list);
        Snackbar snackbar = Snackbar.make(view, R.string.snack_bar_undo,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoRecentSelectedDeleted();
                notifyDataSetChanged();
            }
        });
        snackbar.addCallback(new Snackbar.Callback(){

            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                Log.d(TAG, "Snackbar Dismissed");
                if(event != Snackbar.Callback.DISMISS_EVENT_ACTION){
                    Log.d(TAG, "Snackbar Dismissed By Timeout / New SnackBar / Swipe");
                    for(int i = 0; i < mDeletedNotes.size(); i++){
                        Log.d(TAG, "deleting");
                        mDeletedNotes.get(i).setDatabaseID(mDeletedNotes.get(i).getDatabaseID() - i);
                        ((MainActivity)mContext).markNoteCompleted(mDeletedNotes.get(i));
                    }
                    resetIDs();
                }
            }

        });
        snackbar.show();
    }

}
