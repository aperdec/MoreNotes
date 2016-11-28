package com.example.perds.morenotes.beans;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.util.Date;

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

    public static final String ID = "_id";
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
                    PICTURE + " BLOB, " +
                    AUDIO + " BLOB)";

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
}
