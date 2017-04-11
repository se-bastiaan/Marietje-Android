package eu.se_bastiaan.marietje.ui.main;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import eu.se_bastiaan.marietje.data.DataManager;
import eu.se_bastiaan.marietje.data.local.PreferencesHelper;
import eu.se_bastiaan.marietje.data.model.Permissions;
import eu.se_bastiaan.marietje.events.NeedsSessionCookie;
import eu.se_bastiaan.marietje.injection.PerActivity;
import eu.se_bastiaan.marietje.ui.base.BasePresenter;
import eu.se_bastiaan.marietje.util.EventBus;
import eu.se_bastiaan.marietje.util.RxSubscriber;
import timber.log.Timber;

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

    @Override
    public void attachView(@NonNull MainView mvpView) {
        super.attachView(mvpView);

        this.dataManager.controlDataManager().permissions()
            .subscribe(new RxSubscriber<Permissions>() {
                @Override
                public void onError(Throwable e) {
                    Timber.e(e, "Cannot refresh user permissions");
                }

                @Override
                public void onNext(Permissions permissions) {
                    PreferencesHelper preferencesHelper = dataManager.preferencesHelper();
                    preferencesHelper.setCanSkip(permissions.canSkip());
                    preferencesHelper.setCanCancel(permissions.canCancel());
                    preferencesHelper.setCanMove(permissions.canMove());
                    preferencesHelper.setCanControlVolume(permissions.canControlVolume());
                }
            });
    }
}
