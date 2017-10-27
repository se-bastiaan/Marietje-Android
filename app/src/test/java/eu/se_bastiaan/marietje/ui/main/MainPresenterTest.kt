package eu.se_bastiaan.marietje.ui.main

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

import eu.se_bastiaan.marietje.data.ControlDataManager
import eu.se_bastiaan.marietje.data.DataManager
import eu.se_bastiaan.marietje.data.local.PreferencesHelper
import eu.se_bastiaan.marietje.events.NeedsSessionCookie
import eu.se_bastiaan.marietje.test.common.TestDataFactory
import eu.se_bastiaan.marietje.util.EventBus
import eu.se_bastiaan.marietje.util.RxSchedulersOverrideRule
import rx.Observable

import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@RunWith(MockitoJUnitRunner::class)
class MainPresenterTest {

    @Mock
    internal var mockMainView: MainView? = null
    @Mock
    internal var mockDataManager: DataManager? = null

    private var eventBus: EventBus? = null
    private var mainPresenter: MainPresenter? = null

    @Rule
    val overrideSchedulersRule = RxSchedulersOverrideRule()

    @Before
    fun setUp() {
        `when`(mockDataManager!!.controlDataManager()).thenReturn(mock(ControlDataManager::class.java))
        `when`(mockDataManager!!.preferencesHelper()).thenReturn(mock(PreferencesHelper::class.java))
        `when`<Observable<Permissions>>(mockDataManager!!.controlDataManager().permissions()).thenReturn(Observable.just<Permissions>(TestDataFactory.makePermissionsResponse()))
        eventBus = EventBus()
        mainPresenter = MainPresenter(mockDataManager, eventBus)
        mainPresenter!!.attachView(mockMainView!!)
    }

    @After
    fun tearDown() {
        mainPresenter!!.detachView(mockMainView!!)
    }

    @Test
    fun needsSessionCookieEventOpensLogin() {
        eventBus!!.post(NeedsSessionCookie())
        verify<MainView>(mockMainView).showLogin()
    }

    @Test
    fun testPreferencesCorrect() {
        verify(mockDataManager!!.preferencesHelper()).setCanCancel(false)
        verify(mockDataManager!!.preferencesHelper()).setCanSkip(true)
        verify(mockDataManager!!.preferencesHelper()).setCanMove(false)
        verify(mockDataManager!!.preferencesHelper()).setCanControlVolume(true)
    }

}
