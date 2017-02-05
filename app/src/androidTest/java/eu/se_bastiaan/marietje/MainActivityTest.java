package eu.se_bastiaan.marietje;

import android.content.ComponentName;
import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.HashSet;

import eu.se_bastiaan.marietje.data.model.Song;
import eu.se_bastiaan.marietje.data.model.SongsResponse;
import eu.se_bastiaan.marietje.test.common.TestComponentRule;
import eu.se_bastiaan.marietje.test.common.TestDataFactory;
import eu.se_bastiaan.marietje.ui.login.LoginActivity;
import eu.se_bastiaan.marietje.ui.main.MainActivity;
import rx.Observable;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    public final TestComponentRule component =
            new TestComponentRule(getTargetContext());
    public final ActivityTestRule<MainActivity> main =
            new ActivityTestRule<MainActivity>(MainActivity.class, false, false) {
                @Override
                protected void beforeActivityLaunched() {
                    Intents.init();
                    super.beforeActivityLaunched();
                }

                @Override
                protected Intent getActivityIntent() {
                    // Override the default intent so we pass a false flag for syncing so it doesn't
                    // start a sync service in the background that would affect  the behaviour of
                    // this test.
                    return MainActivity.getStartIntent(getTargetContext());
                }

                @Override
                protected void afterActivityFinished() {
                    Intents.release();
                    super.afterActivityFinished();
                }
            };

    // TestComponentRule needs to go first to make sure the Dagger ApplicationTestComponent is set
    // in the Application before any Activity is launched.
    @Rule
    public final TestRule chain = RuleChain.outerRule(component).around(main);

    @Test
    public void opensLoginActivity() {
        when(component.getMockDataManager().preferencesHelper().getCookies())
                .thenReturn(new HashSet<>());

        main.launchActivity(null);

        intended(hasComponent(new ComponentName(getTargetContext(), LoginActivity.class)));
    }

    @Test
    public void listOfSongsShows() {
        SongsResponse response = TestDataFactory.makeSongsResponse(0);
        when(component.getMockDataManager().songsDataManager().songs(0, null, null))
                .thenReturn(Observable.just(response));
        when(component.getMockDataManager().preferencesHelper().getCookies())
                .thenReturn(new HashSet<>(Collections.singletonList("test")));

        main.launchActivity(null);

        int position = 0;
        for (Song song : response.data()) {
            Espresso.onView(ViewMatchers.withId(R.id.recycler_view))
                    .perform(RecyclerViewActions.scrollToPosition(position));
            Espresso.onView(ViewMatchers.withText(song.title()))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
            Espresso.onView(ViewMatchers.withText(song.artist()))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
            position++;
        }
    }

}