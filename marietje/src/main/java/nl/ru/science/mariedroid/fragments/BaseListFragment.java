package nl.ru.science.mariedroid.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

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

}
