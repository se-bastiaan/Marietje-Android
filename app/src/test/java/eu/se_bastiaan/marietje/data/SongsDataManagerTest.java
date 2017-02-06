package eu.se_bastiaan.marietje.data;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import eu.se_bastiaan.marietje.data.model.Songs;
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
        Songs response = stubSongsResponse(currentPage);

        TestSubscriber<Songs> result = new TestSubscriber<>();
        dataManager.songs(currentPage, null).subscribe(result);
        result.assertNoErrors();

        result.assertReceivedOnNext(Collections.singletonList(response));
    }

    @Test
    public void songsCallsApi() {
        int currentPage = 4;
        stubSongsResponse(currentPage);
        dataManager.songs(currentPage, null).subscribe();
        verify(mockSongsService).songs(currentPage, 50, null, null);
    }

    @Test
    public void manageSongsEmitsValues() {
        int currentPage = 1;
        Songs response = stubSongsResponse(currentPage);

        TestSubscriber<Songs> result = new TestSubscriber<>();
        dataManager.manageSongs(currentPage, null, null).subscribe(result);
        result.assertNoErrors();

        result.assertReceivedOnNext(Collections.singletonList(response));
    }

    @Test
    public void manageSongsCallsApi() {
        int currentPage = 2;
        stubSongsResponse(currentPage);
        dataManager.manageSongs(currentPage, null, null).subscribe();
        verify(mockSongsService).manageSongs(currentPage, 50, null, null);
    }

    private Songs stubSongsResponse(int currentPage) {
        Songs response = TestDataFactory.makeSongsResponse(currentPage);
        when(mockSongsService.songs(currentPage, 50, null, null))
                .thenReturn(Observable.just(response));
        when(mockSongsService.manageSongs(currentPage, 50, null, null))
                .thenReturn(Observable.just(response));
        return response;
    }

}
