package eu.se_bastiaan.marietje.injection.component;

import dagger.Subcomponent;
import eu.se_bastiaan.marietje.injection.PerFragment;
import eu.se_bastiaan.marietje.injection.module.FragmentModule;
import eu.se_bastiaan.marietje.ui.main.controls.ControlsFragment;
import eu.se_bastiaan.marietje.ui.main.queue.QueueFragment;
import eu.se_bastiaan.marietje.ui.main.request.RequestFragment;

@PerFragment
@Subcomponent(modules = {
        FragmentModule.class
})
public interface FragmentComponent {
    void inject(RequestFragment fragment);
    void inject(QueueFragment fragment);
    void inject(ControlsFragment fragment);
}