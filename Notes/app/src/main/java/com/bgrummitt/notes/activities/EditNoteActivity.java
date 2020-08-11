package com.bgrummitt.notes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.bgrummitt.notes.R;
import com.bgrummitt.notes.controller.adapters.ListAdapter;

public class EditNoteActivity extends AppCompatActivity {

    private ListAdapter.ListTypes mNoteType;
    private String mNoteSubject;
    private String mNoteBody;
    private EditText mSubjectEditText;
    private EditText mBodyEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Intent intent = getIntent();

        mNoteSubject = intent.getStringExtra(ListAdapter.NOTE_SUBJECT);
        mNoteBody = intent.getStringExtra(ListAdapter.NOTE_BODY);
        mNoteType = (ListAdapter.ListTypes)intent.getSerializableExtra(ListAdapter.NOTE_TYPE);

        // Get the size of the window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        // Set the size of this activity
        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.7));
        getWindow().setBackgroundDrawable(getDrawable(R.drawable.note_background_shape));

        // Center the activity
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        // Save the positioning
        getWindow().setAttributes(params);

        mSubjectEditText = findViewById(R.id.edit_text_subject);
        mBodyEditText = findViewById(R.id.edit_text_body);

        setTitle("Edit Note");

        mSubjectEditText.setText(mNoteSubject);
        mBodyEditText.setText(mNoteBody);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.save_note_button:
                Intent returnIntent = new Intent();
                returnIntent.putExtra(ListAdapter.NOTE_SUBJECT, mSubjectEditText.getText().toString());
                returnIntent.putExtra(ListAdapter.NOTE_BODY, mBodyEditText.getText().toString());
                setResult(ViewNoteActivity.EDITED_RETURN_RESULT, returnIntent);
                finish();
                break;
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
