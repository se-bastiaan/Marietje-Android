package eu.se_bastiaan.marietje.ui.main.queue;

import android.content.Context;

import javax.inject.Inject;

import eu.se_bastiaan.marietje.data.DataManager;
import eu.se_bastiaan.marietje.injection.ApplicationContext;
import eu.se_bastiaan.marietje.injection.PerFragment;
import eu.se_bastiaan.marietje.ui.base.BasePresenter;

@PerFragment
public class QueuePresenter extends BasePresenter<QueueView> {

    private final DataManager dataManager;
    private final Context context;

    @Inject
    public QueuePresenter(DataManager dataManager, @ApplicationContext Context context) {
        this.dataManager = dataManager;
        this.context = context;
    }

}
