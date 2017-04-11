package eu.se_bastiaan.marietje.ui.main;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import eu.se_bastiaan.marietje.data.ControlDataManager;
import eu.se_bastiaan.marietje.data.DataManager;
import eu.se_bastiaan.marietje.data.local.PreferencesHelper;
import eu.se_bastiaan.marietje.data.model.Permissions;
import eu.se_bastiaan.marietje.events.NeedsSessionCookie;
import eu.se_bastiaan.marietje.test.common.TestDataFactory;
import eu.se_bastiaan.marietje.util.EventBus;
import eu.se_bastiaan.marietje.util.RxSchedulersOverrideRule;
import rx.Observable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {

    @Mock
    MainView mockMainView;
    @Mock
    DataManager mockDataManager;

    private EventBus eventBus;
    private MainPresenter mainPresenter;

    @Rule
    public final RxSchedulersOverrideRule overrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp() {
        when(mockDataManager.controlDataManager()).thenReturn(mock(ControlDataManager.class));
        when(mockDataManager.preferencesHelper()).thenReturn(mock(PreferencesHelper.class));
        when(mockDataManager.controlDataManager().permissions()).thenReturn(Observable.just(TestDataFactory.makePermissionsResponse()));
        eventBus = new EventBus();
        mainPresenter = new MainPresenter(mockDataManager, eventBus);
        mainPresenter.attachView(mockMainView);
    }

    @After
    public void tearDown() {
        mainPresenter.detachView(mockMainView);
    }

    @Test
    public void needsSessionCookieEventOpensLogin() {
        eventBus.post(new NeedsSessionCookie());
        verify(mockMainView).showLogin();
    }

    @Test
    public void testPreferencesCorrect() {
        verify(mockDataManager.preferencesHelper()).setCanCancel(false);
        verify(mockDataManager.preferencesHelper()).setCanSkip(true);
        verify(mockDataManager.preferencesHelper()).setCanMove(false);
        verify(mockDataManager.preferencesHelper()).setCanControlVolume(true);
    }

}
