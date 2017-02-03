package eu.se_bastiaan.marietje.ui.other;

import android.support.annotation.NonNull;
import android.view.View;

public class NoOpViewModifier implements ViewModifier {
    @NonNull
    @Override
    public <T extends View> T modify(@NonNull T view) {
        // no-op
        return view;
    }
}