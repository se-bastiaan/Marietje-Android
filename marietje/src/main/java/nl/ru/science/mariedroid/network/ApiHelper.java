package nl.ru.science.mariedroid.network;

import android.content.Context;

import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import nl.ru.science.mariedroid.Constants;
import nl.ru.science.mariedroid.database.MediaDbHelper;
import nl.ru.science.mariedroid.network.objects.Request;
import nl.ru.science.mariedroid.network.objects.Song;
import nl.ru.science.mariedroid.utils.PrefUtils;

public class ApiHelper {

    public enum SongRequestResult { SUCCESS, ERROR, WRONG_LOGIN, IN_QUEUE };
    private Context mContext;

    public ApiHelper(Context context) {
        mContext = context;
    }

    /**
     * Get server url by saved index
     * @return Base URL
     */
    private String getInstanceBaseUrl() {
        return getInstanceBaseUrl(PrefUtils.getInstanceIndex(mContext));
    }

    /**
     * Get server url for index
     * @param index Index of server in the Constants file
     * @return Base URL
     */
    private String getInstanceBaseUrl(Integer index) {
        return Constants.INSTANCE_URLS[index];
    }

    /**
     * Authenticate user on server
     * FIXME: When the server is going to support this, fix this request to do what it says. Currently, this is a dummy request.
     * @param instanceIndex Index of server in the Constants file
     * @param username Username on server
     * @param password Password on server
     * @param callback FutureCallback<Boolean> to verify the request succeeded
     * @return Future<String> Give the ability to cancel or edit the request
     */
    public Future<String> authenticateUser(final Integer instanceIndex, final String username, final String password, final FutureCallback<Boolean> callback) {
        return Ion.with(mContext).load(getInstanceBaseUrl(instanceIndex)).asString()
            .setCallback(new FutureCallback<String>() {
                @Override
                public void onCompleted(Exception e, String result) {
                    PrefUtils.saveUserLogin(mContext, username, password);
                    PrefUtils.saveInstanceIndex(mContext, instanceIndex);
                    callback.onCompleted(null, true);
                }
            });
    }

    /**
     * Gets the database with songs from the server, parse the JSON and save all records in the database.
     * @param callback FutureCallback<Boolean> Handles result, success or not
     */
    public void getMedia(final FutureCallback<Boolean> callback) {
        Ion.with(mContext).load(getInstanceBaseUrl() + "media").asString()
            .setCallback(new FutureCallback<String>() {
                @Override
                public void onCompleted(Exception e, String result) {
                    Song.parseJSON(mContext, result, callback);
                }
            });
    }

    /**
     * Gets the current playing song from the server
     * @param callback FutureCallback<ArrayList<Request>> Handles result
     */
    public void getNowPlaying(final FutureCallback<ArrayList<Request>> callback) {
        getRequestsXml(getInstanceBaseUrl() + "playing", callback);
    }

    /**
     * Gets all requests from the server
     * @param callback FutureCallback<ArrayList<Request>> Handles result
     */
    public void getRequests(final FutureCallback<ArrayList<Request>> callback) {
        getRequestsXml(getInstanceBaseUrl() + "requests", callback);
    }

    /**
     * Gets the specified data and parses the XML to ArrayList<Request>
     * @param callback FutureCallback<ArrayList<Request>> Handles result
     */
    private void getRequestsXml(String url, final FutureCallback<ArrayList<Request>> callback) {
        Ion.with(mContext).load(url)
            .asString()
            .setCallback(new FutureCallback<String>() {
                @Override
                public void onCompleted(Exception e, String result) {
                    if (e == null) {
                        Request.parseXMLAsync(result, callback);
                    }
                }
            });
    }

    /**
     * Request song on server
     * @param song Song Object containing all information needed to do request
     * @param callback FutureCallback<SongRequestResult> Handles result of operation
     */
    public void requestSong(final Song song, final FutureCallback<SongRequestResult> callback) {
        String username = PrefUtils.getUsername(mContext, "");
        String password = PrefUtils.getPassword(mContext, "");

        Ion.with(mContext).load(getInstanceBaseUrl() + "request/" + username + "/" + password + "/" + song.id)
            .asString()
            .setCallback(new FutureCallback<String>() {
                @Override
                public void onCompleted(Exception e, String result) {
                    SongRequestResult resultCode = SongRequestResult.ERROR;
                    if(e == null && result.contains("ok")) {
                        resultCode = SongRequestResult.SUCCESS;
                    } else if(e == null && result.contains("wrong-login")) {
                        resultCode = SongRequestResult.WRONG_LOGIN;
                    } else if(e == null && result.contains("AlreadyInQueueError()")) {
                        resultCode = SongRequestResult.IN_QUEUE;
                    }

                    if(resultCode == SongRequestResult.SUCCESS) {
                        MediaDbHelper mediaDbHelper = new MediaDbHelper(mContext);
                        mediaDbHelper.incrementRequestCount(song.id);
                    }

                    callback.onCompleted(e, resultCode);
                }
            });
    }

}
