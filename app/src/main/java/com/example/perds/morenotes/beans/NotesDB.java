package com.example.perds.morenotes.beans;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Perds on 11/21/2016.
 */

public class NotesDB {

    // instance members for accessing the database
    private SQLiteDatabase database;
    private SQLiteOpenHelper openHelper;

    // constants for the database
    public static final String DB_NAME = "notes.db";
    public static final int DB_VERSION = 1;

    // constants for the table
    public static final String NOTES_TABLE = "Notes";

    public static final String ID = "id";
    public static final int ID_COLUMN = 0;

    public static final String TITLE = "title";
    public static final int TITLE_COLUMN = 1;

    public static final String CATEGORY = "category";
    public static final int CATEGORY_COLUMN = 2;

    public static final String TEXT = "text";
    public static final int TEXT_COLUMN = 3;

    public static final String LOCATION = "location";
    public static final int LOCATION_COLUMN = 4;

    public static final String DATE_CREATED = "dateCreated";
    public static final int DATE_CREATED_COLUMN = 5;

    public static final String PICTURE = "picture";
    public static final int PICTURE_COLUMN = 6;

    public static final String AUDIO = "audio";
    public static final int AUDIO_COLUMN = 7;

    // DDL for creating the table. Careful with adding spaces!!
    public static final String CREATE_NOTES_TABLE =
            "CREATE TABLE " + NOTES_TABLE + " (" +
                    ID + " INTEGER PRIMARY KEY, " +
                    TITLE + " TEXT, " +
                    CATEGORY + " TEXT, " +
                    TEXT + " TEXT, " +
                    LOCATION + " TEXT, " +
                    DATE_CREATED + " TEXT, " +
                    PICTURE + " TEXT, " +
                    AUDIO + " TEXT)";

    public NotesDB(Context context) {
        // initialize our OpenHelper class
        openHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    public static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_NOTES_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            // Drop the table
            db.execSQL("DROP TABLE IF EXISTS " + NOTES_TABLE);

            // Call onCreate to create the table
            onCreate(db);
        }
    }

    public void saveNote(Note note) {
        database = openHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(ID, note.getId());
        cv.put(TITLE, note.getTitle());
        cv.put(CATEGORY, note.getCategory());
        cv.put(TEXT, note.getText());
        cv.put(LOCATION, note.getLocation());
        cv.put(DATE_CREATED, note.getDateCreated());
        cv.put(PICTURE, note.getPicture());
        cv.put(AUDIO, note.getAudio());

        database.insert(NOTES_TABLE, null, cv);

        // Close the database
        database.close();
    }

    public ArrayList<Note> updateNoteById(Note note) {

        database = openHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(ID, note.getId());
        cv.put(TITLE, note.getTitle());
        cv.put(CATEGORY, note.getCategory());
        cv.put(TEXT, note.getText());
        cv.put(LOCATION, note.getLocation());
        cv.put(DATE_CREATED, note.getDateCreated());
        cv.put(PICTURE, note.getPicture());
        cv.put(AUDIO, note.getAudio());

        database.update(NOTES_TABLE, cv, "id = ?", new String[]{String.valueOf(note.getId())});

        database.close();

        return getAllNotes();

    }

    public ArrayList<Note> getAllNotes() {

        database = openHelper.getReadableDatabase();

        Cursor cursor = database.query(NOTES_TABLE, null, null, null, null, null, ID);

        ArrayList<Note> notes = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(ID_COLUMN);
            String title = cursor.getString(TITLE_COLUMN);
            String category = cursor.getString(CATEGORY_COLUMN);
            String text = cursor.getString(TEXT_COLUMN);
            String location = cursor.getString(LOCATION_COLUMN);
            String dateCreated = cursor.getString(DATE_CREATED_COLUMN);
            String picture = cursor.getString(PICTURE_COLUMN);
            String audio = cursor.getString(AUDIO_COLUMN);

            notes.add(new Note(id, title, category, text, location, dateCreated, picture, audio));
        }

        database.close();

        return notes;
    }

    public int deleteAllNotes() {

        database = openHelper.getWritableDatabase();

        int amountDeleted = database.delete(NOTES_TABLE, "1", null);

        database.close();

        return amountDeleted;
    }

    public int deleteNotesById(int id) {

        database = openHelper.getWritableDatabase();

        int amountDeleted = database.delete(NOTES_TABLE, "id = ?", new String[]{String.valueOf(id)});

        database.close();

        return amountDeleted;
    }
}
