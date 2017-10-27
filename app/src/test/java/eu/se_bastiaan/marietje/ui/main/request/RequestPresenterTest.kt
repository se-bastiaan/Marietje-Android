package eu.se_bastiaan.marietje.ui.main.request

import android.content.Context
import android.test.mock.MockContext

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

import eu.se_bastiaan.marietje.data.ControlDataManager
import eu.se_bastiaan.marietje.data.DataManager
import eu.se_bastiaan.marietje.data.SongsDataManager
import eu.se_bastiaan.marietje.data.model.Empty
import eu.se_bastiaan.marietje.data.model.Songs
import eu.se_bastiaan.marietje.test.common.TestDataFactory
import eu.se_bastiaan.marietje.util.EventBus
import eu.se_bastiaan.marietje.util.RxSchedulersOverrideRule
import rx.Observable

import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@RunWith(MockitoJUnitRunner::class)
class RequestPresenterTest {

    internal var context: Context = MockContext()
    @Mock
    internal var eventBus: EventBus? = null
    @Mock
    internal var mockRequestView: RequestView? = null
    @Mock
    internal var mockDataManager: DataManager? = null
    private var requestPresenter: RequestPresenter? = null

    @Rule
    val overrideSchedulersRule = RxSchedulersOverrideRule()

    @Before
    fun setUp() {
        `when`(mockDataManager!!.songsDataManager()).thenReturn(mock(SongsDataManager::class.java))
        `when`(mockDataManager!!.controlDataManager()).thenReturn(mock(ControlDataManager::class.java))

        requestPresenter = RequestPresenter(mockDataManager, context, eventBus)
        requestPresenter!!.attachView(mockRequestView!!)
    }

    @After
    fun tearDown() {
        requestPresenter!!.detachView(mockRequestView!!)
    }

    @Test
    fun searchSongsReturnsSongs() {
        val response = TestDataFactory.makeSongsResponse(1)
        val query = "returnsSongs"
        `when`(mockDataManager!!.songsDataManager().songs(1, query))
                .thenReturn(Observable.just(response))

        requestPresenter!!.searchSong(query)
        verify<RequestView>(mockRequestView).showLoading()
        verify<RequestView>(mockRequestView).showSongs(eq<List<Song>>(response.data), anyBoolean(), anyBoolean())
        verify<RequestView>(mockRequestView, never()).showSongsEmpty()
        verify<RequestView>(mockRequestView, never()).showLoadingError()
    }

    @Test
    fun searchSongsReturnsEmptyList() {
        val response = TestDataFactory.makeEmptySongsResponse(1)
        val query = "returnsEmpty"
        `when`(mockDataManager!!.songsDataManager().songs(1, query))
                .thenReturn(Observable.just(response))

        requestPresenter!!.searchSong(query)
        verify<RequestView>(mockRequestView).showLoading()
        verify<RequestView>(mockRequestView).showSongsEmpty()
        verify<RequestView>(mockRequestView, never()).showSongs(anyList<Song>(), anyBoolean(), anyBoolean())
        verify<RequestView>(mockRequestView, never()).showLoadingError()
    }

    @Test
    fun searchSongsFails() {
        val query = "error"
        `when`(mockDataManager!!.songsDataManager().songs(1, query))
                .thenReturn(Observable.error(RuntimeException()))

        requestPresenter!!.searchSong(query)
        verify<RequestView>(mockRequestView).showLoadingError()
        verify<RequestView>(mockRequestView, never()).showSongsEmpty()
        verify<RequestView>(mockRequestView, never()).showSongs(anyList<Song>(), anyBoolean(), anyBoolean())
    }

    @Test
    fun searchSongsDoesNothing() {
        requestPresenter!!.searchSong("te")
        verify<RequestView>(mockRequestView, never()).showLoading()
        verify<RequestView>(mockRequestView, never()).showSongs(anyList<Song>(), anyBoolean(), anyBoolean())
        verify<RequestView>(mockRequestView, never()).showSongsEmpty()
        verify<RequestView>(mockRequestView, never()).showLoadingError()
    }

    @Test
    fun searchSongsReturnsPages() {
        val query = "returnsSongs"
        for (i in 1..100) {
            val response = TestDataFactory.makeSongsResponse(i.toLong())
            `when`(mockDataManager!!.songsDataManager().songs(i.toLong(), query))
                    .thenReturn(Observable.just(response))
            requestPresenter!!.searchSong(query)
        }

        verify<RequestView>(mockRequestView).showLoading()
        verify<RequestView>(mockRequestView).showSongs(anyList<Song>(), eq(true), eq(true))
        verify<RequestView>(mockRequestView, times(98)).showSongs(anyList<Song>(), eq(false), eq(true))
        verify<RequestView>(mockRequestView).showSongs(anyList<Song>(), eq(false), eq(false))
        verify<RequestView>(mockRequestView, never()).showSongsEmpty()
        verify<RequestView>(mockRequestView, never()).showLoadingError()
    }

    @Test
    fun requestSongShowsSuccess() {
        val response = Empty()
        `when`(mockDataManager!!.controlDataManager().request(0))
                .thenReturn(Observable.just(response))

        requestPresenter!!.requestSong(0)
        verify<RequestView>(mockRequestView).showRequestSuccess()
        verify<RequestView>(mockRequestView, never()).showRequestError()
    }

    @Test
    fun requestSongShowsError() {
        `when`(mockDataManager!!.controlDataManager().request(0))
                .thenReturn(Observable.error(RuntimeException()))

        requestPresenter!!.requestSong(0)
        verify<RequestView>(mockRequestView).showRequestError()
        verify<RequestView>(mockRequestView, never()).showRequestSuccess()
    }

}