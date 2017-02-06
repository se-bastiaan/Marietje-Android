package eu.se_bastiaan.marietje.data;

import javax.inject.Inject;
import javax.inject.Singleton;

import eu.se_bastiaan.marietje.data.local.PreferencesHelper;
import eu.se_bastiaan.marietje.data.model.Empty;
import eu.se_bastiaan.marietje.data.model.Queue;
import eu.se_bastiaan.marietje.data.remote.ControlService;
import eu.se_bastiaan.marietje.util.TextUtil;
import rx.Observable;
import rx.functions.Func1;

@Singleton
public class ControlDataManager {

    private final ControlService controlService;
    private final PreferencesHelper preferencesHelper;

    @Inject
    public ControlDataManager(ControlService controlService, PreferencesHelper preferencesHelper) {
        this.controlService = controlService;
        this.preferencesHelper = preferencesHelper;
    }

    public Observable<String> csrf() {
        String csrfToken = preferencesHelper.getCsrfToken();
        preferencesHelper.setCsrftoken("");
        return controlService.csrf()
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends String>>() {
                    @Override
                    public Observable<String> call(Throwable throwable) {
                        if (TextUtil.isEmpty(preferencesHelper.getCsrfToken())) {
                            preferencesHelper.setCsrftoken(csrfToken);
                        }
                        return Observable.just(preferencesHelper.getCsrfToken());
                    }
                })
                .map(s -> preferencesHelper.getCsrfToken());
    }

    public Observable<Queue> queue() {
        return controlService.queue();
    }

    public Observable<Empty> skip() {
        return controlService.skip();
    }

    public Observable<Empty> moveUp(long songId) {
        return controlService.moveUp(songId);
    }

    public Observable<Empty> moveDown(long songId) {
        return controlService.moveDown(songId);
    }

    public Observable<Empty> cancel(long songId) {
        return controlService.cancel(songId);
    }

    public Observable<Empty> request(long songId) {
        return controlService.request(songId);
    }

    public Observable<Empty> volumeDown() {
        return controlService.volumeDown();
    }

    public Observable<Empty> volumeUp() {
        return controlService.volumeUp();
    }

}
