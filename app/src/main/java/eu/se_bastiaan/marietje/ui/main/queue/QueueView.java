package eu.se_bastiaan.marietje.ui.main.queue;

import eu.se_bastiaan.marietje.data.model.Queue;
import eu.se_bastiaan.marietje.ui.base.MvpView;

public interface QueueView extends MvpView {

    void showQueue(Queue queue);
    void showLoadingError();
    void showLoading();

    void showMoveDownSuccess();
    void showMoveDownError();

    void showRemoveSuccess();
    void showRemoveError();

    void showMoveUpSuccess();
    void showMoveUpError();

    void showSkipSuccess();
    void showSkipError();

}
