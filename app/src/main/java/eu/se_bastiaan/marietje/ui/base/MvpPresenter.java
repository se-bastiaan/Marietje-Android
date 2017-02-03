package eu.se_bastiaan.marietje.ui.base;

/**
 * Every presenter in the app must either implement this interface or extend BasePresenter
 * indicating the MvpView type that wants to be attached with.
 */
public interface MvpPresenter<V extends MvpView> {

    void attachView(V mvpView);

    void detachView(V mvpView);

}
