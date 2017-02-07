package eu.se_bastiaan.marietje.data;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.Collections;

import eu.se_bastiaan.marietje.data.local.PreferencesHelper;
import eu.se_bastiaan.marietje.data.model.Empty;
import eu.se_bastiaan.marietje.data.model.Queue;
import eu.se_bastiaan.marietje.data.remote.ControlService;
import eu.se_bastiaan.marietje.test.common.TestDataFactory;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ControlDataManagerTest {

    @Mock
    ControlService mockControlService;
    @Mock
    PreferencesHelper mockPreferencesHelper;

    private ControlDataManager dataManager;

    @Before
    public void setUp() {
        dataManager = new ControlDataManager(mockControlService, mockPreferencesHelper);
    }

    @Test
    public void csrfEmitsValues() {
        String csrfToken = "csrf_token";
        when(mockPreferencesHelper.getCsrfToken())
                .thenReturn(csrfToken);

        String response = "<html>";
        when(mockControlService.csrf())
                .thenReturn(Observable.just(response));

        TestSubscriber<String> result = new TestSubscriber<>();
        dataManager.csrf().subscribe(result);
        result.assertNoErrors();

        result.assertReceivedOnNext(Collections.singletonList(csrfToken));
    }

    @Test
    public void queueEmitsValues() {
        Queue response = TestDataFactory.makeQueueResponse();
        when(mockControlService.queue())
                .thenReturn(Observable.just(response));

        TestSubscriber<Queue> result = new TestSubscriber<>();
        dataManager.queue().subscribe(result);
        result.assertNoErrors();

        result.assertReceivedOnNext(Collections.singletonList(response));
    }

    @Test
    public void skipEmitsValues() {
        Empty response = Empty.create();
        when(mockControlService.skip())
                .thenReturn(Observable.just(response));

        TestSubscriber<Empty> result = new TestSubscriber<>();
        dataManager.skip().subscribe(result);
        result.assertNoErrors();

        result.assertReceivedOnNext(Collections.singletonList(response));
    }

    @Test
    public void moveUpEmitsValues() {
        Empty response = Empty.create();
        when(mockControlService.moveUp(0))
                .thenReturn(Observable.just(response));

        TestSubscriber<Empty> result = new TestSubscriber<>();
        dataManager.moveUp(0).subscribe(result);
        result.assertNoErrors();

        result.assertReceivedOnNext(Collections.singletonList(response));
    }

    @Test
    public void moveDownEmitsValues() {
        Empty response = Empty.create();
        when(mockControlService.moveDown(0))
                .thenReturn(Observable.just(response));

        TestSubscriber<Empty> result = new TestSubscriber<>();
        dataManager.moveDown(0).subscribe(result);
        result.assertNoErrors();

        result.assertReceivedOnNext(Collections.singletonList(response));
    }

    @Test
    public void cancelEmitsValues() {
        Empty response = Empty.create();
        when(mockControlService.cancel(0))
                .thenReturn(Observable.just(response));

        TestSubscriber<Empty> result = new TestSubscriber<>();
        dataManager.cancel(0).subscribe(result);
        result.assertNoErrors();

        result.assertReceivedOnNext(Collections.singletonList(response));
    }

    @Test
    public void requestEmitsValues() {
        Empty response = Empty.create();
        when(mockControlService.request(0))
                .thenReturn(Observable.just(response));

        TestSubscriber<Empty> result = new TestSubscriber<>();
        dataManager.request(0).subscribe(result);
        result.assertNoErrors();

        result.assertReceivedOnNext(Collections.singletonList(response));
    }

    @Test
    public void volumeDownEmitsValues() {
        Empty response = Empty.create();
        when(mockControlService.volumeDown())
                .thenReturn(Observable.just(response));

        TestSubscriber<Empty> result = new TestSubscriber<>();
        dataManager.volumeDown().subscribe(result);
        result.assertNoErrors();

        result.assertReceivedOnNext(Collections.singletonList(response));
    }

    @Test
    public void volumeUpEmitsValues() {
        Empty response = Empty.create();
        when(mockControlService.volumeUp())
                .thenReturn(Observable.just(response));

        TestSubscriber<Empty> result = new TestSubscriber<>();
        dataManager.volumeUp().subscribe(result);
        result.assertNoErrors();

        result.assertReceivedOnNext(Collections.singletonList(response));
    }

    @Test
    public void csrfCallsApi() {
        String response = "<html>";
        when(mockControlService.csrf())
                .thenReturn(Observable.just(response));

        dataManager.csrf().subscribe();
        verify(mockControlService).csrf();
    }

    @Test
    public void queueCallsApi() {
        Queue response = TestDataFactory.makeQueueResponse();
        when(mockControlService.queue())
                .thenReturn(Observable.just(response));

        dataManager.queue().subscribe();
        verify(mockControlService).queue();
    }

    @Test
    public void skipCallsApi() {
        Empty response = Empty.create();
        when(mockControlService.skip())
                .thenReturn(Observable.just(response));

        dataManager.skip().subscribe();
        verify(mockControlService).skip();
    }

    @Test
    public void moveDownCallsApi() {
        Empty response = Empty.create();
        when(mockControlService.moveDown(0))
                .thenReturn(Observable.just(response));

        dataManager.moveDown(0).subscribe();
        verify(mockControlService).moveDown(0);
    }

    @Test
    public void moveUpCallsApi() {
        Empty response = Empty.create();
        when(mockControlService.moveUp(0))
                .thenReturn(Observable.just(response));

        dataManager.moveUp(0).subscribe();
        verify(mockControlService).moveUp(0);
    }

    @Test
    public void cancelCallsApi() {
        Empty response = Empty.create();
        when(mockControlService.cancel(0))
                .thenReturn(Observable.just(response));

        dataManager.cancel(0).subscribe();
        verify(mockControlService).cancel(0);
    }

    @Test
    public void requestCallsApi() {
        Empty response = Empty.create();
        when(mockControlService.request(0))
                .thenReturn(Observable.just(response));

        dataManager.request(0).subscribe();
        verify(mockControlService).request(0);
    }

    @Test
    public void volumeUpCallsApi() {
        Empty response = Empty.create();
        when(mockControlService.volumeUp())
                .thenReturn(Observable.just(response));

        dataManager.volumeUp().subscribe();
        verify(mockControlService).volumeUp();
    }

    @Test
    public void volumeDownCallsApi() {
        Empty response = Empty.create();
        when(mockControlService.volumeDown())
                .thenReturn(Observable.just(response));

        dataManager.volumeDown().subscribe();
        verify(mockControlService).volumeDown();
    }

    @Test
    public void csrfCallsPreferencesHelper() {
        when(mockPreferencesHelper.getCsrfToken())
                .thenReturn("csrf_token");
        when(mockControlService.csrf())
                .then(new Answer<Observable<String>>() {
                    @Override
                    public Observable<String> answer(InvocationOnMock invocation) throws Throwable {
                        when(mockPreferencesHelper.getCsrfToken())
                                .thenReturn("");
                        return Observable.error(new RuntimeException());
                    }
                });

        dataManager.csrf().subscribe();

        verify(mockPreferencesHelper).setCsrftoken("");
        verify(mockPreferencesHelper, times(4)).getCsrfToken();
        verify(mockPreferencesHelper).setCsrftoken("csrf_token");
    }
}