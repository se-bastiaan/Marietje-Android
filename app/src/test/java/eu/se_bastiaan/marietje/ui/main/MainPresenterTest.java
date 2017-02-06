package eu.se_bastiaan.marietje.ui.main;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import eu.se_bastiaan.marietje.data.DataManager;
import eu.se_bastiaan.marietje.events.NeedsSessionCookie;
import eu.se_bastiaan.marietje.util.EventBus;
import eu.se_bastiaan.marietje.util.RxSchedulersOverrideRule;

import static org.mockito.Mockito.verify;

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

}
