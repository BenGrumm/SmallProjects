package com.bgrummitt.notes.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bgrummitt.notes.controller.adapters.CompletedAdapter;
import com.bgrummitt.notes.controller.adapters.ListAdapter;
import com.bgrummitt.notes.controller.adapters.TODOAdapter;
import com.bgrummitt.notes.controller.callback.SwipeToDeleteCallback;
import com.bgrummitt.notes.controller.databse.DatabaseHelper;
import com.bgrummitt.notes.model.CompletedNote;
import com.bgrummitt.notes.model.Note;
import com.bgrummitt.notes.R;
import com.bgrummitt.notes.controller.callback.SwipeToCompleteCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    public static final int NOTE_EDITED_ACTIVITY_RESULT = 434;

    private TODOAdapter mTODOListAdapter;
    private CompletedAdapter mCompletedListAdapter;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private DatabaseHelper mDatabaseHelper;
    private ItemTouchHelper mItemTouchHelper;

    private String mCurrentPopulated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView =  findViewById(R.id.list);

        // Set FAB and then set on click listener to create new note
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newNote();
            }
        });

        mDatabaseHelper = new DatabaseHelper(this, "NOTES_DB");

        List<Note> notes = getNotesFromDB();

        initialiseRecyclerView(notes);

        // Make it so fab disappears when scrolling down then reappears when scrolling back up
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // If the rate of change of y > 0 hide else show
                if(dy > 0){
                    fab.hide();
                } else{
                    fab.show();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        // Add "HamBurger" icon in the toolbar with open close action and animation
        // Retrieve draw and navigationView
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Create an action bar toggle with the drawer view and toolbar with string id's for accessibility description
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * When the app is stopped in its lifecycle
     */
    @Override
    protected void onStop() {
        super.onStop();

        // Close the database.
        mDatabaseHelper.close();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        mDatabaseHelper.checkIfDBValid();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_select_all:
                Toast.makeText(this, "Select All", Toast.LENGTH_SHORT).show();
                if(mCurrentPopulated.equals("TODO")) {
                    mTODOListAdapter.selectAll(true);
                    mTODOListAdapter.notifyDataSetChanged();
                }else if(mCurrentPopulated.equals("COMPLETED")){
                    mCompletedListAdapter.selectAll(true);
                    mCompletedListAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.action_de_select_all:
                Toast.makeText(this, "Deselect All", Toast.LENGTH_SHORT).show();
                if(mCurrentPopulated.equals("TODO")) {
                    mTODOListAdapter.selectAll(false);
                    mTODOListAdapter.notifyDataSetChanged();
                }else if(mCurrentPopulated.equals("COMPLETED")){
                    mCompletedListAdapter.selectAll(false);
                    mCompletedListAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.action_delete_selected:
                if(mCurrentPopulated.equals("TODO")) {
                    mTODOListAdapter.deleteSelected();
                }else if(mCurrentPopulated.equals("COMPLETED")){
                    mCompletedListAdapter.deleteSelected();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void newNote(){
        // Build the pop up for the new note
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        // Create a new view with the note layout
        View mView = getLayoutInflater().inflate(R.layout.dialog_new_note, null);

        // Get the edit elements from the view
        final EditText mSubject = mView.findViewById(R.id.subjectEditText);
        final EditText mNotes = mView.findViewById(R.id.mainNotesEditText);
        Button mSaveButton = mView.findViewById(R.id.saveButton);

        // Build the dialog with the view
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();

        // Add the on click listener so when the sections have been filled the note is added and
        // the dialog gets dismissed
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mSubject.getText().toString().isEmpty() && !mNotes.getText().toString().isEmpty()){
                    String subject = mSubject.getText().toString();
                    String note = mNotes.getText().toString();
                    makeNewNote(subject, note);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    public void insertIntoDB(Note noteToInsert){
        mDatabaseHelper.insertNoteIntoTODO(noteToInsert);
    }

    /**
     * Function to move note from T.O.D.O to completed
     * @param note to be moved
     */
    public void markNoteCompleted(Note note){
        mDatabaseHelper.moveNoteToCompleted(note);
    }

    /**
     * Delete note from the completed db
     * @param note to be deleted
     */
    public void deleteNoteFromCompleted(Note note){
        mDatabaseHelper.deleteNoteFromDB(DatabaseHelper.COMPLETED_TABLE_NAME, note.getDatabaseID());
        mDatabaseHelper.editPointersDelete(DatabaseHelper.COMPLETED_TABLE_NAME, note.getDatabaseID(), note.getNextNoteID());
    }

    /**
     * Create a new note and insert it into the db and Recycler view
     * @param subject of the note to be inserted into the db
     * @param note body of the note to be inserted into the db
     */
    public void makeNewNote(String subject, String note){
        Note newNote = new Note(subject, note, false, -1, -1);
        int noteID = mDatabaseHelper.insertNoteIntoTODO(newNote);
        newNote.setDatabaseID(noteID);
        //Add note to db
        // Add the note in the adapter and refresh
        mTODOListAdapter.addNote(newNote);
        mTODOListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.Completed_List:
                setRecyclerViewToCompleted(getCompletedNotesFromDB());
                break;
            case R.id.TODO_List:
                setRecyclerViewToTODO(getNotesFromDB());
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Set up the default look of the recycler view
     * @param notes to populate the recycler view with
     */
    private void initialiseRecyclerView(List<Note> notes){
        mCurrentPopulated = "TODO";
        // Create the list adapter and set the recycler views adapter to the created list adapter
        mTODOListAdapter = new TODOAdapter(this, notes);
        mRecyclerView.setAdapter(mTODOListAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // When at end of list give half oval show still pulling
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);

        // Default Swipe callback
        mItemTouchHelper = new ItemTouchHelper(new SwipeToCompleteCallback(mTODOListAdapter));
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    /**
     * Populate the recycler view with the completed notes
     * @param notes to populate the recycler view
     */
    private void setRecyclerViewToCompleted(List<CompletedNote> notes){
        mCurrentPopulated = "COMPLETED";
        // Create the list adapter and set the recycler views adapter to the created list adapter
        mCompletedListAdapter = new CompletedAdapter(this, notes);
        mRecyclerView.setAdapter(mCompletedListAdapter);

        // Remove the old SwipeCallback and replace with correct Callback
        mItemTouchHelper.attachToRecyclerView(null);
        mItemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(mCompletedListAdapter));
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    /**
     * Populate the recycler view with the notes T.O.D.O
     * @param notes to populate the recycler view
     */
    private void setRecyclerViewToTODO(List<Note> notes){
        mCurrentPopulated = "TODO";
        // Set the new adapter
        mTODOListAdapter = new TODOAdapter(this, notes);
        mRecyclerView.setAdapter(mTODOListAdapter);

        // Remove the old SwipeCallback and replace with correct Callback
        mItemTouchHelper.attachToRecyclerView(null);
        mItemTouchHelper = new ItemTouchHelper(new SwipeToCompleteCallback(mTODOListAdapter));
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

//    /**
//     * Retrieve notes from the database of notes to be completed
//     * @return list of T.O.D.O notes
//     */
//    public List<Note> getNotesFromDB(){
//        Cursor cursor = mDatabaseHelper.getNotesFromDB(DatabaseHelper.TO_COMPLETE_TABLE_NAME);
//
//        List<Note> notes = new ArrayList<>();
//
//        int indexID = cursor.getColumnIndex(DatabaseHelper.ID_COLUMN_NAME);
//        int indexSubject = cursor.getColumnIndex(DatabaseHelper.SUBJECT_COLUMN_NAME);
//        int indexNote = cursor.getColumnIndex(DatabaseHelper.NOTE_COLUMN_NAME);
//
//        if(cursor.moveToFirst()){
//            while(!cursor.isAfterLast()){
//                notes.add(new Note(cursor.getString(indexSubject), cursor.getString(indexNote), false, cursor.getInt(indexID)));
//                cursor.moveToNext();
//            }
//        }
//
//        return notes;
//    }

    /**
     * Retrieve notes from the database of notes to be completed
     * @return list of T.O.D.O notes
     */
    public List<Note> getNotesFromDB(){
        // If empty returns -1
        int currentNoteToGetID = mDatabaseHelper.getIDOfFirstElement(DatabaseHelper.TO_COMPLETE_TABLE_NAME);

        List<Note> notes = new ArrayList<>();
        Cursor tempCursor;

        Log.d(TAG, "First ID = " + currentNoteToGetID);

        while(currentNoteToGetID != -1) {

            tempCursor = mDatabaseHelper.getNoteFromDB(DatabaseHelper.TO_COMPLETE_TABLE_NAME, currentNoteToGetID);
            Log.d(TAG, "TC Size = " + tempCursor.getCount());
            final int indexID = tempCursor.getColumnIndex(DatabaseHelper.ID_COLUMN_NAME);
            final int indexSubject = tempCursor.getColumnIndex(DatabaseHelper.SUBJECT_COLUMN_NAME);
            final int indexNote = tempCursor.getColumnIndex(DatabaseHelper.NOTE_COLUMN_NAME);
            final int indexNextID = tempCursor.getColumnIndex(DatabaseHelper.ID_NEXT_NOTE);

            tempCursor.moveToFirst();

            Note note = new Note(tempCursor.getString(indexSubject),
                    tempCursor.getString(indexNote),
                    false,
                    tempCursor.getInt(indexID),
                    tempCursor.getInt(indexNextID));

            currentNoteToGetID = note.getNextNoteID();

            tempCursor.close();

            notes.add(note);

        }

        return notes;
    }

//    /**
//     * Retrieve notes from the database of completed notes
//     * @return list of completed notes
//     */
//    public List<CompletedNote> getCompletedNotesFromDB(){
//        Cursor cursor = mDatabaseHelper.getNotesFromDB(DatabaseHelper.COMPLETED_TABLE_NAME);
//
//        List<CompletedNote> notes = new ArrayList<>();
//
//        int indexID = cursor.getColumnIndex(DatabaseHelper.ID_COLUMN_NAME);
//        int indexSubject = cursor.getColumnIndex(DatabaseHelper.SUBJECT_COLUMN_NAME);
//        int indexNote = cursor.getColumnIndex(DatabaseHelper.NOTE_COLUMN_NAME);
//        int indexDate = cursor.getColumnIndex(DatabaseHelper.DATE_COLUMN_NAME);
//
//        // For every entry in the cursor retrieve every note and move to the next
//        if(cursor.moveToNext()){
//            while(!cursor.isAfterLast()){
//                notes.add(new CompletedNote(cursor.getString(indexSubject), cursor.getString(indexNote), false, cursor.getInt(indexID), cursor.getString(indexDate)));
//                cursor.moveToNext();
//            }
//        }
//
//        return notes;
//
//    }

    /**
     * Retrieve notes from the database of completed notes
     * @return list of completed notes
     */
    public List<CompletedNote> getCompletedNotesFromDB(){
        int currentNoteToGetID = mDatabaseHelper.getIDOfFirstElement(DatabaseHelper.COMPLETED_TABLE_NAME);

        List<CompletedNote> notes = new ArrayList<>();
        Cursor tempCursor;

        while(currentNoteToGetID != -1) {

            tempCursor = mDatabaseHelper.getNoteFromDB(DatabaseHelper.COMPLETED_TABLE_NAME, currentNoteToGetID);
            final int indexID = tempCursor.getColumnIndex(DatabaseHelper.ID_COLUMN_NAME);
            final int indexSubject = tempCursor.getColumnIndex(DatabaseHelper.SUBJECT_COLUMN_NAME);
            final int indexNote = tempCursor.getColumnIndex(DatabaseHelper.NOTE_COLUMN_NAME);
            final int indexNextID = tempCursor.getColumnIndex(DatabaseHelper.ID_NEXT_NOTE);
            final int indexDate = tempCursor.getColumnIndex(DatabaseHelper.DATE_COLUMN_NAME);
            tempCursor.moveToFirst();

            CompletedNote note = new CompletedNote(tempCursor.getString(indexSubject),
                    tempCursor.getString(indexNote),
                    false,
                    tempCursor.getInt(indexID),
                    tempCursor.getInt(indexNextID),
                    tempCursor.getString(indexDate));

            currentNoteToGetID = note.getNextNoteID();

            tempCursor.close();

            notes.add(note);

        }

        return notes;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int pos;

        switch(resultCode){
            case NOTE_EDITED_ACTIVITY_RESULT:
                mTODOListAdapter.editNote(data.getIntExtra(ListAdapter.NOTE_POSITION, -1), data.getStringExtra(ListAdapter.NOTE_SUBJECT), data.getStringExtra(ListAdapter.NOTE_BODY));
                mTODOListAdapter.notifyDataSetChanged();
                break;
            case ViewNoteActivity.MOVE_TO_COMPLETED_RESULT:
                pos = data.getIntExtra(ListAdapter.NOTE_POSITION, -1);
                try {
                    mTODOListAdapter.deleteItem(pos);
                } catch (ArrayIndexOutOfBoundsException e){
                    Log.d(TAG, e.toString());
                }
                break;
            case ViewNoteActivity.DELETE_FROM_COMPLETED:
                pos = data.getIntExtra(ListAdapter.NOTE_POSITION, -1);
                try {
                    mCompletedListAdapter.deleteItem(pos);
                } catch (ArrayIndexOutOfBoundsException e){
                    Log.d(TAG, e.toString());
                }
        }

    }
}
