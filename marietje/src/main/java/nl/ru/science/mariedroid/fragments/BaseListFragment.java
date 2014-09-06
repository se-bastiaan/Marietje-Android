package nl.ru.science.mariedroid.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;

import nl.ru.science.mariedroid.MarieApplication;
import nl.ru.science.mariedroid.R;
import nl.ru.science.mariedroid.activities.BaseActivity;
import nl.ru.science.mariedroid.network.ApiHelper;

/**
 * Created by Sebastiaan on 05-09-14.
 */
public class BaseListFragment extends ListFragment {

    protected ApiHelper mApi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApi = new ApiHelper(getActivity());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setSelector(R.drawable.radboud_list_selector_holo_light);
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    public MarieApplication getApp() {
        return getBaseActivity().getApp();
    }

}
