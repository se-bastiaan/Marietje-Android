package com.digitalisma.boilerplate;

import com.digitalisma.boilerplate.data.DataManager;
import com.digitalisma.boilerplate.data.local.PreferencesHelper;
import com.digitalisma.boilerplate.data.model.Person;
import com.digitalisma.boilerplate.data.model.ServerResponse;
import com.digitalisma.boilerplate.data.remote.PersonsService;
import com.digitalisma.boilerplate.test.common.TestDataFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * This test class performs local unit tests without dependencies on the Android framework
 * For testing methods in the DataManager follow this approach:
 * 1. Stub mock helper classes that your method relies on. e.g. RetrofitServices or DatabaseHelper
 * 2. Test the Observable using TestSubscriber
 * 3. Optionally write a SEPARATE test that verifies that your method is calling the right helper
 * using Mockito.verify()
 */
@RunWith(MockitoJUnitRunner.class)
public class DataManagerTest {

    @Mock
    PreferencesHelper mockPreferencesHelper;
    @Mock
    PersonsService mockPersonsService;
    private DataManager dataManager;

    @Before
    public void setUp() {
        dataManager = new DataManager(mockPersonsService, mockPreferencesHelper);
    }

    @Test
    public void getPersonsEmitsValues() {
        List<Person> persons = Arrays.asList(TestDataFactory.makePerson("r1"),
                TestDataFactory.makePerson("r2"));
        stubGetPersonsHelperCalls(persons);

        TestSubscriber<List<Person>> result = new TestSubscriber<>();
        dataManager.getPersons().subscribe(result);
        result.assertNoErrors();

        result.assertReceivedOnNext(Collections.singletonList(persons));
    }

    @Test
    public void getPersonsCallsApi() {
        List<Person> persons = Arrays.asList(TestDataFactory.makePerson("r1"),
                TestDataFactory.makePerson("r2"));
        stubGetPersonsHelperCalls(persons);

        dataManager.getPersons().subscribe();
        // Verify right calls to helper methods
        verify(mockPersonsService).getPersons();
    }

    private void stubGetPersonsHelperCalls(List<Person> persons) {
        // Stub calls to the ribot service and database helper.
        when(mockPersonsService.getPersons())
                .thenReturn(Observable.just(ServerResponse.create(persons)));
    }

}
