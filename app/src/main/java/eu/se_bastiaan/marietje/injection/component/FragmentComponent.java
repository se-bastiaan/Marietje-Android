package eu.se_bastiaan.marietje.injection.component;

import eu.se_bastiaan.marietje.injection.PerFragment;
import eu.se_bastiaan.marietje.injection.module.FragmentModule;

import dagger.Subcomponent;

@PerFragment
@Subcomponent(modules = {
        FragmentModule.class
})
public interface FragmentComponent {


}