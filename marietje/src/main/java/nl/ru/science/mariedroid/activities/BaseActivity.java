package nl.ru.science.mariedroid.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import butterknife.ButterKnife;
import nl.ru.science.mariedroid.R;
import nl.ru.science.mariedroid.network.ApiHelper;

/**
 * Created by Sebastiaan on 05-09-14.
 */
public class BaseActivity extends ActionBarActivity {

    protected ApiHelper mApi;
    protected ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void onCreate(Bundle savedInstanceState, Integer layout) {
        super.onCreate(savedInstanceState);
        setContentView(layout);
        ButterKnife.inject(this);
        mApi = new ApiHelper(this);
        mActionBar = getSupportActionBar();
        mActionBar.setLogo(R.drawable.ic_logo);
    }

}
