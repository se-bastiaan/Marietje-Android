package eu.se_bastiaan.marietje.injection.module;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import eu.se_bastiaan.marietje.injection.ActivityContext;

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