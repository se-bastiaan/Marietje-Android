package eu.se_bastiaan.marietje.ui.main.controls;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import eu.se_bastiaan.marietje.R;
import eu.se_bastiaan.marietje.injection.component.FragmentComponent;
import eu.se_bastiaan.marietje.ui.base.BaseFragment;

public class ControlsFragment extends BaseFragment implements ControlsView {

    @Inject
    ControlsPresenter presenter;

    public static ControlsFragment newInstance() {
        Bundle args = new Bundle();
        ControlsFragment fragment = new ControlsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState, R.layout.fragment_controls);
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
    protected void injectFragment(FragmentComponent component) {
        component.inject(this);
    }
}
