package com.example.marietje;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.marietje.MediaContract.MediaEntry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SongListFragment extends ListFragment implements
        OnQueryTextListener, LoaderManager.LoaderCallbacks<Cursor> {
    private final String REQUEST_URL = "http://marietje.marie-curie.nl:8080/request";

    private String mCurFilter = "";
    private SimpleCursorAdapter mAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setEmptyText("Geen liedjes gevonden.");
        setHasOptionsMenu(true);

        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_2, null, new String[]{
                MediaEntry.COLUMN_NAME_TITLE,
                MediaEntry.COLUMN_NAME_ARTIST}, new int[]{
                android.R.id.text1, android.R.id.text2}, 0);
        setListAdapter(mAdapter);

        // Prepare the loader. Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Place an action bar item for searching.
        MenuItem item = menu.add("Zoeken");
        item.setIcon(android.R.drawable.ic_menu_search);
        MenuItemCompat.setShowAsAction(item,
                MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        SearchView sv = new SearchView(getActivity());
        sv.setOnQueryTextListener(this);
        MenuItemCompat.setActionView(item, sv);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //mCurFilter = !TextUtils.isEmpty(newText) ? newText : null;
        mCurFilter = newText;
        getLoaderManager().restartLoader(0, null, this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // Don't care about this.
        return true;
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
                    Log.i("", result);

                    if (!result.equals("<status code=\"ok\"/>")) {
                        if (result.equals("<status code=\"wrong-login\"/>")) {
                            return 2;
                        }

                        return 1; // yield generic error
                    }

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

            ;

        }.execute();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Uri.withAppendedPath(MediaProvider.CONTENT_URI_FILTER,
                Uri.encode(mCurFilter));

        return new CursorLoader(getActivity(), baseUri, new String[]{"_id",
                MediaEntry.COLUMN_NAME_ENTRY_ID, MediaEntry.COLUMN_NAME_TITLE,
                MediaEntry.COLUMN_NAME_ARTIST}, null, null, MediaEntry.COLUMN_NAME_TITLE + "," +
                MediaEntry.COLUMN_NAME_ARTIST);
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
