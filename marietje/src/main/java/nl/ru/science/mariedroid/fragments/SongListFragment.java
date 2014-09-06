package nl.ru.science.mariedroid.fragments;

import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import nl.ru.science.mariedroid.R;
import nl.ru.science.mariedroid.database.MediaEntry;
import nl.ru.science.mariedroid.database.MediaProvider;
import nl.ru.science.mariedroid.network.objects.Song;
import nl.ru.science.mariedroid.widget.AlphabetCursorAdapter;

public class SongListFragment extends BaseListFragment implements OnQueryTextListener, LoaderManager.LoaderCallbacks<Cursor> {

    private String mCurFilter = "";
    private AlphabetCursorAdapter mAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setEmptyText(getActivity().getString(R.string.no_songs));
        setHasOptionsMenu(true);

        mAdapter = new AlphabetCursorAdapter(getActivity(),
                R.layout.fragment_songs_listitem, null, new String[]{
                MediaEntry.COLUMN_NAME_TITLE,
                MediaEntry.COLUMN_NAME_ARTIST}, new int[]{
                R.id.text1, R.id.text2}, 0);
        setListAdapter(mAdapter);

        getListView().setFastScrollEnabled(true);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Place an action bar item for searching.
        MenuItem item = menu.add(R.string.search);
        item.setIcon(R.drawable.ic_action_search);
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);

        SearchView sv = new SearchView(getActivity());
        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) sv.findViewById(R.id.search_src_text);
        LinearLayout searchPlate = (LinearLayout) sv.findViewById(R.id.search_plate);
        searchAutoComplete.setTextColor(Color.WHITE);
        searchPlate.setBackgroundResource(R.drawable.radboud_white_edit_text_holo_light);

        sv.setOnQueryTextListener(this);
        MenuItemCompat.setActionView(item, sv);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
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
        Cursor cursor = (Cursor) l.getItemAtPosition(position);
        Song song = new Song(
                cursor.getString(1),
                cursor.getString(3),
                cursor.getString(2),
                cursor.getInt(4)
        );
        song.request(getActivity(), mApi);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Uri.withAppendedPath(MediaProvider.CONTENT_URI_FILTER,
                Uri.encode(mCurFilter));

        return new CursorLoader(getActivity(), baseUri, new String[]{"_id",
                MediaEntry.COLUMN_NAME_ENTRY_ID, MediaEntry.COLUMN_NAME_TITLE,
                MediaEntry.COLUMN_NAME_ARTIST, MediaEntry.COLUMN_NAME_REQCOUNT}, null, null, MediaEntry.COLUMN_NAME_TITLE + "," +
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
