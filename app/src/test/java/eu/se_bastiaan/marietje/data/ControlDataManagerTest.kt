package eu.se_bastiaan.marietje.data

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.invocation.InvocationOnMock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.stubbing.Answer

import java.util.Collections

import eu.se_bastiaan.marietje.data.local.PreferencesHelper
import eu.se_bastiaan.marietje.data.model.Empty
import eu.se_bastiaan.marietje.data.model.Permissions
import eu.se_bastiaan.marietje.data.model.Queue
import eu.se_bastiaan.marietje.data.remote.ControlService
import eu.se_bastiaan.marietje.test.common.TestDataFactory
import rx.Observable
import rx.observers.TestSubscriber

import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@RunWith(MockitoJUnitRunner::class)
class ControlDataManagerTest {

    @Mock
    internal var mockControlService: ControlService? = null
    @Mock
    internal var mockPreferencesHelper: PreferencesHelper? = null

    private var dataManager: ControlDataManager? = null

    @Before
    fun setUp() {
        dataManager = ControlDataManager(mockControlService, mockPreferencesHelper)
    }

    @Test
    fun csrfEmitsValues() {
        val csrfToken = "csrf_token"
        `when`(mockPreferencesHelper!!.csrfToken)
                .thenReturn(csrfToken)

        val response = TestDataFactory.makeNormalCsrfResponse()
        `when`(mockControlService!!.csrf())
                .thenReturn(Observable.just(response))

        val result = TestSubscriber<String>()
        dataManager!!.csrf().subscribe(result)
        result.assertNoErrors()

        result.assertReceivedOnNext(listOf(csrfToken))
    }

    @Test
    fun queueEmitsValues() {
        val response = TestDataFactory.makeQueueResponse()
        `when`(mockControlService!!.queue())
                .thenReturn(Observable.just(response))

        val result = TestSubscriber<Queue>()
        dataManager!!.queue().subscribe(result)
        result.assertNoErrors()

        result.assertReceivedOnNext(listOf(response))
    }

    @Test
    fun skipEmitsValues() {
        val response = Empty()
        `when`(mockControlService!!.skip())
                .thenReturn(Observable.just(response))

        val result = TestSubscriber<Empty>()
        dataManager!!.skip().subscribe(result)
        result.assertNoErrors()

        result.assertReceivedOnNext(listOf(response))
    }

    @Test
    fun moveUpEmitsValues() {
        val response = Empty()
        `when`(mockControlService!!.moveUp(0))
                .thenReturn(Observable.just(response))

        val result = TestSubscriber<Empty>()
        dataManager!!.moveUp(0).subscribe(result)
        result.assertNoErrors()

        result.assertReceivedOnNext(listOf(response))
    }

    @Test
    fun moveDownEmitsValues() {
        val response = Empty()
        `when`(mockControlService!!.moveDown(0))
                .thenReturn(Observable.just(response))

        val result = TestSubscriber<Empty>()
        dataManager!!.moveDown(0).subscribe(result)
        result.assertNoErrors()

        result.assertReceivedOnNext(listOf(response))
    }

    @Test
    fun cancelEmitsValues() {
        val response = Empty()
        `when`(mockControlService!!.cancel(0))
                .thenReturn(Observable.just(response))

        val result = TestSubscriber<Empty>()
        dataManager!!.cancel(0).subscribe(result)
        result.assertNoErrors()

        result.assertReceivedOnNext(listOf(response))
    }

    @Test
    fun requestEmitsValues() {
        val response = Empty()
        `when`(mockControlService!!.request(0))
                .thenReturn(Observable.just(response))

        val result = TestSubscriber<Empty>()
        dataManager!!.request(0).subscribe(result)
        result.assertNoErrors()

        result.assertReceivedOnNext(listOf(response))
    }

    @Test
    fun volumeDownEmitsValues() {
        val response = Empty()
        `when`(mockControlService!!.volumeDown())
                .thenReturn(Observable.just(response))

        val result = TestSubscriber<Empty>()
        dataManager!!.volumeDown().subscribe(result)
        result.assertNoErrors()

        result.assertReceivedOnNext(listOf(response))
    }

    @Test
    fun volumeUpEmitsValues() {
        val response = Empty()
        `when`(mockControlService!!.volumeUp())
                .thenReturn(Observable.just(response))

        val result = TestSubscriber<Empty>()
        dataManager!!.volumeUp().subscribe(result)
        result.assertNoErrors()

        result.assertReceivedOnNext(listOf(response))
    }

