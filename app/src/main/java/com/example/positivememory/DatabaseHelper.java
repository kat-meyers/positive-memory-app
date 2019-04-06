package com.example.positivememory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    static final String TABLE_NAME = "memories_table";
    static final String _ID = "_id";
    static final String COLUMN_NAME_TITLE = "date";
    static final String COLUMN_NAME_SUBTITLE = "entry";
    SQLiteDatabase db;
    Cursor cursor;

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME_TITLE + " TEXT, " +
            COLUMN_NAME_SUBTITLE + " TEXT )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DatabaseHelper(Context context){
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);

    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /*
        adds data to the table
     */
    public boolean addData(String date, String entry) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME_TITLE, date);
        cv.put(COLUMN_NAME_SUBTITLE, entry);

        long result = db.insert(TABLE_NAME, null, cv);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /*
        deletes an entry from the table
     */
    public void deleteEntry(String str){
        db = this.getWritableDatabase();
        String[] entry = {str};
        db.delete(TABLE_NAME, COLUMN_NAME_TITLE + " = '" + str + "'", null);

    }

    /*
        fetches the entry from that date
     */
    public Cursor fetchEntryByDate(String str){
        db = this.getReadableDatabase();

        cursor = null;

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME_TITLE + " = '" + str + "'";

        cursor =  db.rawQuery(query, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        return cursor;
    }

    /*
        fetches entries that contain a keyword
    */
    public Cursor fetchEntriesByWord(String str){
        db = this.getReadableDatabase();

        cursor = null;

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME_SUBTITLE + " LIKE '%" + str + "%' ORDER BY " + COLUMN_NAME_TITLE;

        cursor =  db.rawQuery(query, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        return cursor;
    }

    /*
        fetches all entries
     */
    public Cursor fetchAllEntries(){
        db = this.getReadableDatabase();

        cursor = null;

        cursor = db.query(TABLE_NAME, new String[]{_ID, COLUMN_NAME_TITLE, COLUMN_NAME_SUBTITLE}, null, null, null, null, COLUMN_NAME_TITLE);

        if(cursor != null){
            cursor.moveToFirst();
        }

        return cursor;
    }

    /*
        fetches a random entry
     */
    public Cursor fetchRandomEntry(){
        db = this.getReadableDatabase();

        cursor = null;

        cursor = db.query(TABLE_NAME, new String[]{_ID, COLUMN_NAME_TITLE, COLUMN_NAME_SUBTITLE}, null, null, null, null, "RANDOM()", "1" );

        if(cursor != null){
            cursor.moveToFirst();
        }

        return cursor;
    }

    /*
        Updates the entry for the corresponding date
     */
    public Cursor updateEntry(String date, String str){
        db = this.getReadableDatabase();

        cursor = null;

        String query = "UPDATE " + TABLE_NAME + " SET " + COLUMN_NAME_SUBTITLE + " = '" + str + "' WHERE " + COLUMN_NAME_TITLE + " = '" + date + "'";

        cursor =  db.rawQuery(query, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        return cursor;
    }

}
