package eu.se_bastiaan.marietje.ui.main.request;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.ybq.endless.Endless;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import eu.se_bastiaan.marietje.R;
import eu.se_bastiaan.marietje.data.model.Song;
import eu.se_bastiaan.marietje.injection.ActivityContext;
import eu.se_bastiaan.marietje.injection.component.FragmentComponent;
import eu.se_bastiaan.marietje.ui.base.BaseFragment;
import eu.se_bastiaan.marietje.ui.common.dialog.ConfirmationDialogFragment;

public class RequestFragment extends BaseFragment implements RequestView, SongsAdapter.Listener {

    private Endless endless;

    @Inject
    SongsAdapter songsAdapter;
    @Inject
    RequestPresenter presenter;
    @Inject
    @ActivityContext
    Context context;

    View rootView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    SearchView searchView;

    public static RequestFragment newInstance() {
        Bundle args = new Bundle();
        RequestFragment fragment = new RequestFragment();
        fragment.setHasOptionsMenu(true);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState, R.layout.fragment_request);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);

        songsAdapter.setListener(this);

        recyclerView.setAdapter(songsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        View loadingView = LayoutInflater.from(getActivity()).inflate(R.layout.item_endless_loading, null);
        loadingView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        endless = Endless.applyTo(recyclerView, loadingView);

        endless.setLoadMoreListener(page -> {
            presenter.searchSong(searchView.getQuery().toString());
        });

        presenter.searchSong("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_request, menu);

        MenuItem searchItem = menu.findItem( R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                presenter.searchSong(s);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    public void showLoading() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRequestSuccess() {
        Snackbar.make(recyclerView, R.string.request_success, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showRequestError() {
        Snackbar.make(recyclerView, R.string.request_error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showSongs(List<Song> songs, boolean clear, boolean moreAvailable) {
        endless.loadMoreComplete();
        progressBar.setVisibility(View.GONE);
        if (clear) {
            songsAdapter.setSongs(songs);
        } else {
            songsAdapter.addSongs(songs);
        }
        songsAdapter.notifyDataSetChanged();
        recyclerView.setVisibility(View.VISIBLE);
        endless.setLoadMoreAvailable(moreAvailable);
    }

    @Override
    public void showSongsEmpty() {
        progressBar.setVisibility(View.GONE);
        songsAdapter.setSongs(new ArrayList<>());
        songsAdapter.notifyDataSetChanged();
        recyclerView.setVisibility(View.VISIBLE);
        Snackbar.make(recyclerView, R.string.request_songs_empty, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadingError() {
        progressBar.setVisibility(View.GONE);
        Snackbar.make(recyclerView, R.string.request_songs_error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onSongClicked(int position, Song song) {
        ConfirmationDialogFragment.newInstance(getString(R.string.request_confirm, song.title(), song.artist()), R.string.dialog_action_ok, R.string.dialog_action_cancel,
                allowed -> {
                   if (allowed) {
                       presenter.requestSong(song.objectId());
                   }
                }).show(getChildFragmentManager());
    }

    @Override
    protected void injectFragment(FragmentComponent component) {
        component.inject(this);
    }
}
