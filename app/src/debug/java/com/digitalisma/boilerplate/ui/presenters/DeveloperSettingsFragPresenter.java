package com.digitalisma.boilerplate.ui.presenters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.digitalisma.boilerplate.devsettings.DeveloperSettingsModelImpl;
import com.digitalisma.boilerplate.ui.fragments.DeveloperSettingsFragment;
import okhttp3.logging.HttpLoggingInterceptor;

public class DeveloperSettingsFragPresenter {

    private DeveloperSettingsModelImpl developerSettingsModel;
    @Nullable
    private volatile DeveloperSettingsFragment view;

    public DeveloperSettingsFragPresenter(DeveloperSettingsModelImpl developerSettingsModel) {
        this.developerSettingsModel = developerSettingsModel;
    }

    public void bindView(DeveloperSettingsFragment view) {
        this.view = view;

        view.changeGitSha(developerSettingsModel.getGitSha());
        view.changeBuildDate(developerSettingsModel.getBuildDate());
        view.changeBuildVersionCode(developerSettingsModel.getBuildVersionCode());
        view.changeBuildVersionName(developerSettingsModel.getBuildVersionName());
        view.changeStethoState(developerSettingsModel.isStethoEnabled());
        view.changeLeakCanaryState(developerSettingsModel.isLeakCanaryEnabled());
        view.changeHttpLoggingLevel(developerSettingsModel.getHttpLoggingLevel());
    }

    public void changeStethoState(boolean enabled) {
        if (developerSettingsModel.isStethoEnabled() == enabled) {
            return; // no-op
        }

        boolean stethoWasEnabled = developerSettingsModel.isStethoEnabled();

        developerSettingsModel.changeStethoState(enabled);

        if (view != null) {
            view.showMessage("Stetho was " + booleanToEnabledDisabled(enabled));

            if (stethoWasEnabled) {
                view.showAppNeedsToBeRestarted();
            }
        }
    }

    public void changeLeakCanaryState(boolean enabled) {
        if (developerSettingsModel.isLeakCanaryEnabled() == enabled) {
            return; // no-op
        }

        developerSettingsModel.changeLeakCanaryState(enabled);

        if (view != null) {
            view.showMessage("LeakCanary was " + booleanToEnabledDisabled(enabled));
            view.showAppNeedsToBeRestarted(); // LeakCanary can not be enabled on demand (or it's possible?)
        }
    }

    public void changeHttpLoggingLevel(@NonNull HttpLoggingInterceptor.Level loggingLevel) {
        if (developerSettingsModel.getHttpLoggingLevel() == loggingLevel) {
            return; // no-op
        }

        developerSettingsModel.changeHttpLoggingLevel(loggingLevel);

        if (view != null) {
            view.showMessage("Http logging level was changed to " + loggingLevel.toString());
        }
    }

    @NonNull
    private static String booleanToEnabledDisabled(boolean enabled) {
        return enabled ? "enabled" : "disabled";
    }

}
