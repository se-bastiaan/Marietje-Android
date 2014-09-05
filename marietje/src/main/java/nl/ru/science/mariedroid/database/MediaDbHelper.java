package nl.ru.science.mariedroid.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import nl.ru.science.mariedroid.Constants;

public class MediaDbHelper extends SQLiteOpenHelper {

    private static final String INT_TYPE = " int";
    private static final String FLOAT_TYPE = " float";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MediaEntry.TABLE_NAME + " (" +
                    MediaEntry._ID + " INTEGER PRIMARY KEY," +
                    MediaEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    MediaEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    MediaEntry.COLUMN_NAME_ARTIST + TEXT_TYPE + COMMA_SEP +
                    MediaEntry.COLUMN_NAME_REQCOUNT + INT_TYPE + COMMA_SEP +
                    MediaEntry.COLUMN_NAME_LENGTH + FLOAT_TYPE +
                    " )";

    public MediaDbHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Nothing, yet.
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean isEmpty() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(MediaEntry.TABLE_NAME, null, null, null, null, null, null, "1");
        Boolean empty = cursor.getCount() == 0;
        cursor.close();
        db.close();
        return empty;
    }

    public void incrementRequestCount(String songId) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("UPDATE " + MediaEntry.TABLE_NAME + " SET " + MediaEntry.COLUMN_NAME_REQCOUNT + " = " + MediaEntry.COLUMN_NAME_REQCOUNT + " + 1 WHERE " + MediaEntry.COLUMN_NAME_ENTRY_ID + " = " + songId);
        db.close();
    }
}