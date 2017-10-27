package eu.se_bastiaan.marietje.ui.main.queue

import android.content.Context
import android.test.mock.MockContext
import eu.se_bastiaan.marietje.data.ControlDataManager
import eu.se_bastiaan.marietje.data.DataManager
import eu.se_bastiaan.marietje.data.model.Empty
import eu.se_bastiaan.marietje.data.model.Queue
import eu.se_bastiaan.marietje.test.common.TestDataFactory
import eu.se_bastiaan.marietje.util.EventBus
import eu.se_bastiaan.marietje.util.RxSchedulersOverrideRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import rx.Observable

@RunWith(MockitoJUnitRunner::class)
class QueuePresenterTest {

    var context: Context = MockContext()
    @Mock
    lateinit var eventBus: EventBus
    @Mock
    lateinit var mockQueueView: QueueView
    @Mock
    lateinit var mockDataManager: DataManager
    lateinit var queuePresenter: QueuePresenter

    @get:Rule
    val overrideSchedulersRule = RxSchedulersOverrideRule()

    @Before
    fun setUp() {
        `when`(mockDataManager.controlDataManager()).thenReturn(mock(ControlDataManager::class.java))

        queuePresenter = QueuePresenter(mockDataManager, context, eventBus)
        queuePresenter.attachView(mockQueueView)

    }

    @After
    fun tearDown() {
        queuePresenter.detachView(mockQueueView)
    }

    @Test
    fun loadDataReturnsQueue() {
        val queue = TestDataFactory.makeQueueResponse()
        `when`(mockDataManager.controlDataManager().queue())
                .thenReturn(Observable.just(queue))

        queuePresenter.loadData()
        verify<QueueView>(mockQueueView).showLoading()
        verify<QueueView>(mockQueueView).showQueue(queue)
        verify<QueueView>(mockQueueView, never()).showLoadingError()
    }

    @Test
    fun loadDataFails() {
        `when`(mockDataManager.controlDataManager().queue())
                .thenReturn(Observable.error(RuntimeException()))

        queuePresenter.loadData()
        verify<QueueView>(mockQueueView).showLoading()
        verify<QueueView>(mockQueueView).showLoadingError()
        verify<QueueView>(mockQueueView, never()).showQueue(any(Queue::class.java))
    }

    @Test
    fun removeSongFromQueueShowsSuccess() {
        val response = Empty()
        `when`(mockDataManager.controlDataManager().cancel(0))
                .thenReturn(Observable.just(response))

        queuePresenter.removeSongFromQueue(0)
        verify<QueueView>(mockQueueView).showRemoveSuccess()
        verify<QueueView>(mockQueueView, never()).showRemoveError()
    }

    @Test
    fun removeSongFromQueueShowsError() {
        `when`(mockDataManager.controlDataManager().cancel(0))
                .thenReturn(Observable.error(RuntimeException()))

        queuePresenter.removeSongFromQueue(0)
        verify<QueueView>(mockQueueView).showRemoveError()
        verify<QueueView>(mockQueueView, never()).showRemoveSuccess()
    }

    @Test
    fun moveSongDownInQueueShowsSuccess() {
        val response = Empty()
        `when`(mockDataManager.controlDataManager().moveDown(0))
                .thenReturn(Observable.just(response))

        queuePresenter.moveSongDownInQueue(0)
        verify<QueueView>(mockQueueView).showMoveDownSuccess()
        verify<QueueView>(mockQueueView, never()).showMoveDownError()
    }

    @Test
    fun moveSongDownInQueueShowsError() {
        `when`(mockDataManager.controlDataManager().moveDown(0))
                .thenReturn(Observable.error(RuntimeException()))

        queuePresenter.moveSongDownInQueue(0)
        verify<QueueView>(mockQueueView).showMoveDownError()
        verify<QueueView>(mockQueueView, never()).showMoveDownSuccess()
    }

    @Test
    fun moveSongUpInQueueShowsSuccess() {
        val response = Empty()
        `when`(mockDataManager.controlDataManager().moveUp(0))
                .thenReturn(Observable.just(response))

        queuePresenter.moveSongUpInQueue(0)
        verify<QueueView>(mockQueueView).showMoveUpSuccess()
        verify<QueueView>(mockQueueView, never()).showMoveUpError()
    }

    @Test
    fun moveSongUpInQueueShowsError() {
        `when`(mockDataManager.controlDataManager().moveUp(0))
                .thenReturn(Observable.error(RuntimeException()))

        queuePresenter.moveSongUpInQueue(0)
        verify<QueueView>(mockQueueView).showMoveUpError()
        verify<QueueView>(mockQueueView, never()).showMoveUpSuccess()
    }

    @Test
    fun skipCurrentSongShowsSuccess() {
        val response = Empty()
        `when`(mockDataManager.controlDataManager().skip())
                .thenReturn(Observable.just(response))

        queuePresenter.skipCurrentSong()
        verify<QueueView>(mockQueueView).showSkipSuccess()
        verify<QueueView>(mockQueueView, never()).showSkipError()
    }

    @Test
    fun skipCurrentSongShowsError() {
        `when`(mockDataManager.controlDataManager().skip())
                .thenReturn(Observable.error(RuntimeException()))

        queuePresenter.skipCurrentSong()
        verify<QueueView>(mockQueueView).showSkipError()
        verify<QueueView>(mockQueueView, never()).showSkipSuccess()
    }

}
