package eu.se_bastiaan.marietje.ui.main;

import java.util.List;

import eu.se_bastiaan.marietje.data.model.Song;
import eu.se_bastiaan.marietje.ui.base.MvpView;

public interface MainView extends MvpView {

    void showSongs(List<Song> songs);

    void showSongsEmpty();

    void showError();

}
