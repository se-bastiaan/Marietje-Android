package eu.se_bastiaan.marietje.ui.base;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Base class that implements the MvpPresenter interface and provides a base implementation for
 * attachView() and detachView(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling mvpView().
 */
public abstract class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    @Nullable
    private V mvpView;
    @NonNull
    private final CompositeSubscription subscriptionsToUnsubscribeOnDetach = new CompositeSubscription();

    @CallSuper
    @Override
    public void attachView(@NonNull V mvpView) {
        if (this.mvpView != null) {
            throw new IllegalStateException("Previous view is not detached! View: " + mvpView);
        }

        this.mvpView = mvpView;
    }

    @CallSuper
    @Override
    public void detachView(@NonNull V mvpView) {
        if (this.mvpView == mvpView) {
            this.mvpView = null;
        } else {
            throw new IllegalStateException("Unexpected view! View: " + mvpView + ", view to detach first: " + this.mvpView);
        }

        subscriptionsToUnsubscribeOnDetach.clear();
    }

    protected boolean isViewAttached() {
        return mvpView != null;
    }

    protected V view() {
        return mvpView;
    }

    protected void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    protected final void unsubscribeOnDetachView(Subscription subscription) {
        subscriptionsToUnsubscribeOnDetach.add(subscription);
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call BasePresenter.attachView(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }
}

