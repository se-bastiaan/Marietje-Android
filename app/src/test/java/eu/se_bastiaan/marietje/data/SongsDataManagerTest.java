package eu.se_bastiaan.marietje.data;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import eu.se_bastiaan.marietje.data.SongsDataManager;
import eu.se_bastiaan.marietje.data.model.SongsResponse;
import eu.se_bastiaan.marietje.data.remote.SongsService;
import eu.se_bastiaan.marietje.test.common.TestDataFactory;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SongsDataManagerTest {

    @Mock
    SongsService mockSongsService;

    private SongsDataManager dataManager;

    @Before
    public void setUp() {
        dataManager = new SongsDataManager(mockSongsService);
    }

    @Test
    public void songsEmitsValues() {
        int currentPage = 0;
        SongsResponse response = stubSongsResponse(currentPage);

        TestSubscriber<SongsResponse> result = new TestSubscriber<>();
        dataManager.songs(currentPage, null, null).subscribe(result);
        result.assertNoErrors();

        result.assertReceivedOnNext(Collections.singletonList(response));
    }

    @Test
    public void songsCallsApi() {
        int currentPage = 4;
        stubSongsResponse(currentPage);
        dataManager.songs(currentPage, null, null).subscribe();
        verify(mockSongsService).songs(currentPage, 100, null, null);
    }

    @Test
    public void manageSongsEmitsValues() {
        int currentPage = 1;
        SongsResponse response = stubSongsResponse(currentPage);

        TestSubscriber<SongsResponse> result = new TestSubscriber<>();
        dataManager.manageSongs(currentPage, null, null).subscribe(result);
        result.assertNoErrors();

        result.assertReceivedOnNext(Collections.singletonList(response));
    }

    @Test
    public void manageSongsCallsApi() {
        int currentPage = 2;
        stubSongsResponse(currentPage);
        dataManager.manageSongs(currentPage, null, null).subscribe();
        verify(mockSongsService).manageSongs(currentPage, 100, null, null);
    }

    private SongsResponse stubSongsResponse(int currentPage) {
        SongsResponse response = TestDataFactory.makeSongsResponse(currentPage);
        when(mockSongsService.songs(currentPage, 100, null, null))
                .thenReturn(Observable.just(response));
        when(mockSongsService.manageSongs(currentPage, 100, null, null))
                .thenReturn(Observable.just(response));
        return response;
    }

}
