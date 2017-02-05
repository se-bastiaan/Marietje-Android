package eu.se_bastiaan.marietje.ui.common.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

import eu.se_bastiaan.marietje.R;

@SuppressLint("ValidFragment")
public class ConfirmationDialogFragment extends DialogFragment {

    private static final String ARG_MESSAGE = "message";
    private static final String ARG_POSITIVE = "positive";
    private static final String ARG_NEGATIVE = "negative";
    private Listener listener;

    /**
     * Creates a new instance of ConfirmationDialogFragment.
     *
     * @return A new instance.
     */
    public static ConfirmationDialogFragment newInstance(@StringRes Integer messageRes, @StringRes Integer positiveRes, @StringRes Integer negativeRes, Listener listener) {
        ConfirmationDialogFragment fragment = new ConfirmationDialogFragment(listener);
        Bundle args = new Bundle();
        args.putInt(ARG_MESSAGE, messageRes);
        args.putInt(ARG_POSITIVE, positiveRes);
        if (negativeRes != null) {
            args.putInt(ARG_NEGATIVE, negativeRes);
        }
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Creates a new instance of ConfirmationDialogFragment.
     *
     * @return A new instance.
     */
    public static ConfirmationDialogFragment newInstance(String message, @StringRes Integer positiveRes, @StringRes Integer negativeRes, Listener listener) {
        ConfirmationDialogFragment fragment = new ConfirmationDialogFragment(listener);
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        args.putInt(ARG_POSITIVE, positiveRes);
        if (negativeRes != null) {
            args.putInt(ARG_NEGATIVE, negativeRes);
        }
        fragment.setArguments(args);
        return fragment;
    }

    public ConfirmationDialogFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public ConfirmationDialogFragment(Listener listener) {
        super();
        this.listener = listener;
        setCancelable(false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String message = "";

        try {
            message = getArguments().getString(ARG_MESSAGE, "");
        } catch (ClassCastException ignore) {
            // Ignore problems
        }

        if (message.isEmpty()) {
            message = getActivity().getString(getArguments().getInt(ARG_MESSAGE));
        }

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity(), R.style.Theme_Marietje_Dialog)
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton(getArguments().getInt(ARG_POSITIVE), (dialog, which) -> {
                    if (listener != null) {
                        listener.onConfirmation(true);
                    }
                });

        if (getArguments().containsKey(ARG_NEGATIVE)) {
            alertBuilder.setNegativeButton(getArguments().getInt(ARG_NEGATIVE), (dialog, which) -> {
                if (listener != null) {
                    listener.onConfirmation(false);
                }
            });
        }

        return alertBuilder.create();
    }

    public void show(FragmentManager manager) {
        super.show(manager, "confirm_dialog");
    }

    /**
     * Callback for the user's response.
     */
    public interface Listener {

        /**
         * @param allowed True if the user allowed the request.
         */
        void onConfirmation(boolean allowed);
    }

}