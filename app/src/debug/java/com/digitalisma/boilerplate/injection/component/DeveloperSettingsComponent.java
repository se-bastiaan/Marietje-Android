package com.digitalisma.boilerplate.injection.component;

import dagger.Subcomponent;
import com.digitalisma.boilerplate.ui.fragments.DeveloperSettingsFragment;

@Subcomponent
public interface DeveloperSettingsComponent {
    void inject(DeveloperSettingsFragment developerSettingsFragment);
}