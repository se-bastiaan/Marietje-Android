package eu.se_bastiaan.marietje.data;

import javax.inject.Inject;
import javax.inject.Singleton;

import eu.se_bastiaan.marietje.data.model.SongsResponse;
import eu.se_bastiaan.marietje.data.remote.SongsService;
import rx.Observable;

@Singleton
public class SongsDataManager {

    private static final Integer PAGE_SIZE = 100;

    private final SongsService songsService;

    @Inject
    public SongsDataManager(SongsService songsService) {
        this.songsService = songsService;
    }

    public Observable<SongsResponse> songs(long page, String searchQuery, String uploaderQuery) {
        return songsService.songs(page, PAGE_SIZE, searchQuery, null);
    }

    public Observable<SongsResponse> manageSongs(long page, String artistQuery, String titleQuery) {
        return songsService.manageSongs(page, PAGE_SIZE, artistQuery, titleQuery);
    }
}
