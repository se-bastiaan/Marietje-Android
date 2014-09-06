package nl.ru.science.mariedroid;

import android.app.Application;
import android.os.Handler;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;

import java.util.ArrayList;

import nl.ru.science.mariedroid.network.ApiHelper;
import nl.ru.science.mariedroid.network.objects.Request;
import nl.ru.science.mariedroid.utils.LogUtils;

/**
 * Created by Sebastiaan on 06-09-14.
 */
public class MarieApplication extends Application {

    private FutureCallback<ArrayList<Request>> mQueueCallback;
    private int UPDATE_DELAY = 2000;
    private ApiHelper mApi;
    private ArrayList<Request> mRequests;
    private Handler mUpdateHandler = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mApi = new ApiHelper(this);
        mUpdateHandler = new Handler();
        mRequests = new ArrayList<Request>();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mUpdateHandler.removeCallbacksAndMessages(null);
    }

    public void startQueueUpdating() {
        updateRunnable.run();
    }

    public void stopQueueUpdating() {
        mUpdateHandler.removeCallbacksAndMessages(null);
    }

    public void setQueueUpdatingCallback(FutureCallback<ArrayList<Request>> callback) {
        mQueueCallback = callback;
    }

    public ArrayList<Request> getCurrentRequestData() {
        return mRequests;
    }

    private Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            LogUtils.d("Updating 'now playing'...");

            mApi.getNowPlaying(new FutureCallback<ArrayList<Request>>() {
                @Override
                public void onCompleted(Exception e, ArrayList<Request> result) {
                    if (e == null) {
                        mRequests = result;
                        mApi.getRequests(new FutureCallback<ArrayList<Request>>() {
                            @Override
                            public void onCompleted(Exception e, ArrayList<Request> result) {
                                if (e == null) {
                                    Request request = mRequests.get(0);
                                    mRequests = result;
                                    mRequests.add(0, request);
                                    if(mQueueCallback != null) mQueueCallback.onCompleted(null, mRequests);
                                    UPDATE_DELAY = 2000;
                                } else {
                                    UPDATE_DELAY = 10000;
                                    Toast.makeText(MarieApplication.this, R.string.unknown_error, Toast.LENGTH_SHORT).show();
                                }
                                mUpdateHandler.postDelayed(updateRunnable, UPDATE_DELAY);
                            }
                        });
                    } else {
                        UPDATE_DELAY = 10000;
                        Toast.makeText(MarieApplication.this, R.string.unknown_error, Toast.LENGTH_SHORT).show();
                        mUpdateHandler.postDelayed(updateRunnable, UPDATE_DELAY);
                    }
                }
            });
        }
    };

}
