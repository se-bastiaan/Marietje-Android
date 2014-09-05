package nl.ru.science.mariedroid.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;

import butterknife.InjectView;
import nl.ru.science.mariedroid.Constants;
import nl.ru.science.mariedroid.R;

/**
 * Activity which displays a activity_login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends BaseActivity {

    /**
     * Keep track of the activity_login task to ensure we can cancel it if requested.
     */
    private Future<String> mAuthFuture = null;

    private String mUsername;
    private String mPassword;

    @InjectView(R.id.instance)
    Spinner instanceSpinner;
    @InjectView(R.id.username)
    EditText usernameEdit;
    @InjectView(R.id.password)
    EditText passwordEdit;
    @InjectView(R.id.login_form)
    View loginFormView;
    @InjectView(R.id.login_status)
    View loginStatusView;
    @InjectView(R.id.login_status_message)
    TextView loginStatusMessageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_login);

        // Set up the activity_login form.
        usernameEdit.setText(mUsername);

        passwordEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    loginClick(null);
                    return true;
                }
                return false;
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Constants.INSTANCE_NAMES);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        instanceSpinner.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_forgot_password:
                   final AlertDialog d = new AlertDialog.Builder(this)
                        .setPositiveButton(android.R.string.ok, null)
                        .setTitle("Login aanvragen")
                        .setIcon(R.drawable.ic_launcher)
                        .setMessage(Html.fromHtml("Vraag een activity_login aan op <a href=\"http://marietje.marie-curie.nl\">marietje.marie-curie.nl</a> of <a href=\"http://marietje-noord.marie-curie.nl\">marietje-noord.marie-curie.nl</a>"))
                        .create();
                d.show();

                // make text clickable
                ((TextView)d.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Attempts to sign in or register the account specified by the activity_login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual activity_login attempt is made.
     */
    public void loginClick(View v) {
        if (mAuthFuture != null) {
            return;
        }

        // Reset errors.
        usernameEdit.setError(null);
        passwordEdit.setError(null);

        // Store values at the time of the activity_login attempt.
        mUsername = usernameEdit.getText().toString();
        mPassword = passwordEdit.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(mPassword)) {
            passwordEdit.setError(getString(R.string.error_field_required));
            focusView = passwordEdit;
            cancel = true;
        }

        // Check for a valid username.
        if (TextUtils.isEmpty(mUsername)) {
            usernameEdit.setError(getString(R.string.error_field_required));
            focusView = usernameEdit;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt activity_login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user activity_login attempt.
            loginStatusMessageView.setText(R.string.login_progress_signing_in);
            showProgress(true);
            mAuthFuture = mApi.authenticateUser(instanceSpinner.getSelectedItemPosition(), mUsername, mPassword, new FutureCallback<Boolean>() {
                @Override
                public void onCompleted(Exception e, Boolean result) {
                    if(!result) {
                        Toast.makeText(LoginActivity.this, R.string.incorrect_login, Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new  Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    mAuthFuture = null;
                    showProgress(false);
                }
            });
        }
    }

    /**
     * Shows the progress UI and hides the activity_login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);

            loginStatusView.setVisibility(View.VISIBLE);
            loginStatusView.animate().setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            loginStatusView.setVisibility(show ? View.VISIBLE
                                    : View.GONE);
                        }
                    });

            loginFormView.setVisibility(View.VISIBLE);
            loginFormView.animate().setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            loginFormView.setVisibility(show ? View.GONE
                                    : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            loginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
