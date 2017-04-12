package eu.se_bastiaan.marietje.ui.main;

import android.content.ComponentName;
import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import eu.se_bastiaan.marietje.R;
import eu.se_bastiaan.marietje.test.common.TestComponentRule;
import eu.se_bastiaan.marietje.test.common.TestDataFactory;
import eu.se_bastiaan.marietje.ui.login.LoginActivity;
import rx.Observable;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.verify;
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
                    return MainActivity.getStartIntent(getTargetContext(), false);
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

    @Before
    public void setUp() {
        when(component.getMockDataManager().controlDataManager().queue())
                .thenReturn(Observable.just(TestDataFactory.makeQueueResponse()));
        when(component.getMockDataManager().controlDataManager().permissions())
                .thenReturn(Observable.just(TestDataFactory.makePermissionsResponse()));
    }

    @Test
    public void noSessionOpensLoginActivity() {
        when(component.getMockDataManager().preferencesHelper().getSessionId())
                .thenReturn("");

        main.launchActivity(null);

        intended(hasComponent(new ComponentName(getTargetContext(), LoginActivity.class)));
    }

    @Test
    public void logoutOpensLoginActivity() {
        when(component.getMockDataManager().preferencesHelper().getSessionId())
                .thenReturn("session");

        main.launchActivity(null);

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.action_logout))
                .perform(click());

        verify(component.getMockDataManager().preferencesHelper()).clear();
        intended(hasComponent(new ComponentName(getTargetContext(), LoginActivity.class)));
    }


}
