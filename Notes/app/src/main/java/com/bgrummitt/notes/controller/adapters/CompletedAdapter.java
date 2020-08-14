package com.bgrummitt.notes.controller.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.bgrummitt.notes.R;
import com.bgrummitt.notes.activities.MainActivity;
import com.bgrummitt.notes.model.CompletedNote;
import com.bgrummitt.notes.model.Note;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CompletedAdapter extends ListAdapter {

    private static final String TAG = TODOAdapter.class.getSimpleName();

    private List<Date> mNoteDates;
    private Date mRecentlyDeletedDate;

    public CompletedAdapter(Context context, List<CompletedNote> notes) {
        super(context, convertNotes(notes));

        mNoteDates = new ArrayList<>();

        for(CompletedNote note : notes){
            mNoteDates.add(note.getDateNoteCompleted());
        }
    }

    public static List<Note> convertNotes(List<CompletedNote> notes){
        List<Note> convertedNotes = new ArrayList<>();

        for(CompletedNote note : notes){
            convertedNotes.add(convertNote(note));
        }

        return convertedNotes;
    }

    public static Note convertNote(CompletedNote note){
        return new Note(note.getSubject(), note.getNoteBody(), note.getIsCompleted(), note.getDatabaseID());
    }

    @Override
    public void deleteItem(int position) {
        mRecentlyDeletedItem = mNotes.get(position);
        mRecentlyDeletedDate = mNoteDates.get(position);
        mNotes.remove(position);
        mNoteDates.remove(position);
        changeLinks(position);
        ((MainActivity)mContext).deleteNoteFromCompleted(mRecentlyDeletedItem);
        notifyItemRemoved(position);
        showUndoSingleSnackBar(mRecentlyDeletedItem.getDatabaseID());
    }

    @Override
    protected void showUndoSingleSnackBar(final int idToUndo) {
        View view = ((Activity) mContext).findViewById(R.id.list);
        Snackbar snackbar = Snackbar.make(view, R.string.snack_bar_undo,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertNoteIntoList(mRecentlyDeletedItem, idToUndo);
                mNoteDates.add(idToUndo, mRecentlyDeletedDate);
            }
        });

        snackbar.show();
    }

    public ListTypes getType(){
        return ListTypes.COMPLETED_LIST;
    }

}
