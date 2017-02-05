package eu.se_bastiaan.marietje.ui.main.request;

import java.util.List;

import eu.se_bastiaan.marietje.data.model.Song;
import eu.se_bastiaan.marietje.ui.base.MvpView;

public interface RequestView extends MvpView {

    void showSongs(List<Song> songList, boolean clear, boolean moreAvailable);
    void showSongsEmpty();
    void showLoadingError();
    void showLoading();
    void showRequestSuccess();
    void showRequestError();

}
