package com.bgrummitt.notes.controller.databse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bgrummitt.notes.model.CompletedNote;
import com.bgrummitt.notes.model.Note;

import java.util.Calendar;
import java.util.Locale;

/**
 * Class that deals with all db interaction
 * Extends SQLiteOpenHelper
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private final static String TAG = DatabaseHelper.class.getSimpleName();

    public final static String TO_COMPLETE_TABLE_NAME = "NOTES_TO_COMPLETE";
    public final static String COMPLETED_TABLE_NAME = "NOTES_COMPLETED";
    public final static String ID_COLUMN_NAME = "NOTE_ID";
    public final static String SUBJECT_COLUMN_NAME = "SUBJECT";
    public final static String NOTE_COLUMN_NAME = "NOTE";
    public final static String DATE_COLUMN_NAME = "DATE_COMPLETED";
    public final static String ID_NEXT_NOTE = "NEXT_LINKED_NOTE";

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public DatabaseHelper(Context context, String db_name) {
        super(context, db_name, null, 1);

        this.mContext = context;

        mDatabase = getWritableDatabase();

    }

    @Override
    public synchronized void close() {
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CreateTableNotesToComplete = "CREATE TABLE " + TO_COMPLETE_TABLE_NAME + "(" + ID_COLUMN_NAME +" INTEGER PRIMARY KEY AUTOINCREMENT, " + ID_NEXT_NOTE + " INTEGER, " + SUBJECT_COLUMN_NAME + " TEXT, " + NOTE_COLUMN_NAME + " TEXT)";
        String CreateTableNotesCompleted = "CREATE TABLE " + COMPLETED_TABLE_NAME + "(" + ID_COLUMN_NAME +" INTEGER PRIMARY KEY AUTOINCREMENT, " + ID_NEXT_NOTE + " INTEGER, " + SUBJECT_COLUMN_NAME + " TEXT, " + NOTE_COLUMN_NAME + " TEXT, " + DATE_COLUMN_NAME + " DATE)";

        db.execSQL(CreateTableNotesCompleted);
        db.execSQL(CreateTableNotesToComplete);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + COMPLETED_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TO_COMPLETE_TABLE_NAME);

        onCreate(db);
    }


     /**
      * Insert a note in a given position in the specified table
      * @param tableName table for note to be inserted to
      * @param note the note to be inserted
      * @return new db id
      */
    public int insertNoteIntoTable(String tableName, Note note){
        // Add note to end of the list with null pointer
        ContentValues cv = new ContentValues();
        cv.put(SUBJECT_COLUMN_NAME, note.getSubject());
        cv.put(NOTE_COLUMN_NAME, note.getNoteBody());

        if(tableName.equals(COMPLETED_TABLE_NAME)){
            CompletedNote cNote = (CompletedNote) note;
            cv.put(DATE_COLUMN_NAME, cNote.convertDateToString(cNote.getDateNoteCompleted()));
        }

        // Will return -1 if error else id
        int newID = (int) mDatabase.insert(tableName, null, cv);
        Log.d(TAG, Long.toString(newID));

        // Configure surrounding pointers to position item correctly
        editPointersInsert(tableName, newID, note.getNextNoteID());

        return newID;
    }

    /**
     * Function to move a given note from the T.O.D.O db to the completed db
     * @param note to be removed from one and added to another
     * @return ID of the note in the completed DB
     */
    public int moveNoteToCompleted(Note note){

        deleteNoteFromDB(TO_COMPLETE_TABLE_NAME, note.getDatabaseID());

        editPointersDelete(TO_COMPLETE_TABLE_NAME, note.getDatabaseID(), note.getNextNoteID());

        CompletedNote cNote = new CompletedNote(note, Calendar.getInstance().getTime(), -1);

        int completedTableID = insertNoteIntoTable(COMPLETED_TABLE_NAME, cNote);

        Log.d(TAG, Integer.toString(completedTableID));

        return completedTableID;

    }

    /**
     * Function to edit the pointers around an element that is being deleted and then deleted the
     * element
     * @param tableName table of the element to delete from
     * @param noteIDToEdit id in the db of note to delete
     * @param nextNoteID id of note deleted note points to
     */
    public void editPointersDelete(String tableName, int noteIDToEdit, int nextNoteID){
        String queryIDPrevNote = "SELECT " + ID_COLUMN_NAME +
                " FROM "+ tableName +
                " WHERE " + ID_NEXT_NOTE + " = " + noteIDToEdit + ";";

        Cursor prevNoteID = mDatabase.rawQuery(queryIDPrevNote, null);

        // Note is not first element
        if(prevNoteID.getCount() != 0 && prevNoteID.moveToFirst()){
            // Set prev note next id to current note next id
            String updateQuery =    "UPDATE " + tableName +
                                    " SET " + ID_NEXT_NOTE + " = " + nextNoteID +
                                    " WHERE " + ID_COLUMN_NAME + " = " + prevNoteID.getInt(0) + ";";

            mDatabase.execSQL(updateQuery);
        }

        prevNoteID.close();
        deleteNoteFromDB(tableName, noteIDToEdit);
    }

    /**
     * Function to edit the pointers of surrounding elements in the database after either reinsert or
     * a note was added.
     * NOTE THE NOTE BEING ADDED SHOULD HAVE NO NEXT ID YET
     * @param tableName
     * @param noteID
     * @param oldNextID
     */
    public void editPointersInsert(String tableName, int noteID, int oldNextID){
        String queryIDPrevNote = "SELECT " + ID_COLUMN_NAME +
                " FROM "+ tableName +
                " WHERE " + ID_NEXT_NOTE + " = " + oldNextID + ";";

        Cursor prevNoteID = mDatabase.rawQuery(queryIDPrevNote, null);

        // If it was not the first element update the previous element to point here
        if(prevNoteID.getCount() != 0 && prevNoteID.moveToFirst()) {
            int idColumnIndex = prevNoteID.getColumnIndex(ID_COLUMN_NAME);
            // Set prev note next id to current note next id
            String updateQuery = "UPDATE " + tableName +
                        " SET " + ID_NEXT_NOTE + " = " + noteID + " " +
                        " WHERE " + ID_COLUMN_NAME + " = " + prevNoteID.getInt(idColumnIndex) + ";";

            mDatabase.execSQL(updateQuery);
            prevNoteID.close();
        }

        // Update where the inserted note points to
        String updateNoteNext = "UPDATE " + tableName +
                " SET " + ID_NEXT_NOTE + " = " + oldNextID +
                " WHERE " + ID_COLUMN_NAME + " = " + noteID + ";";

        mDatabase.execSQL(updateNoteNext);
    }

    /**
     * Function to get ID of the first element in the table (one that no other element points to)
     * @param tableName name of table to get id from
     * @return id
     */
    public int getIDOfFirstElement(String tableName){
        String query = "SELECT " + ID_COLUMN_NAME +
                        " FROM " + tableName +
                        " WHERE " + ID_COLUMN_NAME + " NOT IN " +
                            "(SELECT " + ID_NEXT_NOTE + " FROM " + tableName + ");";

        Cursor result = mDatabase.rawQuery(query, null);

        Log.d(TAG, "Number Of Results = " + Integer.toString(result.getCount()));
        Log.d(TAG, query);

        if(result.getCount() == 0) {
            result.close();
            return -1;
        }else {
            result.moveToFirst();
            int id = result.getInt(0);
            result.close();
            return id;
        }
    }

    /**
     * Function to retrieve a note from the given table
     * @param dbName name of db to retrieve notes from
     * @param noteID id of note to retrieve
     * @return cursor with sql query return
     */
    public Cursor getNoteFromDB(String dbName, int noteID){
        String query = "SELECT * FROM " + dbName + " WHERE " + ID_COLUMN_NAME + " = " + noteID + ";";
        return mDatabase.rawQuery(query, null);
    }

    /**
     * Function to change data of a note in the db
     * @param tableName table to edit
     * @param dbID id of note to edit
     * @param subject new subject
     * @param body new body
     */
    public void editNote(String tableName, int dbID, String subject, String body){
        String query = "UPDATE %s SET %s = \"%s\", %s = \"%s\" WHERE %s = %d";
        query = String.format(Locale.getDefault(), query, tableName, SUBJECT_COLUMN_NAME, subject, NOTE_COLUMN_NAME, body, ID_COLUMN_NAME, dbID);

        mDatabase.execSQL(query);
    }

    /**
     * Delete the given note from the db given id in the note
     * @param dbToDeleteFrom the db name to be deleted from
     * @param dbID of the entry in the db to delete
     */
    public void deleteNoteFromDB(String dbToDeleteFrom, int dbID){
        boolean complete = mDatabase.delete(dbToDeleteFrom, ID_COLUMN_NAME + " = " + dbID, null) > 0;

        Log.d(TAG, String.format("ID deleting = %d, SUCCESS = %b", dbID, complete));
    }

    private String tableQuery = "SELECT * FROM %s";

    /**
     * Get the Cursor of all the notes in the db of give db name
     * @param dbName name of db to retrieve notes from
     * @return Cursor of the data
     */
    public Cursor getNotesFromDB(String dbName){
        return mDatabase.rawQuery(String.format(tableQuery, dbName), null);
    }

    public void checkIfDBValid(){
        if(!mDatabase.isOpen()){
            mDatabase = this.getWritableDatabase();
        }
    }

}
