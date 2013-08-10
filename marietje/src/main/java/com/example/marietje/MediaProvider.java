package com.example.marietje;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.example.marietje.MediaContract.MediaEntry;

public class MediaProvider extends ContentProvider {
    private MediaDbHelper mDB;

    private static final int ALL_MEDIA = 1;
    private static final int SEARCH_SONG = 2;

    private static final String AUTHORITY = "com.example.marietje.songprovider";

    // create content URIs from the authority by appending path to database
    // table
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/media");

    public static final Uri CONTENT_URI_FILTER = Uri.parse("content://" + AUTHORITY
            + "/search");

    // a content URI pattern matches content URIs using wildcard characters:
    // *: Matches a string of any valid characters of any length.
    // #: Matches a string of numeric characters of any length.
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "media", ALL_MEDIA);
        uriMatcher.addURI(AUTHORITY, "search/*", SEARCH_SONG);
    }

    @Override
    public int delete(Uri arg0, String arg1, String[] arg2) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getType(Uri arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri arg0, ContentValues arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean onCreate() {
        mDB = new MediaDbHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MediaEntry.TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case SEARCH_SONG:
                queryBuilder.appendWhere(MediaEntry.COLUMN_NAME_TITLE + " LIKE "); // TODO: decode?
                queryBuilder.appendWhereEscapeString('%' + uri.getLastPathSegment() + '%'); // FIXME: is this safe?
                queryBuilder.appendWhere(" OR " + MediaEntry.COLUMN_NAME_ARTIST + " LIKE ");
                queryBuilder.appendWhereEscapeString('%' + uri.getLastPathSegment() + '%');

                break;
            default:
                queryBuilder.appendWhere("1");
                break;
        }

        queryBuilder.appendWhere(" AND " + MediaEntry.COLUMN_NAME_TITLE + " <> ''"); // disallow empty names

        Cursor cursor = queryBuilder.query(mDB.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
        // TODO Auto-generated method stub
        return 0;
    }

}
