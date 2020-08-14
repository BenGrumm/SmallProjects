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
     * Add a note to the database of completed notes
     * @param subject string of subject of note
     * @param note string of body of the note
     * @return id of new note (-1 if error)
     */
    public int addNoteToBeCompleted(String subject, String note){
        ContentValues cv = new ContentValues();
        cv.put(SUBJECT_COLUMN_NAME, subject);
        cv.put(NOTE_COLUMN_NAME, note);

        long result = mDatabase.insert(TO_COMPLETE_TABLE_NAME, null, cv);
        Log.d(TAG, Long.toString(result));

        return (int)result;
    }

    /**
     * Insert a note in a given position in the T.O.D.O db
     * @param note the note to be inserted
     * @return new db id
     */
    public int insertNoteIntoTODO(Note note){
        // Add note to end of the list
        int newID = addNoteToBeCompleted(note.getSubject(), note.getNoteBody());

        // Configure surrounding pointers to position item correctly
        editPointersInsert(TO_COMPLETE_TABLE_NAME, newID, note.getNextNoteID());

        return newID;
    }

    public int insertNoteIntoCompleted(CompletedNote note, int nextID){
        // Next note will be -1 as its being added to end of list
        int noteID = addCompletedNote(note);

        // Edit pointer as this will be the last
        editPointersInsert(COMPLETED_TABLE_NAME, noteID, nextID);

        return noteID;
    }

    /**
     * Add a note to the database of completed notes
     * @param note note to be written to db
     * @return if completed successfully return true else false
     */
    public int addCompletedNote(CompletedNote note){
        ContentValues cv = new ContentValues();
        cv.put(SUBJECT_COLUMN_NAME, note.getSubject());
        cv.put(NOTE_COLUMN_NAME, note.getNoteBody());
        cv.put(DATE_COLUMN_NAME, note.convertDateToString(note.getDateNoteCompleted()));

        long result = mDatabase.insert(COMPLETED_TABLE_NAME, null, cv);

        if(result == -1){
            return -1;
        }else{
            Cursor rowID = mDatabase.rawQuery(String.format(Locale.ENGLISH, "SELECT %s FROM %s WHERE rowid = %d", ID_COLUMN_NAME, COMPLETED_TABLE_NAME, result), null);
            rowID.moveToFirst();
            int id = rowID.getInt(0);
            rowID.close();
            return id;
        }
    }

    /**
     * Function to move a given note from the T.O.D.O db to the completed db
     * @param note to be removed from one and added to another
     */
    public void moveNoteToCompleted(Note note){

        deleteNoteFromDB(TO_COMPLETE_TABLE_NAME, note.getDatabaseID());

        editPointersDelete(TO_COMPLETE_TABLE_NAME, note.getDatabaseID(), note.getNextNoteID());

        CompletedNote cNote = new CompletedNote(note, Calendar.getInstance().getTime(), -1);

        Log.d(TAG, Integer.toString(insertNoteIntoCompleted(cNote, -1)));

    }

//    /**
//     * Function to change all ids > than id of given noteID by a given value
//     * @param noteID id of the where all notes with > id's changed
//     * @param changeByX the value to change the id's by
//     */
//    public void changeDbIds(String tableName, int noteID, int changeByX){
//        String query = "SELECT * FROM " + tableName + " WHERE " + ID_COLUMN_NAME  + " > " + noteID;
//
//        Cursor mCursor = mDatabase.rawQuery(query, null);
//
//        if(mCursor.moveToFirst()){
//            int columnID = mCursor.getColumnIndex(ID_COLUMN_NAME);
//            int posID;
//            while(!mCursor.isAfterLast()){
//                posID = mCursor.getInt(columnID);
//                query = "UPDATE " + tableName + " SET " + ID_COLUMN_NAME + " = " + (posID + changeByX) + " WHERE " + ID_COLUMN_NAME  + " = " + posID;
//                mDatabase.execSQL(query);
//                mCursor.moveToNext();
//            }
//        }
//        mCursor.close();
//    }

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

    public Cursor getNoteFromDB(String dbName, int noteID){
        String query = "SELECT * FROM " + dbName + " WHERE " + ID_COLUMN_NAME + " = " + noteID + ";";
        return mDatabase.rawQuery(query, null);
    }

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
