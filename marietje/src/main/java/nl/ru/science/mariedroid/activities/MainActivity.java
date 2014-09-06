package nl.ru.science.mariedroid.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import nl.ru.science.mariedroid.R;
import nl.ru.science.mariedroid.database.MediaDbHelper;
import nl.ru.science.mariedroid.database.MediaProvider;
import nl.ru.science.mariedroid.fragments.FavouriteFragment;
import nl.ru.science.mariedroid.fragments.QueueListFragment;
import nl.ru.science.mariedroid.fragments.SongListFragment;
import nl.ru.science.mariedroid.utils.AppUtils;
import nl.ru.science.mariedroid.utils.LogUtils;
import nl.ru.science.mariedroid.utils.PrefUtils;

public class MainActivity extends BaseActivity {

    private ProgressDialog mDialog;

    @InjectView(R.id.pager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main);

        Integer versionCode = AppUtils.getAppVersion(this);
        Integer savedVersion = PrefUtils.getVersionCode(this);
        if(!savedVersion.equals(versionCode)) {
            if(savedVersion == -1) {
                PrefUtils.reset(this);
            }

            PrefUtils.saveVersionCode(this, AppUtils.getAppVersion(this));
        }

        if (PrefUtils.getUsername(this, "").isEmpty() || PrefUtils.getPassword(this, "").isEmpty()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(Fragment.instantiate(this, FavouriteFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, SongListFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, QueueListFragment.class.getName()));

        createTabs();

        PagerAdapter pagerAdapter = new PageAdapter(super.getSupportFragmentManager(), fragments);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mActionBar.setSelectedNavigationItem(position);
            }
        });
        viewPager.setCurrentItem(1);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!PrefUtils.getUsername(this, "").isEmpty()) {
			/* Check if the database if empty, if so, update */
            MediaDbHelper mediaDbHelper = new MediaDbHelper(MainActivity.this);
            if (mediaDbHelper.isEmpty()) {
                LogUtils.d("Empty database, download songs");
                refreshMediaDatabase();
            }
        }

        getApp().startQueueUpdating();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getApp().stopQueueUpdating();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                refreshMediaDatabase();
                return true;
            case R.id.logout:
                PrefUtils.reset(this);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createTabs() {
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }
        };

        mActionBar.addTab(mActionBar.newTab().setText(R.string.favourites).setTabListener(tabListener));
        mActionBar.addTab(mActionBar.newTab().setText(R.string.request).setTabListener(tabListener));
        mActionBar.addTab(mActionBar.newTab().setText(R.string.queue).setTabListener(tabListener));
    }

    private void refreshMediaDatabase() {
        mDialog = new ProgressDialog(MainActivity.this);
        mDialog.setMessage(getString(R.string.loading_media));
        mDialog.setIndeterminate(true);
        mDialog.setCancelable(false);
        mDialog.show();
        mApi.getMedia(new FutureCallback<Boolean>() {
            @Override
            public void onCompleted(Exception e, Boolean success) {
                mDialog.dismiss();

                if (!success) {
                    Toast.makeText(MainActivity.this, R.string.error_loading_media, Toast.LENGTH_SHORT).show();
                } else {
                    // FIXME: fake insert to update list
                    getContentResolver().insert(MediaProvider.CONTENT_URI_FILTER, null);
                    getContentResolver().insert(MediaProvider.CONTENT_URI_FAVOURITES, null);
                }
            }
        });
    }

    class PageAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> fragments;

        public PageAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0)
                return getString(R.string.favourites);
            if (position == 1)
                return getString(R.string.request);
            if (position == 2)
                return getString(R.string.queue);
            return "";
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }

}
