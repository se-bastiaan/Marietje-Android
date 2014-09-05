package nl.ru.science.mariedroid.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;

import java.util.ArrayList;

import nl.ru.science.mariedroid.R;
import nl.ru.science.mariedroid.network.objects.Request;
import nl.ru.science.mariedroid.utils.LogUtils;
import nl.ru.science.mariedroid.widget.QueueListAdapter;

public class QueueListFragment extends BaseListFragment {

    private ArrayList<Request> mRequests;
    private final int UPDATE_DELAY = 2000;
    private Handler updateHandler = null;

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
                                    refreshListAdapter();
                                    updateHandler.postDelayed(updateRunnable, UPDATE_DELAY);
                                } else {
                                    Toast.makeText(getActivity(), R.string.unknown_error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), R.string.unknown_error, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateHandler = new Handler();
        mRequests = new ArrayList<Request>();
    }

    public void startUpdate() {
        updateHandler.postDelayed(updateRunnable, UPDATE_DELAY);
    }

    public void refreshListAdapter() {
        int index = getListView().getFirstVisiblePosition();
        View v = getListView().getChildAt(0);
        int top = (v == null) ? 0 : v.getTop();

        getListView().setSelectionFromTop(index, top);
        QueueListAdapter adapter = new QueueListAdapter(getActivity(), mRequests);
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setEmptyText(getActivity().getString(R.string.loading_queue));
        setHasOptionsMenu(true);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            LogUtils.d("Visible");
            startUpdate();
        } else {
            LogUtils.d("Queue updates disabled");
            if(updateHandler != null) {
                updateHandler.removeCallbacksAndMessages(null);
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        if (updateHandler != null) {
            updateHandler.removeCallbacksAndMessages(null);
        }
    }
}
