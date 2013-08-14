package com.example.marietje;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

// TODO: give SongListFragment extra parameters. Don't copy code.
public class FavouriteFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private SimpleCursorAdapter mAdapter;
    private final String REQUEST_URL = "http://marietje.marie-curie.nl:8080/request";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.fav_layout, null, new String[]{
                MediaContract.MediaEntry.COLUMN_NAME_TITLE,
                MediaContract.MediaEntry.COLUMN_NAME_ARTIST,
                MediaContract.MediaEntry.COLUMN_NAME_REQCOUNT}, new int[]{
                R.id.text1, R.id.text2, R.id.text3}, 0);
        setListAdapter(mAdapter);

        getLoaderManager().initLoader(0, null, this);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        final Cursor c = (Cursor) l.getItemAtPosition(position);

        // TODO: move to listener?
        new AsyncTask<Void, Void, Integer>() {

            @Override
            protected Integer doInBackground(Void... params) {
                try {
                    SharedPreferences settings = getActivity()
                            .getSharedPreferences("prefs", 0);

                    InputStream res = Utils
                            .downloadUrl(REQUEST_URL
                                    + "/"
                                    + settings.getString("username", "user")
                                    + "/"
                                    + settings.getString("password", "pass")
                                    + "/" + c.getString(1));

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(res, "UTF-8"), 8);

                    reader.readLine(); // skip xml header
                    String result = reader.readLine();

                    if (!result.equals("<status code=\"ok\"/>")) {
                        if (result.equals("<status code=\"wrong-login\"/>")) {
                            return 2;
                        }

                        return 1; // yield generic error
                    }

                    /* Increase the request count */
                    MediaDbHelper mediaDbHelper = new MediaDbHelper(getActivity());
                    SQLiteDatabase db = mediaDbHelper.getWritableDatabase();

                    db.execSQL("UPDATE " + MediaContract.MediaEntry.TABLE_NAME + " SET " + MediaContract.MediaEntry.COLUMN_NAME_REQCOUNT + " = "
                            + MediaContract.MediaEntry.COLUMN_NAME_REQCOUNT + " + 1 WHERE " + MediaContract.MediaEntry.COLUMN_NAME_ENTRY_ID + " = " +
                            c.getString(1)); // FIXME: move, plus: is this safe?
                    db.close();

                } catch (IOException e) {
                    return 1;
                }

                return 0;
            }

            @Override
            protected void onPostExecute(Integer result) {
                if (result == 0) {
                    Toast.makeText(getActivity(),
                            c.getString(2) + " aangevraagd.",
                            Toast.LENGTH_SHORT).show();

                    getActivity().getContentResolver().insert(MediaProvider.CONTENT_URI_FAVOURITES, null); // FIXME: fake insert to update list
                }
                if (result == 1) {
                    Toast.makeText(getActivity(),
                            "Kon liedje niet aangevragen.", Toast.LENGTH_SHORT)
                            .show();
                }
                if (result == 2) {
                    Toast.makeText(
                            getActivity(),
                            "Verkeerde inloggegevens. Log uit en probeer het opnieuw.",
                            Toast.LENGTH_SHORT).show();
                }
            }

        }.execute();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = MediaProvider.CONTENT_URI_FAVOURITES;

        // TODO: where to do sorting?
        return new CursorLoader(getActivity(), baseUri, new String[]{"_id",
                MediaContract.MediaEntry.COLUMN_NAME_ENTRY_ID, MediaContract.MediaEntry.COLUMN_NAME_TITLE,
                MediaContract.MediaEntry.COLUMN_NAME_ARTIST, MediaContract.MediaEntry.COLUMN_NAME_REQCOUNT},
                null, null, MediaContract.MediaEntry.COLUMN_NAME_TITLE + "," +
                MediaContract.MediaEntry.COLUMN_NAME_ARTIST);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

}
