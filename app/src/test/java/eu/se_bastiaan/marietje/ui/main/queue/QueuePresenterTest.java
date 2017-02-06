package eu.se_bastiaan.marietje.ui.main.queue;

import android.content.Context;
import android.test.mock.MockContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import eu.se_bastiaan.marietje.data.ControlDataManager;
import eu.se_bastiaan.marietje.data.DataManager;
import eu.se_bastiaan.marietje.data.model.Queue;
import eu.se_bastiaan.marietje.test.common.TestDataFactory;
import eu.se_bastiaan.marietje.util.EventBus;
import eu.se_bastiaan.marietje.util.RxSchedulersOverrideRule;
import rx.Observable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QueuePresenterTest {

    Context context = new MockContext();
    @Mock
    EventBus eventBus;
    @Mock
    QueueView mockQueueView;
    @Mock
    DataManager mockDataManager;
    private QueuePresenter queuePresenter;

    @Rule
    public final RxSchedulersOverrideRule overrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp() {
        when(mockDataManager.controlDataManager()).thenReturn(mock(ControlDataManager.class));

        queuePresenter = new QueuePresenter(mockDataManager, context, eventBus);
        queuePresenter.attachView(mockQueueView);

    }

    @After
    public void tearDown() {
        queuePresenter.detachView(mockQueueView);
    }

    @Test
    public void loadDataReturnsQueue() {
        Queue queue = TestDataFactory.makeQueueResponse();
        when(mockDataManager.controlDataManager().queue())
                .thenReturn(Observable.just(queue));

        queuePresenter.loadData();
        verify(mockQueueView).showLoading();
        verify(mockQueueView).showQueue(queue);
        verify(mockQueueView, never()).showLoadingError();
    }

    @Test
    public void loadDataFails() {
        when(mockDataManager.controlDataManager().queue())
                .thenReturn(Observable.error(new RuntimeException()));

        queuePresenter.loadData();
        verify(mockQueueView).showLoading();
        verify(mockQueueView).showLoadingError();
        verify(mockQueueView, never()).showQueue(any(Queue.class));
    }

}
