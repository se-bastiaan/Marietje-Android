package com.digitalisma.boilerplate;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.digitalisma.boilerplate.data.model.Person;
import com.digitalisma.boilerplate.test.common.TestComponentRule;
import com.digitalisma.boilerplate.test.common.TestDataFactory;
import com.digitalisma.boilerplate.ui.main.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.util.List;

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
        List<Person> testDataPersons = TestDataFactory.makeListPersons(20);
        when(component.getMockDataManager().getPersons())
                .thenReturn(Observable.just(testDataPersons));

        main.launchActivity(null);

        int position = 0;
        for (Person person : testDataPersons) {
            Espresso.onView(ViewMatchers.withId(R.id.recycler_view))
                    .perform(RecyclerViewActions.scrollToPosition(position));
            String name = String.format("%s %s", person.name().first(),
                    person.name().last());
            Espresso.onView(ViewMatchers.withText(name))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
            Espresso.onView(ViewMatchers.withText(person.email()))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
            position++;
        }
    }

}