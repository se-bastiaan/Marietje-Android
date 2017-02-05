package eu.se_bastiaan.marietje.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.se_bastiaan.marietje.R;
import eu.se_bastiaan.marietje.data.local.PreferencesHelper;
import eu.se_bastiaan.marietje.data.model.Song;
import eu.se_bastiaan.marietje.injection.component.ActivityComponent;
import eu.se_bastiaan.marietje.injection.module.DeveloperSettingsModule;
import eu.se_bastiaan.marietje.ui.base.BaseActivity;
import eu.se_bastiaan.marietje.ui.login.LoginActivity;
import eu.se_bastiaan.marietje.ui.other.ViewModifier;

public class MainActivity extends BaseActivity implements MainView {

    @Inject
    MainPresenter presenter;
    @Inject
    SongsAdapter songsAdapter;
    @Inject
    PreferencesHelper preferencesHelper;
    @Inject
    @Named(DeveloperSettingsModule.MAIN_ACTIVITY_VIEW_MODIFIER)
    ViewModifier viewModifier;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    /**
     * Return an Intent to start this Activity.
     */
    public static Intent getStartIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @SuppressLint("InflateParams") // In this case it's ok
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(viewModifier.modify(getLayoutInflater().inflate(R.layout.activity_main, null)));
        ButterKnife.bind(this);
        presenter.attachView(this);

        if (preferencesHelper.getCookies().isEmpty()) {
            startActivity(LoginActivity.getStartIntent(this));
            finish();
            return;
        }

        recyclerView.setAdapter(songsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        presenter.loadSongs();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView(this);
    }

    // MVP View methods implementation

    @Override
    public void showSongs(List<Song> songs) {
        songsAdapter.setSongs(songs);
        songsAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError() {
        Snackbar.make(recyclerView, R.string.error_loading_songs, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showSongsEmpty() {
        songsAdapter.setSongs(Collections.<Song>emptyList());
        songsAdapter.notifyDataSetChanged();
        Snackbar.make(recyclerView, R.string.empty_songs, Snackbar.LENGTH_LONG).show();
    }

    // End MVP View methods implementation

    @Override
    protected void injectActivity(ActivityComponent component) {
        component.inject(this);
    }

}
