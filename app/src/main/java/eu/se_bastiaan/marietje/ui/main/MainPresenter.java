package eu.se_bastiaan.marietje.ui.main;

import javax.inject.Inject;

import eu.se_bastiaan.marietje.data.DataManager;
import eu.se_bastiaan.marietje.data.model.SongsResponse;
import eu.se_bastiaan.marietje.injection.PerActivity;
import eu.se_bastiaan.marietje.ui.base.BasePresenter;
import eu.se_bastiaan.marietje.util.RxSubscriber;
import eu.se_bastiaan.marietje.util.RxUtil;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@PerActivity
public class MainPresenter extends BasePresenter<MainView> {

    private final DataManager dataManager;
    private Subscription subscription;

    @Inject
    public MainPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void loadSongs() {
        checkViewAttached();
        RxUtil.unsubscribe(subscription);

        subscription = dataManager.songsDataManager().songs(0, null, null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new RxSubscriber<SongsResponse>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the persons.");
                        view().showError();
                    }

                    @Override
                    public void onNext(SongsResponse response) {
                        if (response.data().isEmpty()) {
                            view().showSongsEmpty();
                        } else {
                            view().showSongs(response.data());
                        }
                    }
                });
        unsubscribeOnDetachView(subscription);
    }

}
