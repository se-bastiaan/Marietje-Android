package eu.se_bastiaan.marietje.injection.component;

import dagger.Subcomponent;
import eu.se_bastiaan.marietje.ui.fragments.DeveloperSettingsFragment;

@Subcomponent
public interface DeveloperSettingsComponent {
    void inject(DeveloperSettingsFragment developerSettingsFragment);
}