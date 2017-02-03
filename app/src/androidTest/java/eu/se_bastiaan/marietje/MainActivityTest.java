package eu.se_bastiaan.marietje;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import eu.se_bastiaan.marietje.data.model.Song;
import eu.se_bastiaan.marietje.data.model.SongsResponse;
import eu.se_bastiaan.marietje.test.common.TestComponentRule;
import eu.se_bastiaan.marietje.test.common.TestDataFactory;
import eu.se_bastiaan.marietje.ui.main.MainActivity;
import rx.Observable;

import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    public final TestComponentRule component =
            new TestComponentRule(InstrumentationRegistry.getTargetContext());
    public final ActivityTestRule<MainActivity> main =
            new ActivityTestRule<MainActivity>(MainActivity.class, false, false) {
                @Override
                protected Intent getActivityIntent() {
                    // Override the default intent so we pass a false flag for syncing so it doesn't
                    // start a sync service in the background that would affect  the behaviour of
                    // this test.
                    return MainActivity.getStartIntent(InstrumentationRegistry.getTargetContext());
                }
            };

    // TestComponentRule needs to go first to make sure the Dagger ApplicationTestComponent is set
    // in the Application before any Activity is launched.
    @Rule
    public final TestRule chain = RuleChain.outerRule(component).around(main);

    @Test
    public void listOfPersonsShows() {
        SongsResponse response = TestDataFactory.makeSongsResponse(0);
        when(component.getMockDataManager().songsDataManager().songs(0, null, null))
                .thenReturn(Observable.just(response));

        main.launchActivity(null);

        int position = 0;
        for (Song song : response.data()) {
            Espresso.onView(ViewMatchers.withId(eu.se_bastiaan.marietje.R.id.recycler_view))
                    .perform(RecyclerViewActions.scrollToPosition(position));
            Espresso.onView(ViewMatchers.withText(song.title()))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
            Espresso.onView(ViewMatchers.withText(song.artist()))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
            position++;
        }
    }

}