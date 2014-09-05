// Generated code from Butter Knife. Do not modify!
package nl.ru.science.mariedroid.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class LoginActivity$$ViewInjector {
  public static void inject(Finder finder, final nl.ru.science.mariedroid.activities.LoginActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165247, "field 'instanceSpinner'");
    target.instanceSpinner = (android.widget.Spinner) view;
    view = finder.findRequiredView(source, 2131165248, "field 'usernameEdit'");
    target.usernameEdit = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131165249, "field 'passwordEdit'");
    target.passwordEdit = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131165246, "field 'loginFormView'");
    target.loginFormView = view;
    view = finder.findRequiredView(source, 2131165244, "field 'loginStatusView'");
    target.loginStatusView = view;
    view = finder.findRequiredView(source, 2131165245, "field 'loginStatusMessageView'");
    target.loginStatusMessageView = (android.widget.TextView) view;
  }

  public static void reset(nl.ru.science.mariedroid.activities.LoginActivity target) {
    target.instanceSpinner = null;
    target.usernameEdit = null;
    target.passwordEdit = null;
    target.loginFormView = null;
    target.loginStatusView = null;
    target.loginStatusMessageView = null;
  }
}
