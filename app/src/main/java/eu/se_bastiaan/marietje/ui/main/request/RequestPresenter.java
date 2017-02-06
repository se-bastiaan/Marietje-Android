package eu.se_bastiaan.marietje.ui.main.request;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import eu.se_bastiaan.marietje.data.DataManager;
import eu.se_bastiaan.marietje.data.model.Empty;
import eu.se_bastiaan.marietje.data.model.Songs;
import eu.se_bastiaan.marietje.injection.ApplicationContext;
import eu.se_bastiaan.marietje.injection.PerFragment;
import eu.se_bastiaan.marietje.ui.base.BasePresenter;
import eu.se_bastiaan.marietje.util.RxSubscriber;
import eu.se_bastiaan.marietje.util.RxUtil;
import eu.se_bastiaan.marietje.util.TextUtil;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

@PerFragment
public class RequestPresenter extends BasePresenter<RequestView> {

    private final DataManager dataManager;
    private final Context context;

    private String currentQuery;
    private int currentPage = 0;
    private Subscription searchSubscription;

    @Inject
    public RequestPresenter(DataManager dataManager, @ApplicationContext Context context) {
        this.dataManager = dataManager;
        this.context = context;
    }

    @Override
    public void attachView(@NonNull RequestView mvpView) {
        super.attachView(mvpView);
    }

    public void searchSong(@NonNull String queryStr) {
        if (queryStr.length() > 0 && queryStr.length() < 3) {
            return;
        }

        if (TextUtil.equals(queryStr, currentQuery)) {
            currentPage++;
        } else {
            currentPage = 1;
        }

        currentQuery = queryStr;
        doSearch(queryStr);
    }

    private void doSearch(String queryStr) {
        checkViewAttached();

        RxUtil.unsubscribe(searchSubscription);

        if (currentPage == 1) {
            view().showLoading();
        }

        searchSubscription = dataManager.songsDataManager().songs(currentPage, queryStr)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<Songs>() {
                    @Override
                    public void onNext(Songs response) {
                        if (response.data().isEmpty()) {
                            view().showSongsEmpty();
                        } else {
                            view().showSongs(response.data(), response.currentPage() == 1, response.currentPage() != response.lastPage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.w(e);
                        view().showLoadingError();
                    }
                });

        unsubscribeOnDetachView(searchSubscription);
    }

    public void requestSong(long songId) {
        checkViewAttached();

        Subscription subscription = dataManager.controlDataManager().request(songId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<Empty>() {
                    @Override
                    public void onNext(Empty emptyResponse) {
                        view().showRequestSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.w(e);
                        view().showRequestError();
                    }
                });

        unsubscribeOnDetachView(subscription);
    }

}
