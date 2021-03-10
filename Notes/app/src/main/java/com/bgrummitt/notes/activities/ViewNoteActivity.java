package com.bgrummitt.notes.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;


import com.bgrummitt.notes.R;
import com.bgrummitt.notes.controller.adapters.ListAdapter;
import com.bgrummitt.notes.controller.databse.DatabaseHelper;
import com.bgrummitt.notes.model.Note;

public class ViewNoteActivity extends AppCompatActivity {

    final static private String TAG = ViewNoteActivity.class.getSimpleName();

    final static public int EDITED_RETURN_RESULT = 13;
    final static public int MOVE_TO_COMPLETED_RESULT = 16;
    final static public int DELETE_FROM_COMPLETED = 34;

    private Intent mIntent;
    private ListAdapter.ListTypes mNoteType;
    private String mNoteSubject;
    private String mNoteBody;
    private TextView mBodyTextView;
    private int mDbID;
    private int mArrayPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        mIntent = getIntent();

        mDbID = mIntent.getIntExtra(ListAdapter.NOTE_ID, -1);
        mNoteSubject = mIntent.getStringExtra(ListAdapter.NOTE_SUBJECT);
        mNoteBody = mIntent.getStringExtra(ListAdapter.NOTE_BODY);
        mNoteType = (ListAdapter.ListTypes)mIntent.getSerializableExtra(ListAdapter.NOTE_TYPE);
        mArrayPosition = mIntent.getIntExtra(ListAdapter.NOTE_POSITION, -1);

        // Get the size of the window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        // Set the size of this activity
        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.7));
        getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);

        // Used to give nice rounded corners now just rounds and replaces space with black
        // getWindow().setBackgroundDrawableResource(R.drawable.note_background_shape);

        // Center the activity
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        // Save the positioning
        getWindow().setAttributes(params);

        mBodyTextView = findViewById(R.id.noteBodyTextView);

        setTitle(mNoteSubject);

        mBodyTextView.setText(mNoteBody);
        mBodyTextView.setMovementMethod(new ScrollingMovementMethod());

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        int id;

        switch (mNoteType){
            case TODO_LIST:
                id = R.menu.menu_note_todo;
                break;
            case COMPLETED_LIST:
                id = R.menu.menu_note_completed;
                break;
            default:
                id = R.menu.menu_note_completed;
        }

        getMenuInflater().inflate(id, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = new Intent();

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.edit_button:
                Log.d(TAG, "Edit Pressed");
                convertActivityToEdit();
                break;
            case R.id.complete_button:
                Log.d(TAG, "Complete Button");
                intent.putExtra(ListAdapter.NOTE_POSITION, mArrayPosition);
                setResult(MOVE_TO_COMPLETED_RESULT, intent);
                finish();
                break;
            case R.id.delete_button:
                Log.d(TAG, "Delete Button");
                intent.putExtra(ListAdapter.NOTE_POSITION, mArrayPosition);
                setResult(DELETE_FROM_COMPLETED, intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Function to take the information in the view not and pass it using an intent
     */
    private void convertActivityToEdit(){
        Intent intent = new Intent(this, EditNoteActivity.class);
        intent.putExtra(ListAdapter.NOTE_SUBJECT, mNoteSubject);
        intent.putExtra(ListAdapter.NOTE_BODY, mNoteBody);
        startActivityForResult(intent, EDITED_RETURN_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "ACTIVITY RESULT");

        switch (resultCode){
            case EDITED_RETURN_RESULT:
                Log.d(TAG, "EDITED NOTE");
                String subject = data.getStringExtra(ListAdapter.NOTE_SUBJECT);
                String body = data.getStringExtra(ListAdapter.NOTE_BODY);
                setTitle(subject);
                mBodyTextView.setText(body);
                editTodoNote(subject, body);
                setReturnIntent(subject, body, mArrayPosition);
                break;
        }
    }

    /**
     * Function to take the edited information and save it to the db
     * @param subject edited subject
     * @param body edited body
     */
    public void editTodoNote(String subject, String body){
        DatabaseHelper dbHelper = new DatabaseHelper(this, "NOTES_DB");
        dbHelper.editNote(DatabaseHelper.TO_COMPLETE_TABLE_NAME, mDbID, subject, body);
        dbHelper.close();

        Intent intent = new Intent();
        intent.putExtra(ListAdapter.NOTE_SUBJECT, subject);
    }

    /**
     * Return proper information in intent
     * @param subject
     * @param body
     * @param position
     */
    public void setReturnIntent(String subject, String body, int position){
        Intent intent = new Intent();
        intent.putExtra(ListAdapter.NOTE_SUBJECT, subject);
        intent.putExtra(ListAdapter.NOTE_BODY, body);
        Log.d(TAG, Integer.toString(position));
        intent.putExtra(ListAdapter.NOTE_POSITION, position);
        setResult(MainActivity.NOTE_EDITED_ACTIVITY_RESULT, intent);
    }

}