    @Test
    fun permissionsEmitsValues() {
        val response = TestDataFactory.makePermissionsResponse()
        `when`(mockControlService!!.permissions())
                .thenReturn(Observable.just(response))

        val result = TestSubscriber<Permissions>()
        dataManager!!.permissions().subscribe(result)
        result.assertNoErrors()

        result.assertReceivedOnNext(listOf(response))
    }

    @Test
    fun csrfCallsApi() {
        val response = TestDataFactory.makeNormalCsrfResponse()
        `when`(mockControlService!!.csrf())
                .thenReturn(Observable.just(response))

        dataManager!!.csrf().subscribe()
        verify<ControlService>(mockControlService).csrf()
    }

    @Test
    fun queueCallsApi() {
        val response = TestDataFactory.makeQueueResponse()
        `when`(mockControlService!!.queue())
                .thenReturn(Observable.just(response))

        dataManager!!.queue().subscribe()
        verify<ControlService>(mockControlService).queue()
    }

    @Test
    fun skipCallsApi() {
        val response = Empty()
        `when`(mockControlService!!.skip())
                .thenReturn(Observable.just(response))

        dataManager!!.skip().subscribe()
        verify<ControlService>(mockControlService).skip()
    }

    @Test
    fun moveDownCallsApi() {
        val response = Empty()
        `when`(mockControlService!!.moveDown(0))
                .thenReturn(Observable.just(response))

        dataManager!!.moveDown(0).subscribe()
        verify<ControlService>(mockControlService).moveDown(0)
    }

    @Test
    fun moveUpCallsApi() {
        val response = Empty()
        `when`(mockControlService!!.moveUp(0))
                .thenReturn(Observable.just(response))

        dataManager!!.moveUp(0).subscribe()
        verify<ControlService>(mockControlService).moveUp(0)
    }

    @Test
    fun cancelCallsApi() {
        val response = Empty()
        `when`(mockControlService!!.cancel(0))
                .thenReturn(Observable.just(response))

        dataManager!!.cancel(0).subscribe()
        verify<ControlService>(mockControlService).cancel(0)
    }

    @Test
    fun requestCallsApi() {
        val response = Empty()
        `when`(mockControlService!!.request(0))
                .thenReturn(Observable.just(response))

        dataManager!!.request(0).subscribe()
        verify<ControlService>(mockControlService).request(0)
    }

    @Test
    fun volumeUpCallsApi() {
        val response = Empty()
        `when`(mockControlService!!.volumeUp())
                .thenReturn(Observable.just(response))

        dataManager!!.volumeUp().subscribe()
        verify<ControlService>(mockControlService).volumeUp()
    }

    @Test
    fun volumeDownCallsApi() {
        val response = Empty()
        `when`(mockControlService!!.volumeDown())
                .thenReturn(Observable.just(response))

        dataManager!!.volumeDown().subscribe()
        verify<ControlService>(mockControlService).volumeDown()
    }

    @Test
    fun permissionsCallsApi() {
        val response = TestDataFactory.makePermissionsResponse()
        `when`(mockControlService!!.permissions())
                .thenReturn(Observable.just(response))

        dataManager!!.permissions().subscribe()
        verify<ControlService>(mockControlService).permissions()
    }

    @Test
    fun csrfCallsPreferencesHelper() {
        `when`(mockPreferencesHelper!!.csrfToken)
                .thenReturn("csrf_token")
        `when`(mockControlService!!.csrf())
                .thenReturn(Observable.just(TestDataFactory.makeNormalCsrfResponse()))

        dataManager!!.csrf().subscribe()

        `when`(mockPreferencesHelper!!.csrfToken)
                .thenReturn("csrf_token")
        `when`(mockControlService!!.csrf())
                .then {
                    `when`(mockPreferencesHelper!!.csrfToken)
                            .thenReturn("")
                    Observable.just(TestDataFactory.makeNormalCsrfResponse())
                }

        dataManager!!.csrf().subscribe()

        verify<PreferencesHelper>(mockPreferencesHelper, times(2)).setCsrftoken("")
        verify<PreferencesHelper>(mockPreferencesHelper, times(4)).csrfToken
        verify<PreferencesHelper>(mockPreferencesHelper).setCsrftoken("csrf_token")

        verify<ControlService>(mockControlService, times(2)).csrf()
    }
}
