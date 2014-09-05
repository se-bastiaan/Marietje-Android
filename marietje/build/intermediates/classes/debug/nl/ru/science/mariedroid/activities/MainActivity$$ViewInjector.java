// Generated code from Butter Knife. Do not modify!
package nl.ru.science.mariedroid.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class MainActivity$$ViewInjector {
  public static void inject(Finder finder, final nl.ru.science.mariedroid.activities.MainActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165252, "field 'viewPager'");
    target.viewPager = (android.support.v4.view.ViewPager) view;
  }

  public static void reset(nl.ru.science.mariedroid.activities.MainActivity target) {
    target.viewPager = null;
  }
}
