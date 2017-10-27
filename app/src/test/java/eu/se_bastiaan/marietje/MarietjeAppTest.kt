package eu.se_bastiaan.marietje

import eu.se_bastiaan.marietje.data.ControlDataManager
import eu.se_bastiaan.marietje.data.DataManager
import eu.se_bastiaan.marietje.events.NeedsCsrfToken
import eu.se_bastiaan.marietje.injection.component.AppComponent
import eu.se_bastiaan.marietje.ui.main.MainView
import eu.se_bastiaan.marietje.util.EventBus
import eu.se_bastiaan.marietje.util.RxSchedulersOverrideRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MarietjeAppTest {

    @Mock
    internal var mockMainView: MainView? = null
    @Mock
    internal var mockDataManager: DataManager? = null
    @Mock
    internal var appComponent: AppComponent? = null

    private var eventBus: EventBus? = null
    private var marietjeApp: MarietjeApp? = null

    @get:Rule
    val overrideSchedulersRule = RxSchedulersOverrideRule()

    @Before
    fun setUp() {
        eventBus = EventBus()

        `when`(appComponent!!.eventBus()).thenReturn(eventBus)
        `when`(appComponent!!.dataManager()).thenReturn(mockDataManager)
        `when`(mockDataManager!!.controlDataManager()).thenReturn(mock(ControlDataManager::class.java))

        marietjeApp = MarietjeApp()
        marietjeApp!!.setAppComponent(appComponent)
        marietjeApp!!.init()
    }

    @Test
    fun needsCsrfCookieCallsNetwork() {
        eventBus!!.post(NeedsCsrfToken())

        verify(appComponent!!.dataManager().controlDataManager()).csrf()
    }

}
