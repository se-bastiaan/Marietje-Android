package eu.se_bastiaan.marietje.ui.main.controls;

import android.content.Context;

import javax.inject.Inject;

import eu.se_bastiaan.marietje.data.DataManager;
import eu.se_bastiaan.marietje.injection.ApplicationContext;
import eu.se_bastiaan.marietje.injection.PerFragment;
import eu.se_bastiaan.marietje.ui.base.BasePresenter;

@PerFragment
public class ControlsPresenter extends BasePresenter<ControlsView> {

    private final DataManager dataManager;
    private final Context context;

    @Inject
    public ControlsPresenter(DataManager dataManager, @ApplicationContext Context context) {
        this.dataManager = dataManager;
        this.context = context;
    }
}
