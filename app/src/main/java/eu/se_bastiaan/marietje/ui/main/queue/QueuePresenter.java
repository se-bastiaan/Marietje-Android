package eu.se_bastiaan.marietje.ui.main.queue;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import eu.se_bastiaan.marietje.data.DataManager;
import eu.se_bastiaan.marietje.data.model.Queue;
import eu.se_bastiaan.marietje.events.NeedsCsrfToken;
import eu.se_bastiaan.marietje.injection.ApplicationContext;
import eu.se_bastiaan.marietje.injection.PerFragment;
import eu.se_bastiaan.marietje.ui.base.BasePresenter;
import eu.se_bastiaan.marietje.util.EventBus;
import eu.se_bastiaan.marietje.util.RxSubscriber;
import eu.se_bastiaan.marietje.util.RxUtil;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

@PerFragment
public class QueuePresenter extends BasePresenter<QueueView> {

    private final DataManager dataManager;
    private final Context context;
    private final EventBus eventBus;
    private Subscription loadingSubscription;

    @Inject
    public QueuePresenter(DataManager dataManager, @ApplicationContext Context context, EventBus eventBus) {
        this.dataManager = dataManager;
        this.context = context;
        this.eventBus = eventBus;
    }

    public void loadData() {
        loadData(0);
    }

    public void loadData(long delay) {
        checkViewAttached();

        if (loadingSubscription == null) {
            view().showLoading();
        }

        RxUtil.unsubscribe(loadingSubscription);

        loadingSubscription = Observable.timer(delay, TimeUnit.SECONDS)
                .flatMap(o -> dataManager.controlDataManager().queue())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<Queue>() {
                    @Override
                    public void onNext(Queue queue) {
                        view().showQueue(queue);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.w(e);
                        view().showLoadingError();

                        if (e instanceof HttpException) {
                            HttpException httpException = (HttpException) e;
                            if (httpException.code() == 403) {
                                eventBus.post(new NeedsCsrfToken());
                            }
                        }
                    }
                });

        unsubscribeOnDetachView(loadingSubscription);
    }

}
