package eu.se_bastiaan.marietje.data;

import javax.inject.Inject;
import javax.inject.Singleton;

import eu.se_bastiaan.marietje.data.local.PreferencesHelper;

@Singleton
public class DataManager {

    private final SongsDataManager songsDataManager;
    private final ControlDataManager controlDataManager;
    private final PreferencesHelper preferencesHelper;

    @Inject
    public DataManager(SongsDataManager songsDataManager, ControlDataManager controlDataManager, PreferencesHelper preferencesHelper) {
        this.songsDataManager = songsDataManager;
        this.controlDataManager = controlDataManager;
        this.preferencesHelper = preferencesHelper;
    }

    public PreferencesHelper preferencesHelper() {
        return preferencesHelper;
    }

    public SongsDataManager songsDataManager() {
        return songsDataManager;
    }

    public ControlDataManager controlDataManager() {
        return controlDataManager;
    }

}
