package com.digitalisma.boilerplate.injection.component;

import com.digitalisma.boilerplate.injection.PerActivity;
import com.digitalisma.boilerplate.injection.module.ActivityModule;
import com.digitalisma.boilerplate.injection.module.DeveloperSettingsModule;
import com.digitalisma.boilerplate.injection.module.FragmentModule;
import com.digitalisma.boilerplate.ui.main.MainActivity;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = {
        ActivityModule.class,
        DeveloperSettingsModule.class
})
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    FragmentComponent fragmentComponent(FragmentModule fragmentModule);

}
