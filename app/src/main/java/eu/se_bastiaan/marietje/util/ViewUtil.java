package eu.se_bastiaan.marietje.util;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public final class ViewUtil {

    private ViewUtil() {
    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }

        hideKeyboard(activity, view);
    }

    public static void hideKeyboard(Fragment fragment, View rootView) {
        hideKeyboard(fragment.getActivity(), rootView);
    }

}
