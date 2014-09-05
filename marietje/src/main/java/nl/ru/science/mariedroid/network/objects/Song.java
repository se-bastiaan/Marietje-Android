package nl.ru.science.mariedroid.network.objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import nl.ru.science.mariedroid.R;
import nl.ru.science.mariedroid.activities.MainActivity;
import nl.ru.science.mariedroid.database.MediaDbHelper;
import nl.ru.science.mariedroid.database.MediaEntry;
import nl.ru.science.mariedroid.database.MediaProvider;
import nl.ru.science.mariedroid.fragments.BaseListFragment;
import nl.ru.science.mariedroid.network.ApiHelper;
import nl.ru.science.mariedroid.utils.LogUtils;
import nl.ru.science.mariedroid.utils.PrefUtils;

/**
 * Created by Sebastiaan on 05-09-14.
 */
public class Song {

    public String id;
    public String artist;
    public String title;
    public Integer requestCount;
    public Float length;

    public Song(String id, String artist, String title, Integer requestCount) {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.requestCount= requestCount;
    }
    
    public void request(final BaseListFragment fragment, ApiHelper api) {
        final Context context = fragment.getActivity();
        if(!PrefUtils.getUsername(context, "").isEmpty() && !PrefUtils.getPassword(context, "").isEmpty()) {
            api.requestSong(this, new FutureCallback<ApiHelper.SongRequestResult>() {
                @Override
                public void onCompleted(Exception e, ApiHelper.SongRequestResult result) {
                    if(result == ApiHelper.SongRequestResult.ERROR) {
                        Toast.makeText(context, R.string.song_request_error, Toast.LENGTH_SHORT).show();
                    } else if(result == ApiHelper.SongRequestResult.SUCCESS) {
                        Toast.makeText(context, title + " " + context.getString(R.string.has_been_requested), Toast.LENGTH_SHORT).show();
                        // FIXME: fake insert to update list
                        context.getContentResolver().insert(MediaProvider.CONTENT_URI_FILTER, null);
                        context.getContentResolver().insert(MediaProvider.CONTENT_URI_FAVOURITES, null);
                    } else if(result == ApiHelper.SongRequestResult.WRONG_LOGIN) {
                        Toast.makeText(context, R.string.wrong_login, Toast.LENGTH_SHORT).show();
                    } else if(result == ApiHelper.SongRequestResult.IN_QUEUE) {
                        Toast.makeText(context, R.string.in_queue, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(context, R.string.wrong_login, Toast.LENGTH_SHORT).show();
        }
    }

    public static void parseJSON(final Context context, String json, final FutureCallback<Boolean> callback) {
        new AsyncTask<String, Void, Boolean>() {

            private Exception mException = null;

            @Override
            protected Boolean doInBackground(String... result) {
                try {
                    JSONObject jObject = new JSONObject(result[0]);

                    MediaDbHelper mediaDbHelper = new MediaDbHelper(context);
                    SQLiteDatabase db = mediaDbHelper.getWritableDatabase();

                    LogUtils.d("Importing songs");

                    publishProgress();

                    JSONArray names = jObject.names();

                    try {
                        db.beginTransaction(); // speeds up insertion

                        // save the request counts
                        Cursor c = db.query(MediaEntry.TABLE_NAME, new String[]{MediaEntry.COLUMN_NAME_ENTRY_ID, MediaEntry.COLUMN_NAME_REQCOUNT}, MediaEntry.COLUMN_NAME_REQCOUNT + " > 0", null, null, null, null);
                        List<String[]> reqList = new ArrayList<String[]>();

                        while (c.moveToNext()) {
                            reqList.add(new String[]{c.getString(0), c.getString(1)});
                        }
                        c.close();

                        db.delete(MediaEntry.TABLE_NAME, null, null);

                        for (int i = 0; i < names.length(); i++) {
                            JSONArray vals = jObject.getJSONArray(names.getString(i));

                            String title = vals.getString(1).trim();
                            String artist = vals.getString(0).trim();

                            if (title.equals("") || artist.equals("")) {
                                continue;
                            }

                            ContentValues values = new ContentValues();
                            values.put(MediaEntry.COLUMN_NAME_ENTRY_ID, names.getString(i).substring(1)); // remove the "_"
                            values.put(MediaEntry.COLUMN_NAME_TITLE, title);
                            values.put(MediaEntry.COLUMN_NAME_ARTIST, artist);
                            values.put(MediaEntry.COLUMN_NAME_REQCOUNT, 0); // TODO: keep this value if it has any

                            db.insert(MediaEntry.TABLE_NAME, null, values);
                        }

                        // restore the request counts
                        for (String[] v : reqList) {
                            ContentValues values = new ContentValues();
                            values.put(MediaEntry.COLUMN_NAME_REQCOUNT, v[1]);

                            db.update(MediaEntry.TABLE_NAME, values, MediaEntry.COLUMN_NAME_ENTRY_ID + " = " + v[0], null);
                        }

                        db.setTransactionSuccessful();
                    } catch (SQLException e) {
                        LogUtils.d("SQL import error", e);
                        mException = e;
                        return false;
                    } finally {
                        db.endTransaction();
                    }

                    db.close();
                    LogUtils.d("Importing done");
                } catch (JSONException e) {
                    mException = e;
                    LogUtils.d("Fout bij JSON parsing", e);
                    return false;
                }

                return true;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);
                callback.onCompleted(mException, success);
            }
        }.execute(json);
    }

}
