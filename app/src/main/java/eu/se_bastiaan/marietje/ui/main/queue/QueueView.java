package eu.se_bastiaan.marietje.ui.main.queue;

import java.util.List;

import eu.se_bastiaan.marietje.data.model.PlaylistSong;
import eu.se_bastiaan.marietje.ui.base.MvpView;

public interface QueueView extends MvpView {

    void showQueue(List<PlaylistSong> songs);
    void showMessage(String message);

}
