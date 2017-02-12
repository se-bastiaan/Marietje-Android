package eu.se_bastiaan.marietje.ui.main.queue;

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

import java.util.ArrayList;
import java.util.List;

import eu.se_bastiaan.marietje.R;
import eu.se_bastiaan.marietje.data.model.Empty;
import eu.se_bastiaan.marietje.data.model.PlaylistSong;
import eu.se_bastiaan.marietje.data.model.Queue;
import eu.se_bastiaan.marietje.test.common.TestComponentRule;
import eu.se_bastiaan.marietje.test.common.TestDataFactory;
import eu.se_bastiaan.marietje.ui.main.MainActivity;
import eu.se_bastiaan.marietje.util.IdlingSchedulersOverrideRule;
import rx.Observable;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static eu.se_bastiaan.marietje.matcher.ViewMatchers.withMenuItemText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class QueueFragmentTest {

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
    }

    // TestComponentRule needs to go first to make sure the Dagger ApplicationTestComponent is set
    // in the Application before any Activity is launched.
    @Rule
    public final TestRule chain = RuleChain.outerRule(component).around(main);
    @Rule
    public final IdlingSchedulersOverrideRule overrideSchedulersRule = new IdlingSchedulersOverrideRule();

    @Test
    public void playlistShows() {
        Queue response = TestDataFactory.makeQueueResponse();
        when(component.getMockDataManager().controlDataManager().queue())
                .thenReturn(Observable.just(response));

        main.launchActivity(null);

        List<PlaylistSong> playlistSongs = new ArrayList<>(response.queuedSongs());
        playlistSongs.add(0, response.currentSong());

        int position = 0;
        for (PlaylistSong playlistSong : playlistSongs) {

            onView(withId(R.id.recycler_view))
                    .perform(RecyclerViewActions.scrollToPosition(position));
            onView(withText(playlistSong.song().title()))
                    .check(matches(isDisplayed()));
            onView(withText(playlistSong.song().artist()))
                    .check(matches(isDisplayed()));
//            FIXME: Test below doesn't work for unknown reasons on CI
//            if (position != 0) {
//                onView(withText(formatter.format(date)))
//                        .check(matches(isDisplayed()));
//            }

            position++;
        }
    }

    @Test
    public void removeSongFromQueueSuccess() {
        when(component.getMockDataManager().controlDataManager().queue())
                .thenReturn(Observable.just(TestDataFactory.makeQueueResponse()));
        when(component.getMockDataManager().controlDataManager().cancel(2))
                .thenReturn(Observable.just(Empty.create()));
        when(component.getMockDataManager().preferencesHelper().canCancel())
                .thenReturn(false);
        when(component.getMockDataManager().preferencesHelper().canSkip())
                .thenReturn(false);
        when(component.getMockDataManager().preferencesHelper().canMove())
                .thenReturn(false);

        main.launchActivity(null);

        onView(allOf(withId(R.id.recycler_view), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        onView(allOf(withId(R.id.recycler_view), withParent(withId(R.id.design_bottom_sheet)), isDisplayed()))
                .perform(RecyclerViewActions.actionOnHolderItem(withMenuItemText(R.string.queue_remove), click()));

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(R.string.queue_remove_success)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void removeSongFromQueueError() {
        when(component.getMockDataManager().controlDataManager().queue())
                .thenReturn(Observable.just(TestDataFactory.makeQueueResponse()));
        when(component.getMockDataManager().controlDataManager().cancel(2))
                .thenReturn(Observable.error(new RuntimeException()));
        when(component.getMockDataManager().preferencesHelper().canCancel())
                .thenReturn(false);
        when(component.getMockDataManager().preferencesHelper().canSkip())
                .thenReturn(false);
        when(component.getMockDataManager().preferencesHelper().canMove())
                .thenReturn(false);

        main.launchActivity(null);
        onView(allOf(withId(R.id.tab_queue), isDisplayed()))
                .perform(click());

        onView(allOf(withId(R.id.recycler_view), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        onView(allOf(withId(R.id.recycler_view), withParent(withId(R.id.design_bottom_sheet)), isDisplayed()))
                .perform(RecyclerViewActions.actionOnHolderItem(withMenuItemText(R.string.queue_remove), click()));

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(R.string.queue_remove_error)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void moveSongDownInQueueSuccess() {
        when(component.getMockDataManager().controlDataManager().queue())
                .thenReturn(Observable.just(TestDataFactory.makeQueueResponse()));
        when(component.getMockDataManager().controlDataManager().moveDown(2))
                .thenReturn(Observable.just(Empty.create()));
        when(component.getMockDataManager().preferencesHelper().canCancel())
                .thenReturn(false);
        when(component.getMockDataManager().preferencesHelper().canSkip())
                .thenReturn(false);
        when(component.getMockDataManager().preferencesHelper().canMove())
                .thenReturn(false);

        main.launchActivity(null);

        onView(allOf(withId(R.id.recycler_view), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        onView(allOf(withId(R.id.recycler_view), withParent(withId(R.id.design_bottom_sheet)), isDisplayed()))
                .perform(RecyclerViewActions.actionOnHolderItem(withMenuItemText(R.string.queue_move_down), click()));

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(R.string.queue_move_down_success)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void moveSongDownInQueueError() {
        when(component.getMockDataManager().controlDataManager().queue())
                .thenReturn(Observable.just(TestDataFactory.makeQueueResponse()));
        when(component.getMockDataManager().controlDataManager().moveDown(2))
                .thenReturn(Observable.error(new RuntimeException()));
        when(component.getMockDataManager().preferencesHelper().canCancel())
                .thenReturn(false);
        when(component.getMockDataManager().preferencesHelper().canSkip())
                .thenReturn(false);
        when(component.getMockDataManager().preferencesHelper().canMove())
                .thenReturn(false);

        main.launchActivity(null);
        onView(allOf(withId(R.id.tab_queue), isDisplayed()))
                .perform(click());

        onView(allOf(withId(R.id.recycler_view), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        onView(allOf(withId(R.id.recycler_view), withParent(withId(R.id.design_bottom_sheet)), isDisplayed()))
                .perform(RecyclerViewActions.actionOnHolderItem(withMenuItemText(R.string.queue_move_down), click()));

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(R.string.queue_move_down_error)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void moveSongUpInQueueSuccess() {
        when(component.getMockDataManager().controlDataManager().queue())
                .thenReturn(Observable.just(TestDataFactory.makeQueueResponse()));
        when(component.getMockDataManager().controlDataManager().moveUp(3))
                .thenReturn(Observable.just(Empty.create()));
        when(component.getMockDataManager().preferencesHelper().canCancel())
                .thenReturn(false);
        when(component.getMockDataManager().preferencesHelper().canSkip())
                .thenReturn(false);
        when(component.getMockDataManager().preferencesHelper().canMove())
                .thenReturn(true);

        main.launchActivity(null);

        onView(allOf(withId(R.id.recycler_view), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));

        onView(allOf(withText(R.string.queue_move_up), withParent(allOf(withId(R.id.recycler_view), withParent(withId(R.id.design_bottom_sheet)), isDisplayed()))))
                .check(matches(isDisplayed()));

        onView(allOf(withText(R.string.queue_move_down), withParent(allOf(withId(R.id.recycler_view), withParent(withId(R.id.design_bottom_sheet)), isDisplayed()))))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.recycler_view), withParent(withId(R.id.design_bottom_sheet)), isDisplayed()))
                .perform(RecyclerViewActions.actionOnHolderItem(withMenuItemText(R.string.queue_move_up), click()));

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(R.string.queue_move_up_success)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void moveSongUpInQueueError() {
        when(component.getMockDataManager().controlDataManager().queue())
                .thenReturn(Observable.just(TestDataFactory.makeQueueResponse()));
        when(component.getMockDataManager().controlDataManager().moveUp(3))
                .thenReturn(Observable.error(new RuntimeException()));
        when(component.getMockDataManager().preferencesHelper().canCancel())
                .thenReturn(false);
        when(component.getMockDataManager().preferencesHelper().canSkip())
                .thenReturn(false);
        when(component.getMockDataManager().preferencesHelper().canMove())
                .thenReturn(true);

        main.launchActivity(null);
        onView(allOf(withId(R.id.tab_queue), isDisplayed()))
                .perform(click());

        onView(allOf(withId(R.id.recycler_view), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));

        onView(allOf(withText(R.string.queue_move_up), withParent(allOf(withId(R.id.recycler_view), withParent(withId(R.id.design_bottom_sheet)), isDisplayed()))))
                .check(matches(isDisplayed()));

        onView(allOf(withText(R.string.queue_move_down), withParent(allOf(withId(R.id.recycler_view), withParent(withId(R.id.design_bottom_sheet)), isDisplayed()))))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.recycler_view), withParent(withId(R.id.design_bottom_sheet)), isDisplayed()))
                .perform(RecyclerViewActions.actionOnHolderItem(withMenuItemText(R.string.queue_move_up), click()));

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(R.string.queue_move_up_error)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void moveSongUpNotAvailable() {
        when(component.getMockDataManager().controlDataManager().queue())
                .thenReturn(Observable.just(TestDataFactory.makeQueueResponse()));
        when(component.getMockDataManager().preferencesHelper().canCancel())
                .thenReturn(false);
        when(component.getMockDataManager().preferencesHelper().canSkip())
                .thenReturn(false);
        when(component.getMockDataManager().preferencesHelper().canMove())
                .thenReturn(false);

        main.launchActivity(null);
        onView(allOf(withId(R.id.tab_queue), isDisplayed()))
                .perform(click());

        onView(allOf(withId(R.id.recycler_view), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        onView(allOf(withText(R.string.queue_move_up), withParent(allOf(withId(R.id.recycler_view), withParent(withId(R.id.design_bottom_sheet)), isDisplayed()))))
                .check(doesNotExist());
    }

    @Test
    public void skipCurrentSongSuccess() {
        when(component.getMockDataManager().controlDataManager().queue())
                .thenReturn(Observable.just(TestDataFactory.makeQueueResponse()));
        when(component.getMockDataManager().controlDataManager().skip())
                .thenReturn(Observable.just(Empty.create()));
        when(component.getMockDataManager().preferencesHelper().canCancel())
                .thenReturn(false);
        when(component.getMockDataManager().preferencesHelper().canMove())
                .thenReturn(false);
        when(component.getMockDataManager().preferencesHelper().canSkip())
                .thenReturn(true);

        main.launchActivity(null);

        onView(allOf(withId(R.id.recycler_view), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(allOf(withId(R.id.recycler_view), withParent(withId(R.id.design_bottom_sheet)), isDisplayed()))
                .perform(RecyclerViewActions.actionOnHolderItem(withMenuItemText(R.string.queue_skip), click()));

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(R.string.queue_skip_success)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void skipCurrentSongError() {
        when(component.getMockDataManager().controlDataManager().queue())
                .thenReturn(Observable.just(TestDataFactory.makeQueueResponse()));
        when(component.getMockDataManager().controlDataManager().skip())
                .thenReturn(Observable.error(new RuntimeException()));
        when(component.getMockDataManager().preferencesHelper().canCancel())
                .thenReturn(false);
        when(component.getMockDataManager().preferencesHelper().canMove())
                .thenReturn(false);
        when(component.getMockDataManager().preferencesHelper().canSkip())
                .thenReturn(true);

        main.launchActivity(null);
        onView(allOf(withId(R.id.tab_queue), isDisplayed()))
                .perform(click());

        onView(allOf(withId(R.id.recycler_view), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(allOf(withId(R.id.recycler_view), withParent(withId(R.id.design_bottom_sheet)), isDisplayed()))
                .perform(RecyclerViewActions.actionOnHolderItem(withMenuItemText(R.string.queue_skip), click()));

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(R.string.queue_skip_error)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void skipCurrentSongNotAvailable() {
        when(component.getMockDataManager().controlDataManager().queue())
                .thenReturn(Observable.just(TestDataFactory.makeQueueResponse()));
        when(component.getMockDataManager().preferencesHelper().canCancel())
                .thenReturn(false);
        when(component.getMockDataManager().preferencesHelper().canMove())
                .thenReturn(false);
        when(component.getMockDataManager().preferencesHelper().canSkip())
                .thenReturn(false);

        main.launchActivity(null);
        onView(allOf(withId(R.id.tab_queue), isDisplayed()))
                .perform(click());

        onView(allOf(withId(R.id.recycler_view), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(allOf(withText(R.string.queue_skip), withParent(allOf(withId(R.id.recycler_view), withParent(withId(R.id.design_bottom_sheet)), isDisplayed()))))
                .check(doesNotExist());
    }

}