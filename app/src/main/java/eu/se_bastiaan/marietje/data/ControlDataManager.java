package eu.se_bastiaan.marietje.data;

import javax.inject.Inject;
import javax.inject.Singleton;

import eu.se_bastiaan.marietje.data.model.EmptyResponse;
import eu.se_bastiaan.marietje.data.model.QueueResponse;
import eu.se_bastiaan.marietje.data.remote.ControlService;
import rx.Observable;

@Singleton
public class ControlDataManager {

    private final ControlService controlService;

    @Inject
    public ControlDataManager(ControlService controlService) {
        this.controlService = controlService;
    }

    public Observable<QueueResponse> queue() {
        return controlService.queue();
    }

    public Observable<EmptyResponse> skip() {
        return controlService.skip();
    }

    public Observable<EmptyResponse> moveUp(long songId) {
        return controlService.moveUp(songId);
    }

    public Observable<EmptyResponse> moveDown(long songId) {
        return controlService.moveDown(songId);
    }

    public Observable<EmptyResponse> cancel(long songId) {
        return controlService.cancel(songId);
    }

    public Observable<EmptyResponse> request(long songId) {
        return controlService.request(songId);
    }

    public Observable<EmptyResponse> volumeDown() {
        return controlService.volumeDown();
    }

    public Observable<EmptyResponse> volumeUp() {
        return controlService.volumeUp();
    }

}
