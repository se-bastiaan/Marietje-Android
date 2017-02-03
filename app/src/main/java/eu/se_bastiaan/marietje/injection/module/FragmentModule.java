package eu.se_bastiaan.marietje.injection.module;

import android.app.Activity;
import android.content.Context;

import eu.se_bastiaan.marietje.injection.ActivityContext;

import dagger.Module;
import dagger.Provides;

@Module
public class FragmentModule {

    private Context context;

    public FragmentModule(Activity context) {
        this.context = context;
    }

    @Provides
    @ActivityContext
    Context providesContext() {
        return context;
    }

}