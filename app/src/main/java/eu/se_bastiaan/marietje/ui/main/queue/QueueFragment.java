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

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import eu.se_bastiaan.marietje.R;
import eu.se_bastiaan.marietje.data.model.PlaylistSong;
import eu.se_bastiaan.marietje.data.model.Queue;
import eu.se_bastiaan.marietje.injection.ActivityContext;
import eu.se_bastiaan.marietje.injection.component.FragmentComponent;
import eu.se_bastiaan.marietje.ui.base.BaseFragment;
import eu.se_bastiaan.marietje.ui.common.dialog.MenuBottomSheetDialogFragment;
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
    public void showMoveDownSuccess() {
        Snackbar.make(recyclerView, R.string.queue_move_down_success, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showMoveDownError() {
        Snackbar.make(recyclerView, R.string.queue_move_down_error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showRemoveSuccess() {
        Snackbar.make(recyclerView, R.string.queue_remove_success, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showRemoveError() {
        Snackbar.make(recyclerView, R.string.queue_remove_error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onSongClicked(int position, PlaylistSong playlistSong) {
        List<String> options = Arrays.asList(getString(R.string.queue_move_down), getString(R.string.queue_remove));
        MenuBottomSheetDialogFragment.newInstance(options, (menu_position, item) -> {
            switch (menu_position) {
                case 0:
                    presenter.moveSongDownInQueue(playlistSong.objectId());
                    break;
                case 1:
                    presenter.removeSongFromQueue(playlistSong.objectId());
                    break;
            }
        }).show(getChildFragmentManager(), "song_options_menu");
    }

    @Override
    protected void injectFragment(FragmentComponent component) {
        component.inject(this);
    }

}
