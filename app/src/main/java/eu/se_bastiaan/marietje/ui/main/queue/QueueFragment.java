package eu.se_bastiaan.marietje.ui.main.queue;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import javax.inject.Inject;

import butterknife.BindView;
import eu.se_bastiaan.marietje.R;
import eu.se_bastiaan.marietje.data.model.PlaylistSong;
import eu.se_bastiaan.marietje.data.model.Queue;
import eu.se_bastiaan.marietje.injection.ActivityContext;
import eu.se_bastiaan.marietje.injection.component.FragmentComponent;
import eu.se_bastiaan.marietje.ui.base.BaseFragment;
import eu.se_bastiaan.marietje.ui.main.MainActivity;

public class QueueFragment extends BaseFragment implements QueueView, PlaylistAdapter.Listener {

    @Inject
    QueuePresenter presenter;
    @Inject
    PlaylistAdapter playlistAdapter;
    @Inject
    @ActivityContext
    Context context;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    public static QueueFragment newInstance() {
        Bundle args = new Bundle();
        QueueFragment fragment = new QueueFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState, R.layout.fragment_queue);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);

        playlistAdapter.setListener(this);

        recyclerView.setAdapter(playlistAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        presenter.loadData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView(this);
    }

    @Override
    public void showQueue(Queue queue) {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        playlistAdapter.setQueue(queue);
        playlistAdapter.notifyDataSetChanged();
        if (getActivity().getIntent().getBooleanExtra(MainActivity.EXTRA_AUTO_REFRESH_QUEUE, true)) {
            presenter.loadData(10);
        }
    }

    @Override
    public void showLoading() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadingError() {
        progressBar.setVisibility(View.GONE);
        Snackbar.make(recyclerView, R.string.queue_loading_error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onSongClicked(int position, PlaylistSong playlistSong) {
        Snackbar.make(recyclerView, R.string.functionality_missing, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void injectFragment(FragmentComponent component) {
        component.inject(this);
    }

}
