package eu.se_bastiaan.marietje.ui.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import eu.se_bastiaan.marietje.MarietjeApp;
import eu.se_bastiaan.marietje.data.local.PreferencesHelper;
import eu.se_bastiaan.marietje.ui.base.BaseFragment;
import com.github.pedrovgs.lynx.LynxActivity;
import com.github.pedrovgs.lynx.LynxConfig;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import eu.se_bastiaan.marietje.ui.adapters.DeveloperSettingsSpinnerAdapter;
import eu.se_bastiaan.marietje.ui.presenters.DeveloperSettingsFragPresenter;
import eu.se_bastiaan.marietje.ui.views.DeveloperSettingsView;

import eu.se_bastiaan.marietje.R;
import okhttp3.logging.HttpLoggingInterceptor;

public class DeveloperSettingsFragment extends BaseFragment implements DeveloperSettingsView {

    @Inject
    LynxConfig lynxConfig;
    @Inject
    PreferencesHelper preferencesHelper;
    @Inject
    DeveloperSettingsFragPresenter presenter;
    @BindView(R.id.developer_settings_git_sha_text_view)
    TextView gitShaTextView;
    @BindView(R.id.developer_settings_build_date_text_view)
    TextView buildDateTextView;
    @BindView(R.id.developer_settings_build_version_code_text_view)
    TextView buildVersionCodeTextView;
    @BindView(R.id.developer_settings_build_version_name_text_view)
    TextView buildVersionNameTextView;
    @BindView(R.id.developer_settings_stetho_switch)
    Switch stethoSwitch;
    @BindView(R.id.developer_settings_leak_canary_switch)
    Switch leakCanarySwitch;
    @BindView(R.id.developer_settings_http_logging_level_spinner)
    Spinner httpLoggingLevelSpinner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MarietjeApp.get(getActivity()).getComponent().plusDeveloperSettingsComponent().inject(this);
        presenter.bindView(this);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_developer_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        httpLoggingLevelSpinner
                .setAdapter(new DeveloperSettingsSpinnerAdapter<>(getActivity().getLayoutInflater())
                        .setSelectionOptions(HttpLoggingLevel.allValues()));
    }

    @OnCheckedChanged(R.id.developer_settings_stetho_switch)
    void onStethoSwitchCheckedChanged(boolean checked) {
        presenter.changeStethoState(checked);
    }

    @OnCheckedChanged(R.id.developer_settings_leak_canary_switch)
    void onLeakCanarySwitchCheckedChanged(boolean checked) {
        presenter.changeLeakCanaryState(checked);
    }

    @OnItemSelected(R.id.developer_settings_http_logging_level_spinner)
    void onHttpLoggingLevelChanged(int position) {
        presenter.changeHttpLoggingLevel(((HttpLoggingLevel) httpLoggingLevelSpinner.getItemAtPosition(position)).loggingLevel);
    }

    @Override
    public void changeGitSha(@NonNull final String gitSha) {
        runOnUiThreadIfFragmentAlive(() -> {
            assert gitShaTextView != null;
            gitShaTextView.setText(gitSha);
        });
    }

    @Override
    public void changeBuildDate(@NonNull final String date) {
        runOnUiThreadIfFragmentAlive(() -> {
            assert buildDateTextView != null;
            buildDateTextView.setText(date);
        });
    }

    @Override
    public void changeBuildVersionCode(@NonNull final String versionCode) {
        runOnUiThreadIfFragmentAlive(() -> {
            assert buildVersionCodeTextView != null;
            buildVersionCodeTextView.setText(versionCode);
        });
    }

    @Override
    public void changeBuildVersionName(@NonNull final String versionName) {
        runOnUiThreadIfFragmentAlive(() -> {
            assert buildVersionNameTextView != null;
            buildVersionNameTextView.setText(versionName);
        });
    }

    @Override
    public void changeStethoState(final boolean enabled) {
        runOnUiThreadIfFragmentAlive(() -> {
            assert stethoSwitch != null;
            stethoSwitch.setChecked(enabled);
        });
    }

    @Override
    public void changeLeakCanaryState(final boolean enabled) {
        runOnUiThreadIfFragmentAlive(() -> {
            assert leakCanarySwitch != null;
            leakCanarySwitch.setChecked(enabled);
        });
    }

    @Override
    public void changeHttpLoggingLevel(@NonNull final HttpLoggingInterceptor.Level loggingLevel) {
        runOnUiThreadIfFragmentAlive(() -> {
            assert httpLoggingLevelSpinner != null;

            for (int position = 0, count = httpLoggingLevelSpinner.getCount(); position < count; position++) {
                if (loggingLevel == ((HttpLoggingLevel) httpLoggingLevelSpinner.getItemAtPosition(position)).loggingLevel) {
                    httpLoggingLevelSpinner.setSelection(position);
                    return;
                }
            }

            throw new IllegalStateException("Unknown loggingLevel, looks like a serious bug. Passed loggingLevel = " + loggingLevel);
        });
    }

    @Override
    public void showMessage(@NonNull final String message) {
        runOnUiThreadIfFragmentAlive(() -> Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void showAppNeedsToBeRestarted() {
        runOnUiThreadIfFragmentAlive(() -> Toast.makeText(getContext(), "To apply new settings app needs to be restarted", Toast.LENGTH_LONG).show());
    }

    @OnClick(R.id.b_change_baseurl)
    void changeBaseUrl() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_Marietje);
        builder.setTitle("API Base Url");
        LinearLayout linearLayout = new LinearLayout(getActivity());
        final EditText input = new EditText(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(lp);
        input.setLayoutParams(lp);
        input.setText(preferencesHelper.getApiUrl());
        linearLayout.addView(input);
        builder.setView(linearLayout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            preferencesHelper.clear();
            preferencesHelper.setApiUrl(input.getText().toString());
            showAppNeedsToBeRestarted();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @OnClick(R.id.b_show_log)
    void showLog() {
        Context context = getActivity();
        context.startActivity(LynxActivity.getIntent(context, lynxConfig));
    }

    private static class HttpLoggingLevel implements DeveloperSettingsSpinnerAdapter.SelectionOption {

        @NonNull
        public final HttpLoggingInterceptor.Level loggingLevel;

        HttpLoggingLevel(@NonNull HttpLoggingInterceptor.Level loggingLevel) {
            this.loggingLevel = loggingLevel;
        }

        @NonNull
        @Override
        public String getTitle() {
            return loggingLevel.toString();
        }

        @NonNull
        static List<HttpLoggingLevel> allValues() {
            final HttpLoggingInterceptor.Level[] loggingLevels = HttpLoggingInterceptor.Level.values();
            final List<HttpLoggingLevel> values = new ArrayList<>(loggingLevels.length);
            for (HttpLoggingInterceptor.Level loggingLevel : loggingLevels) {
                values.add(new HttpLoggingLevel(loggingLevel));
            }
            return values;
        }
    }
}