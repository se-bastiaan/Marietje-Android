package eu.se_bastiaan.marietje.data

import eu.se_bastiaan.marietje.data.model.Songs
import eu.se_bastiaan.marietje.data.remote.SongsService
import eu.se_bastiaan.marietje.test.common.TestDataFactory
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import rx.Observable
import rx.observers.TestSubscriber

@RunWith(MockitoJUnitRunner::class)
class SongsDataManagerTest {

    @Mock
    lateinit var mockSongsService: SongsService

    lateinit var dataManager: SongsDataManager

    @Before
    fun setUp() {
        dataManager = SongsDataManager(mockSongsService)
    }

    @Test
    fun songsEmitsValues() {
        val currentPage = 0
        val response = stubSongsResponse(currentPage)

        val result = TestSubscriber<Songs>()
        dataManager.songs(currentPage.toLong(), null).subscribe(result)
        result.assertNoErrors()

        result.assertReceivedOnNext(listOf(response))
    }

    @Test
    fun songsCallsApi() {
        val currentPage = 4
        stubSongsResponse(currentPage)
        dataManager.songs(currentPage.toLong(), null).subscribe()
        verify<SongsService>(mockSongsService).songs(currentPage.toLong(), 50, null, null)
    }

    @Test
    fun manageSongsEmitsValues() {
        val currentPage = 1
        val response = stubSongsResponse(currentPage)

        val result = TestSubscriber<Songs>()
        dataManager.manageSongs(currentPage.toLong(), null, null).subscribe(result)
        result.assertNoErrors()

        result.assertReceivedOnNext(listOf(response))
    }

    @Test
    fun manageSongsCallsApi() {
        val currentPage = 2
        stubSongsResponse(currentPage)
        dataManager.manageSongs(currentPage.toLong(), null, null).subscribe()
        verify<SongsService>(mockSongsService).manageSongs(currentPage.toLong(), 50, null, null)
    }

    private fun stubSongsResponse(currentPage: Int): Songs {
        val response = TestDataFactory.makeSongsResponse(currentPage.toLong())
        `when`(mockSongsService.songs(currentPage.toLong(), 50, null, null))
                .thenReturn(Observable.just(response))
        `when`(mockSongsService.manageSongs(currentPage.toLong(), 50, null, null))
                .thenReturn(Observable.just(response))
        return response
    }

}
