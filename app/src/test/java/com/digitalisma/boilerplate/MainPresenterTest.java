package com.digitalisma.boilerplate;

import com.digitalisma.boilerplate.data.DataManager;
import com.digitalisma.boilerplate.data.model.Person;
import com.digitalisma.boilerplate.test.common.TestDataFactory;
import com.digitalisma.boilerplate.ui.main.MainPresenter;
import com.digitalisma.boilerplate.ui.main.MainView;
import com.digitalisma.boilerplate.util.RxSchedulersOverrideRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import rx.Observable;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {

    @Mock
    MainView mockMainView;
    @Mock DataManager mockDataManager;
    private MainPresenter mainPresenter;

    @Rule
    public final RxSchedulersOverrideRule overrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp() {
        mainPresenter = new MainPresenter(mockDataManager);
        mainPresenter.attachView(mockMainView);
    }

    @After
    public void tearDown() {
        mainPresenter.detachView();
    }

    @Test
    public void loadPersonsReturnsPersons() {
        List<Person> persons = TestDataFactory.makeListPersons(10);
        when(mockDataManager.getPersons())
                .thenReturn(Observable.just(persons));

        mainPresenter.loadPersons();
        verify(mockMainView).showPersons(persons);
        verify(mockMainView, never()).showPersonsEmpty();
        verify(mockMainView, never()).showError();
    }

    @Test
    public void loadRibotsReturnsEmptyList() {
        when(mockDataManager.getPersons())
                .thenReturn(Observable.just(Collections.<Person>emptyList()));

        mainPresenter.loadPersons();
        verify(mockMainView).showPersonsEmpty();
        verify(mockMainView, never()).showPersons(anyListOf(Person.class));
        verify(mockMainView, never()).showError();
    }

    @Test
    public void loadPersonsFails() {
        when(mockDataManager.getPersons())
                .thenReturn(Observable.<List<Person>>error(new RuntimeException()));

        mainPresenter.loadPersons();
        verify(mockMainView).showError();
        verify(mockMainView, never()).showPersonsEmpty();
        verify(mockMainView, never()).showPersons(anyListOf(Person.class));
    }

}