package eu.se_bastiaan.marietje;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import eu.se_bastiaan.marietje.data.ControlDataManager;
import eu.se_bastiaan.marietje.data.DataManager;
import eu.se_bastiaan.marietje.events.NeedsCsrfToken;
import eu.se_bastiaan.marietje.injection.component.AppComponent;
import eu.se_bastiaan.marietje.ui.main.MainView;
import eu.se_bastiaan.marietje.util.EventBus;
import eu.se_bastiaan.marietje.util.RxSchedulersOverrideRule;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MarietjeAppTest {

    @Mock
    MainView mockMainView;
    @Mock
    DataManager mockDataManager;
    @Mock
    AppComponent appComponent;

    private EventBus eventBus;
    private MarietjeApp marietjeApp;

    @Rule
    public final RxSchedulersOverrideRule overrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp() {
        eventBus = new EventBus();

        when(appComponent.eventBus()).thenReturn(eventBus);
        when(appComponent.dataManager()).thenReturn(mockDataManager);
        when(mockDataManager.controlDataManager()).thenReturn(mock(ControlDataManager.class));

        marietjeApp = new MarietjeApp();
        marietjeApp.setAppComponent(appComponent);
        marietjeApp.init();
    }

    @Test
    public void needsCsrfCookieCallsNetwork() {
        eventBus.post(new NeedsCsrfToken());

        verify(appComponent.dataManager().controlDataManager()).csrf();
    }

}
