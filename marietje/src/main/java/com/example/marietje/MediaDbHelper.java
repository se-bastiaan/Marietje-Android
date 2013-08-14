package com.example.marietje;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.marietje.MediaContract.MediaEntry;

public class MediaDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "Media.db";

    private static final String INT_TYPE = " int";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MediaEntry.TABLE_NAME + " (" +
                    MediaEntry._ID + " INTEGER PRIMARY KEY," +
                    MediaEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP + // TODO: change type to int?
                    MediaEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    MediaEntry.COLUMN_NAME_ARTIST + TEXT_TYPE + COMMA_SEP +
                    MediaEntry.COLUMN_NAME_REQCOUNT + INT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MediaEntry.TABLE_NAME;

    public MediaDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 4 && newVersion == 5) {
            // add request count
            db.execSQL("ALTER TABLE " + MediaEntry.TABLE_NAME + " ADD " +
                    MediaEntry.COLUMN_NAME_REQCOUNT + INT_TYPE + " DEFAULT 0");
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean isEmpty() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(MediaEntry.TABLE_NAME, null, null, null, null, null, null, "1");
        return cursor.getCount() == 0; // FIXME: close db and cursor?
    }
}