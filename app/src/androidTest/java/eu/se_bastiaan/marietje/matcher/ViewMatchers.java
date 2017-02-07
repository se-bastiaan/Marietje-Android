package eu.se_bastiaan.marietje.matcher;

import android.content.res.Resources;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import eu.se_bastiaan.marietje.R;
import eu.se_bastiaan.marietje.ui.common.dialog.MenuBottomSheetAdapter;

public class ViewMatchers {

    public static Matcher<RecyclerView.ViewHolder> withMenuItemText(final int resourceId) {
        return new BoundedMatcher<RecyclerView.ViewHolder, MenuBottomSheetAdapter.ViewHolder>(MenuBottomSheetAdapter.ViewHolder.class) {
            @Override
            protected boolean matchesSafely(MenuBottomSheetAdapter.ViewHolder viewHolder) {
                TextView textView = (TextView) viewHolder.itemView.findViewById(R.id.text_item);

                if (null == expectedText) {
                    try {
                        expectedText = textView.getResources().getString(resourceId);
                        resourceName = textView.getResources().getResourceEntryName(resourceId);
                    } catch (Resources.NotFoundException ignored) {
                        /* view could be from a context unaware of the resource id. */
                    }
                }
                CharSequence actualText = textView.getText();
                return null != expectedText && null != actualText && expectedText.equals(actualText.toString());
            }

            private String resourceName = null;
            private String expectedText = null;

            @Override
            public void describeTo(Description description) {
                description.appendText("with string from resource id: ");
                description.appendValue(resourceId);
                if (null != resourceName) {
                    description.appendText("[");
                    description.appendText(resourceName);
                    description.appendText("]");
                }
                if (null != expectedText) {
                    description.appendText(" value: ");
                    description.appendText(expectedText);
                }
            }
        };
    }


}
