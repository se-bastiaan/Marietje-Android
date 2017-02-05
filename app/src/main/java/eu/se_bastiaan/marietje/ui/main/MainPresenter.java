package eu.se_bastiaan.marietje.ui.main;

import javax.inject.Inject;

import eu.se_bastiaan.marietje.data.DataManager;
import eu.se_bastiaan.marietje.events.NeedsSessionCookie;
import eu.se_bastiaan.marietje.injection.PerActivity;
import eu.se_bastiaan.marietje.ui.base.BasePresenter;
import eu.se_bastiaan.marietje.util.EventBus;
import eu.se_bastiaan.marietje.util.RxSubscriber;

@PerActivity
public class MainPresenter extends BasePresenter<MainView> {

    private final DataManager dataManager;
    private final EventBus eventBus;

    @Inject
    public MainPresenter(DataManager dataManager, EventBus eventBus) {
        this.dataManager = dataManager;
        this.eventBus = eventBus;

        this.eventBus.register(NeedsSessionCookie.class, new RxSubscriber<NeedsSessionCookie>() {
            @Override
            public void onNext(NeedsSessionCookie needsSessionCookie) {
                if (isViewAttached()) {
                    view().showLogin();
                }
            }
        });
    }

}
