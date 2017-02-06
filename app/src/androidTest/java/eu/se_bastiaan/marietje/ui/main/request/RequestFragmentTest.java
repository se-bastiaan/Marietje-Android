package eu.se_bastiaan.marietje.ui.main.request;

import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import eu.se_bastiaan.marietje.R;
import eu.se_bastiaan.marietje.data.model.Empty;
import eu.se_bastiaan.marietje.data.model.Song;
import eu.se_bastiaan.marietje.data.model.Songs;
import eu.se_bastiaan.marietje.test.common.TestComponentRule;
import eu.se_bastiaan.marietje.test.common.TestDataFactory;
import eu.se_bastiaan.marietje.ui.main.MainActivity;
import eu.se_bastiaan.marietje.util.IdlingSchedulersOverrideRule;
import rx.Observable;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class RequestFragmentTest {

    public final TestComponentRule component =
            new TestComponentRule(getTargetContext());
    public final ActivityTestRule<MainActivity> main =
            new ActivityTestRule<MainActivity>(MainActivity.class, false, false) {
                @Override
                protected Intent getActivityIntent() {
                    // Override the default intent so we pass a false flag for syncing so it doesn't
                    // start a sync service in the background that would affect  the behaviour of
                    // this test.
                    return MainActivity.getStartIntent(getTargetContext(), false);
                }
            };

    @Before
    public void setUp() {
        when(component.getMockDataManager().preferencesHelper().getSessionId())
                .thenReturn("sessionId");
        when(component.getMockDataManager().controlDataManager().queue())
                .thenReturn(Observable.just(TestDataFactory.makeQueueResponse()));
    }

    // TestComponentRule needs to go first to make sure the Dagger ApplicationTestComponent is set
    // in the Application before any Activity is launched.
    @Rule
    public final TestRule chain = RuleChain.outerRule(component).around(main);
    @Rule
    public final IdlingSchedulersOverrideRule overrideSchedulersRule = new IdlingSchedulersOverrideRule();

    @Test
    public void listOfSongsShows() {
        Songs response = TestDataFactory.makeSongsResponse(1);
        when(component.getMockDataManager().songsDataManager().songs(1, ""))
                .thenReturn(Observable.just(response));
        when(component.getMockDataManager().songsDataManager().songs(2, ""))
                .thenReturn(Observable.just(TestDataFactory.makeSongsResponse(2)));

        main.launchActivity(null);

        onView(allOf(withId(R.id.tab_request), isDisplayed()))
                .perform(click());

        int position = 0;
        for (Song song : response.data()) {
            onView(withId(R.id.recycler_view))
                    .perform(RecyclerViewActions.scrollToPosition(position));
            onView(withText(song.title()))
                    .check(matches(isDisplayed()));
            onView(withText(song.artist()))
                    .check(matches(isDisplayed()));
            onView(withText(song.durationStr()))
                    .check(matches(isDisplayed()));
            position++;
        }
    }

    @Test
    public void requestSongSuccess() {
        Songs response = TestDataFactory.makeSongsResponse(1);
        when(component.getMockDataManager().songsDataManager().songs(1, ""))
                .thenReturn(Observable.just(response));
        when(component.getMockDataManager().controlDataManager().request(1))
                .thenReturn(Observable.just(Empty.create()));

        main.launchActivity(null);
        onView(allOf(withId(R.id.tab_request), isDisplayed()))
                .perform(click());

        onView(allOf(withId(R.id.recycler_view), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(allOf(withId(android.R.id.button1), withText(R.string.dialog_action_ok)))
                .perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(R.string.request_success)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void requestSongError() {
        Songs response = TestDataFactory.makeSongsResponse(1);
        when(component.getMockDataManager().songsDataManager().songs(1, ""))
                .thenReturn(Observable.just(response));
        when(component.getMockDataManager().controlDataManager().request(1))
                .thenReturn(Observable.error(new RuntimeException()));

        main.launchActivity(null);
        onView(allOf(withId(R.id.tab_request), isDisplayed()))
                .perform(click());

        onView(allOf(withId(R.id.recycler_view), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(allOf(withId(android.R.id.button1), withText(R.string.dialog_action_ok)))
                .perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(R.string.request_error)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void requestSongCanceled() {
        Songs response = TestDataFactory.makeSongsResponse(1);
        when(component.getMockDataManager().songsDataManager().songs(1, ""))
                .thenReturn(Observable.just(response));

        main.launchActivity(null);
        onView(allOf(withId(R.id.tab_request), isDisplayed()))
                .perform(click());

        onView(allOf(withId(R.id.recycler_view), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(allOf(withId(android.R.id.button2), withText(R.string.dialog_action_cancel)))
                .perform(click());
    }


}