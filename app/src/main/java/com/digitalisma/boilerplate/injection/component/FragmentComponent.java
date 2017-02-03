package com.digitalisma.boilerplate.injection.component;

import com.digitalisma.boilerplate.injection.PerFragment;
import com.digitalisma.boilerplate.injection.module.FragmentModule;

import dagger.Subcomponent;

@PerFragment
@Subcomponent(modules = {
        FragmentModule.class
})
public interface FragmentComponent {


}