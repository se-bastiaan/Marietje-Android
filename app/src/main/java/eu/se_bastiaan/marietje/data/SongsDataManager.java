package eu.se_bastiaan.marietje.data;

import javax.inject.Inject;
import javax.inject.Singleton;

import eu.se_bastiaan.marietje.data.model.SongsResponse;
import eu.se_bastiaan.marietje.data.remote.SongsService;
import rx.Observable;
import rx.functions.Func1;

@Singleton
public class SongsDataManager {

    private static final Integer PAGE_SIZE = 50;

    private final SongsService songsService;

    @Inject
    public SongsDataManager(SongsService songsService) {
        this.songsService = songsService;
    }

    public Observable<SongsResponse> songs(long page, String searchQuery) {
        return songsService.csrf()
                .flatMap(new Func1<String, Observable<SongsResponse>>() {
                    @Override
                    public Observable<SongsResponse> call(String o) {
                        return songsService.songs(page, PAGE_SIZE, searchQuery, null);
                    }
                })
                .onErrorResumeNext(new Func1<Throwable, Observable<SongsResponse>>() {
                    @Override
                    public Observable<SongsResponse> call(Throwable throwable) {
                        return songsService.songs(page, PAGE_SIZE, searchQuery, null);
                    }
                });
    }

    public Observable<SongsResponse> manageSongs(long page, String artistQuery, String titleQuery) {
        return songsService.manageSongs(page, PAGE_SIZE, artistQuery, titleQuery);
    }
}
