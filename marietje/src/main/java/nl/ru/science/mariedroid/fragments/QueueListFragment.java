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
            mRequests = getApp().getCurrentRequestData();
            refreshListAdapter();
            getApp().setQueueUpdatingCallback(new FutureCallback<ArrayList<Request>>() {
                @Override
                public void onCompleted(Exception e, ArrayList<Request> result) {
                    mRequests = result;
                    refreshListAdapter();
                }
            });
        } else {
            LogUtils.d("Queue updates disabled");
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        getApp().setQueueUpdatingCallback(null);
    }
}
