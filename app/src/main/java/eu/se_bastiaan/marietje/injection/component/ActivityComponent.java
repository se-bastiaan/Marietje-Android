package eu.se_bastiaan.marietje.injection.component;

import dagger.Subcomponent;
import eu.se_bastiaan.marietje.injection.PerActivity;
import eu.se_bastiaan.marietje.injection.module.ActivityModule;
import eu.se_bastiaan.marietje.injection.module.DeveloperSettingsModule;
import eu.se_bastiaan.marietje.injection.module.FragmentModule;
import eu.se_bastiaan.marietje.ui.login.LoginActivity;
import eu.se_bastiaan.marietje.ui.main.MainActivity;

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
    void inject(LoginActivity loginActivity);

    FragmentComponent fragmentComponent(FragmentModule fragmentModule);

}
