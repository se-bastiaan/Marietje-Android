package eu.se_bastiaan.marietje.ui.main.queue;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import eu.se_bastiaan.marietje.R;
import eu.se_bastiaan.marietje.data.model.PlaylistSong;
import eu.se_bastiaan.marietje.injection.component.FragmentComponent;
import eu.se_bastiaan.marietje.ui.base.BaseFragment;

public class QueueFragment extends BaseFragment implements QueueView {

    @Inject
    QueuePresenter presenter;

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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView(this);
    }

    @Override
    public void showQueue(List<PlaylistSong> songs) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    protected void injectFragment(FragmentComponent component) {
        component.inject(this);
    }
}
