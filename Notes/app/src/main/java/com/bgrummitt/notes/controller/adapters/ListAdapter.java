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

public abstract class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private static final String TAG = ListAdapter.class.getSimpleName();

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

        public void bindList(Note note, final int position){
            subjectTextView.setText(note.getSubject());
            noteTextView.setText(note.getNoteBody());
            completeCheckBox.setChecked(note.getIsCompleted());
        }

        @Override
        public void onClick(View v) {
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

    public List<Note> getNotes(){
        return mNotes;
    }

    public void addNote(Note note){
        mNotes.add(note);
    }

    public Context getContext(){
        return mContext;
    }

    public abstract void deleteItem(int position);

    protected abstract void showUndoSingleSnackBar(int idToUndo);

    public void changeIDs(int idGreaterThan, int changeBy){
        for(Note note : mNotes){
            if(note.getDatabaseID() > idGreaterThan){
                note.setDatabaseID(note.getDatabaseID() + changeBy);
            }
        }
    }

    public void resetIDs(){
        for(int i = 0; i < mNotes.size(); i++){
            mNotes.get(i).setDatabaseID(i+1);
        }
    }

    public void insertNoteIntoList(Note note, int position){
        mNotes.add(position, note);
        notifyItemInserted(position);
    }

    public void editNote(int position, String subject, String body){
        Note note = mNotes.get(position);
        note.setSubject(subject);
        note.setNoteBody(body);
    }

    public abstract ListTypes getType();

    public void selectAll(Boolean type){
        for(Note note : mNotes){
            note.setIsCompleted(type);
        }
    }

    public void removeAndRestructureNotes(int position){
        mNotes.remove(position);
        changeIDs(mRecentlyDeletedItem.getDatabaseID(), -1);
    }

    public void deleteSelected(){
        mDeletedNotes = new ArrayList<>();
        mDeletedNotePosition = new ArrayList<>();
        int whileILessThan = mNotes.size();
        for(int i = 0; i < whileILessThan; i++){
            if(mNotes.get(i).getIsCompleted()){
                mDeletedNotes.add(mNotes.get(i));
                mNotes.remove(i);
                mDeletedNotePosition.add(i + mDeletedNotes.size());
                whileILessThan--;
                i--;
            }
        }
        Log.d(TAG, "Number To Delete : " + mDeletedNotes.size());
    }

    public void undoRecentSelectedDeleted(){
        for(int i = 0; i < mDeletedNotes.size(); i++){
            mNotes.add(mDeletedNotePosition.get(i) - 1, mDeletedNotes.get(i));
        }
    }

    public enum ListTypes{
        COMPLETED_LIST,
        TODO_LIST
    }

}
