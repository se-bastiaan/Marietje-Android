package com.digitalisma.boilerplate.ui.main;

import com.digitalisma.boilerplate.data.DataManager;
import com.digitalisma.boilerplate.data.model.Person;
import com.digitalisma.boilerplate.injection.ConfigPersistent;
import com.digitalisma.boilerplate.injection.PerActivity;
import com.digitalisma.boilerplate.ui.base.BasePresenter;
import com.digitalisma.boilerplate.util.RxSubscriber;
import com.digitalisma.boilerplate.util.RxUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
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

    public void loadPersons() {
        checkViewAttached();
        RxUtil.unsubscribe(subscription);
        subscription = dataManager.getPersons()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new RxSubscriber<List<Person>>() {
                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the persons.");
                        view().showError();
                    }

                    @Override
                    public void onNext(List<Person> persons) {
                        if (persons.isEmpty()) {
                            view().showPersonsEmpty();
                        } else {
                            view().showPersons(persons);
                        }
                    }
                });
        add(subscription);
    }

}
