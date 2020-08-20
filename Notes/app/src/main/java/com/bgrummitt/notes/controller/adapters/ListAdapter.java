package com.bgrummitt.notes.controller.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.bgrummitt.notes.activities.MainActivity;
import com.bgrummitt.notes.activities.ViewNoteActivity;
import com.bgrummitt.notes.model.Note;
import com.bgrummitt.notes.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class that will take a list of notes and display them in a recycler view
 * deal with notes being added and deleted
 */
public abstract class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private static final String TAG = ListAdapter.class.getSimpleName();

    // Strings used for intents.
    public static final String NOTE_ID = "NOTE_ID";
    public static final String NOTE_SUBJECT = "NOTE_SUBJECT";
    public static final String NOTE_BODY = "NOTE_BODY";
    public static final String NOTE_TYPE = "NOTE_TYPE";
    public static final String NOTE_POSITION = "NOTE_POSITION_IN_ARRAY";

    public final Context mContext;
    public final List<Note> mNotes;
    public List<Note> mDeletedNotes;
    public Note mRecentlyDeletedItem;
    public List<Integer> mDeletedNotePosition;

    public ListAdapter (Context context, List<Note> notes){
        mContext = context;
        mNotes = notes;
    }

    /**
     * Inner class for the display of the elements in the recycler view that will implement an on
     * click listener.
     */
    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView subjectTextView;
        public TextView noteTextView;
        public CheckBox completeCheckBox;

        public ListViewHolder(View itemView) {
            super(itemView);

            subjectTextView = itemView.findViewById(R.id.subjectTextView);
            noteTextView = itemView.findViewById(R.id.mainNoteDisplay);
            completeCheckBox = itemView.findViewById(R.id.CompletedCheck);

            completeCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // If the checkbox is check uncheck, if unchecked check
                    if(completeCheckBox.isChecked()){
                        Log.d(TAG, "Check Box Ticked : " + getLayoutPosition());
                        mNotes.get(getLayoutPosition()).setIsCompleted(true);
                    }else {
                        Log.d(TAG, "Check Box Un-ticked : " + getLayoutPosition());
                        mNotes.get(getLayoutPosition()).setIsCompleted(false);
                    }
                }
            });

            itemView.setOnClickListener(this);
        }

        /**
         * Take note elements and bind them to the view holder
         * @param note assigned to view
         * @param position of view in list
         */
        public void bindList(Note note, final int position){
            subjectTextView.setText(note.getSubject());
            noteTextView.setText(note.getNoteBody());
            completeCheckBox.setChecked(note.getIsCompleted());
        }

        @Override
        public void onClick(View v) {
            // On click start a view note activity
            Note note = mNotes.get(getLayoutPosition());
            Toast.makeText(mContext, subjectTextView.getText(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext, ViewNoteActivity.class);
            intent.putExtra(NOTE_ID, note.getDatabaseID());
            intent.putExtra(NOTE_POSITION, getLayoutPosition());
            intent.putExtra(NOTE_SUBJECT, note.getSubject());
            intent.putExtra(NOTE_BODY, note.getNoteBody());
            intent.putExtra(NOTE_TYPE, getType());
            ((MainActivity)mContext).startActivityForResult(intent, MainActivity.NOTE_EDITED_ACTIVITY_RESULT);
        }
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_layout, parent, false);
        ListViewHolder viewHolder = new ListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.bindList(mNotes.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    /**
     * Get notes in list
     * @return list of notes
     */
    public List<Note> getNotes(){
        return mNotes;
    }

    /**
     * Add a note to the recycler view
     * @param note to add
     */
    public void addNote(Note note){
        mNotes.add(note);
        // Edit previous note pointer if its not the first note (Note is already added)
        if(mNotes.size() > 1) {
            mNotes.get(mNotes.size() - 2).setNextNoteID(note.getDatabaseID());
        }
    }

    public Context getContext(){
        return mContext;
    }

    public abstract void deleteItem(int position);

    protected abstract void showUndoSingleSnackBar(int idToUndo);

    /**
     * Function to edit surrounding pointers of a note
     * @param position of the note in the list from recycler view (+1 from array)
     */
    protected void changeLinks(int position){
        // Recycler view works from 1
        // Arrays from 0 so - 2 to find prev and -1 in arr for current
        if(position > 1){
            mNotes.get(position - 2).setNextNoteID(mNotes.get(position - 1).getDatabaseID());
        }
    }

    /**
     * Function to add a note which has recently been deleted
     * @param noteToUndo note that was deleted
     * @param prevPosition position that note was in
     * @param newID id from the db
     */
    protected void undoLastDelete(Note noteToUndo, int prevPosition, int newID){
        if(prevPosition > 1) {
            mNotes.get(prevPosition - 1).setNextNoteID(newID);
        }
        noteToUndo.setDatabaseID(newID);
        insertNoteIntoList(noteToUndo, prevPosition);
    }

    /**
     * Function to add note to list and re-configure surrounding pointers
     * @param note that is being added
     * @param position of note in the list
     */
    public void insertNoteIntoList(Note note, int position){
        mNotes.add(position, note);
        notifyItemInserted(position);
    }

    /**
     * Function to edit note in recycler view
     * @param position pos in list
     * @param subject new subject
     * @param body new body
     */
    public void editNote(int position, String subject, String body){
        Note note = mNotes.get(position);
        note.setSubject(subject);
        note.setNoteBody(body);
    }

    public abstract ListTypes getType();

    /**
     * Function to check / uncheck all elements in the recycler view
     * @param type type of check true = checked, false = unchecked
     */
    public void selectAll(Boolean type){
        for(Note note : mNotes){
            note.setIsCompleted(type);
        }
    }

    /**
     * Delete all elements in array that are checked
     */
    protected void deleteSelected(){
        mDeletedNotes = new ArrayList<>();
        mDeletedNotePosition = new ArrayList<>();
        int whileILessThan = mNotes.size();
        for(int i = 0; i < whileILessThan; i++){
            if(mNotes.get(i).getIsCompleted()){
                mDeletedNotes.add(mNotes.get(i));
                mNotes.remove(i);
                mDeletedNotePosition.add(i + mDeletedNotes.size());
                // Decrease current element position and max search position
                whileILessThan--;
                i--;
            }
        }

        notifyDataSetChanged();
    }

    /**
     * Undo the recent deleted items from a delete of selected items
     * @param tableUndo table to undo delete from
     */
    public void undoRecentSelectedDeleted(String tableUndo){
        // Do it in reverse order
        for(int i = mDeletedNotes.size() - 1; i >= 0; i--){
            Log.d(TAG, "Undoing Select Delete i = " + i);
            int id = ((MainActivity)mContext).insertIntoDB(tableUndo, mDeletedNotes.get(i));
            // Change the next db ID in the deleted list if just added note was ensuing the prev
            if(i > 0 && mDeletedNotes.get(i-1).getNextNoteID() == mDeletedNotes.get(i).getDatabaseID()){
                mDeletedNotes.get(i - 1).setNextNoteID(id);
            }
            undoLastDelete(mDeletedNotes.get(i), mDeletedNotePosition.get(i) - (i + 1), id);
        }
    }

    /**
     * Show an undo snackbar for selected delete
     * @param dbTable table to delete from
     */
    protected void showUndoMultipleSnackBar(final String dbTable) {
        View view = ((Activity) mContext).findViewById(R.id.list);
        Snackbar snackbar = Snackbar.make(view, R.string.snack_bar_undo,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoRecentSelectedDeleted(dbTable);
                notifyDataSetChanged();
            }
        });
        snackbar.show();
    }

    public enum ListTypes{
        COMPLETED_LIST,
        TODO_LIST
    }

}
