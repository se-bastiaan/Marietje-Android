package nl.ru.science.mariedroid.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListView;

import nl.ru.science.mariedroid.R;
import nl.ru.science.mariedroid.database.MediaEntry;
import nl.ru.science.mariedroid.database.MediaProvider;
import nl.ru.science.mariedroid.network.objects.Song;

public class FavouriteFragment extends BaseListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter mAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.fragment_favourite_listitem, null, new String[]{
                MediaEntry.COLUMN_NAME_TITLE,
                MediaEntry.COLUMN_NAME_ARTIST,
                MediaEntry.COLUMN_NAME_REQCOUNT}, new int[]{
                R.id.text1, R.id.text2, R.id.text3}, 0);
        setListAdapter(mAdapter);

        getLoaderManager().initLoader(0, null, this);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        final Cursor cursor = (Cursor) l.getItemAtPosition(position);
        Song song = new Song(
                cursor.getString(cursor.getColumnIndex(MediaEntry.COLUMN_NAME_ENTRY_ID)),
                cursor.getString(cursor.getColumnIndex(MediaEntry.COLUMN_NAME_ARTIST)),
                cursor.getString(cursor.getColumnIndex(MediaEntry.COLUMN_NAME_TITLE)),
                cursor.getInt(cursor.getColumnIndex(MediaEntry.COLUMN_NAME_REQCOUNT))
        );
        song.request(this, mApi);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = MediaProvider.CONTENT_URI_FAVOURITES;

        // TODO: where to do sorting?
        return new CursorLoader(getActivity(), baseUri, new String[]{"_id",
                MediaEntry.COLUMN_NAME_ENTRY_ID, MediaEntry.COLUMN_NAME_TITLE,
                MediaEntry.COLUMN_NAME_ARTIST, MediaEntry.COLUMN_NAME_REQCOUNT},
                null, null, MediaEntry.COLUMN_NAME_TITLE + "," +
                MediaEntry.COLUMN_NAME_ARTIST);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

}
