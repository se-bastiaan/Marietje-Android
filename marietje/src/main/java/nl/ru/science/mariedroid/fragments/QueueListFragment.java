package nl.ru.science.mariedroid.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.koushikdutta.async.future.FutureCallback;

import java.util.ArrayList;

import nl.ru.science.mariedroid.R;
import nl.ru.science.mariedroid.network.objects.Request;
import nl.ru.science.mariedroid.utils.LogUtils;
import nl.ru.science.mariedroid.widget.QueueListAdapter;

public class QueueListFragment extends BaseListFragment {

    private ArrayList<Request> mRequests;
    private QueueListAdapter mListAdapter;
    private Handler mHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setSelector(android.R.color.transparent);
    }

    public void refreshListAdapter() {
        try {
            int index = getListView().getFirstVisiblePosition();
            View v = getListView().getChildAt(0);
            int top = (v == null) ? 0 : v.getTop();

            if (mListAdapter == null) {
                mListAdapter = new QueueListAdapter(getActivity(), mRequests);
                setListAdapter(mListAdapter);
            } else {
                mListAdapter.setData(mRequests);
            }
            mListAdapter.notifyDataSetChanged();
            getListView().setSelectionFromTop(index, top);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            if(!mHandler.hasMessages(0)) mHandler.post(updateRunnable);
        } else {
            LogUtils.d("Queue updates disabled");
            mHandler.removeCallbacksAndMessages(null);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(!mHandler.hasMessages(0)) mHandler.post(updateRunnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d("QueueListFragment", "onPause");
        mHandler.removeCallbacksAndMessages(null);
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
                                    refreshListAdapter();
                                } else {
                                    e.printStackTrace();
                                }
                                mHandler.post(updateRunnable);
                            }
                        });
                    } else {
                        e.printStackTrace();
                        mHandler.post(updateRunnable);
                    }
                }
            });
        }
    };
}
