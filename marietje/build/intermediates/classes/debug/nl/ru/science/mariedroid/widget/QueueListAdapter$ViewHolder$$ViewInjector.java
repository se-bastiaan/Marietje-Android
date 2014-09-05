// Generated code from Butter Knife. Do not modify!
package nl.ru.science.mariedroid.widget;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class QueueListAdapter$ViewHolder$$ViewInjector {
  public static void inject(Finder finder, final nl.ru.science.mariedroid.widget.QueueListAdapter.ViewHolder target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165253, "field 'text1'");
    target.text1 = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131165255, "field 'text2'");
    target.text2 = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131165254, "field 'text3'");
    target.text3 = (android.widget.TextView) view;
  }

  public static void reset(nl.ru.science.mariedroid.widget.QueueListAdapter.ViewHolder target) {
    target.text1 = null;
    target.text2 = null;
    target.text3 = null;
  }
}
