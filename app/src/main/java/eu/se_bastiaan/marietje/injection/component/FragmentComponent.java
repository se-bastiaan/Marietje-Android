package eu.se_bastiaan.marietje.injection.component;

import dagger.Subcomponent;
import eu.se_bastiaan.marietje.injection.PerFragment;
import eu.se_bastiaan.marietje.injection.module.FragmentModule;

@PerFragment
@Subcomponent(modules = {
        FragmentModule.class
})
public interface FragmentComponent {


}