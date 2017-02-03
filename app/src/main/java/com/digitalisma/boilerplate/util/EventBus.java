package com.digitalisma.boilerplate.util;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;
import timber.log.Timber;

/**
 * A simple event bus built with RxJava
 */
@Singleton
public class EventBus {

    @Inject
    public EventBus() {
        // Public empty constructor
    }

    private final Subject<Object, Object> busSubject = new SerializedSubject<>(PublishSubject.create());

    public <T> Subscription register(final Class<T> eventClass, Action1<T> onNext) {
        return busSubject
                .filter(event -> event.getClass().equals(eventClass))
                .map(obj -> (T) obj)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    Timber.w(throwable, "Error in EventBus");
                })
                .onErrorResumeNext(throwable -> Observable.empty())
                .subscribe(onNext);
    }

    public <T> Subscription register(final Class<T> eventClass, Subscriber<T> subscriber) {
        return busSubject
                .filter(event -> event.getClass().equals(eventClass))
                .map(obj -> (T) obj)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    Timber.w(throwable, "Error in EventBus");
                })
                .onErrorResumeNext(throwable -> Observable.empty())
                .subscribe(subscriber);
    }

    public <T> Observable<T> register(final Class<T> eventClass) {
        return busSubject
                .filter(event -> event.getClass().equals(eventClass))
                .map(obj -> (T) obj)
                .doOnError(throwable -> {
                    Timber.w(throwable, "Error in EventBus");
                })
                .onErrorResumeNext(throwable -> Observable.empty())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void post(Object event) {
        busSubject.onNext(event);
    }

}
