package eu.se_bastiaan.marietje.ui.main.request;

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
import eu.se_bastiaan.marietje.data.SongsDataManager;
import eu.se_bastiaan.marietje.data.model.Empty;
import eu.se_bastiaan.marietje.data.model.Song;
import eu.se_bastiaan.marietje.data.model.Songs;
import eu.se_bastiaan.marietje.test.common.TestDataFactory;
import eu.se_bastiaan.marietje.util.EventBus;
import eu.se_bastiaan.marietje.util.RxSchedulersOverrideRule;
import rx.Observable;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyListOf;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RequestPresenterTest {

    Context context = new MockContext();
    @Mock
    EventBus eventBus;
    @Mock
    RequestView mockRequestView;
    @Mock
    DataManager mockDataManager;
    private RequestPresenter requestPresenter;

    @Rule
    public final RxSchedulersOverrideRule overrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp() {
        when(mockDataManager.songsDataManager()).thenReturn(mock(SongsDataManager.class));
        when(mockDataManager.controlDataManager()).thenReturn(mock(ControlDataManager.class));

        requestPresenter = new RequestPresenter(mockDataManager, context, eventBus);
        requestPresenter.attachView(mockRequestView);
    }

    @After
    public void tearDown() {
        requestPresenter.detachView(mockRequestView);
    }

    @Test
    public void searchSongsReturnsSongs() {
        Songs response = TestDataFactory.makeSongsResponse(1);
        String query = "returnsSongs";
        when(mockDataManager.songsDataManager().songs(1, query))
                .thenReturn(Observable.just(response));

        requestPresenter.searchSong(query);
        verify(mockRequestView).showLoading();
        verify(mockRequestView).showSongs(eq(response.data()), anyBoolean(), anyBoolean());
        verify(mockRequestView, never()).showSongsEmpty();
        verify(mockRequestView, never()).showLoadingError();
    }

    @Test
    public void searchSongsReturnsEmptyList() {
        Songs response = TestDataFactory.makeEmptySongsResponse(1);
        String query = "returnsEmpty";
        when(mockDataManager.songsDataManager().songs(1, query))
                .thenReturn(Observable.just(response));

        requestPresenter.searchSong(query);
        verify(mockRequestView).showLoading();
        verify(mockRequestView).showSongsEmpty();
        verify(mockRequestView, never()).showSongs(anyListOf(Song.class), anyBoolean(), anyBoolean());
        verify(mockRequestView, never()).showLoadingError();
    }

    @Test
    public void searchSongsFails() {
        String query = "error";
        when(mockDataManager.songsDataManager().songs(1, query))
                .thenReturn(Observable.error(new RuntimeException()));

        requestPresenter.searchSong(query);
        verify(mockRequestView).showLoadingError();
        verify(mockRequestView, never()).showSongsEmpty();
        verify(mockRequestView, never()).showSongs(anyListOf(Song.class), anyBoolean(), anyBoolean());
    }

    @Test
    public void searchSongsDoesNothing() {
        requestPresenter.searchSong("te");
        verify(mockRequestView, never()).showLoading();
        verify(mockRequestView, never()).showSongs(anyListOf(Song.class), anyBoolean(), anyBoolean());
        verify(mockRequestView, never()).showSongsEmpty();
        verify(mockRequestView, never()).showLoadingError();
    }

    @Test
    public void searchSongsReturnsPages() {
        String query = "returnsSongs";
        for (int i = 1; i <= 100; i++) {
            Songs response = TestDataFactory.makeSongsResponse(i);
            when(mockDataManager.songsDataManager().songs(i, query))
                    .thenReturn(Observable.just(response));
            requestPresenter.searchSong(query);
        }

        verify(mockRequestView).showLoading();
        verify(mockRequestView).showSongs(anyListOf(Song.class), eq(true), eq(true));
        verify(mockRequestView, times(98)).showSongs(anyListOf(Song.class), eq(false), eq(true));
        verify(mockRequestView).showSongs(anyListOf(Song.class), eq(false), eq(false));
        verify(mockRequestView, never()).showSongsEmpty();
        verify(mockRequestView, never()).showLoadingError();
    }

    @Test
    public void requestSongShowsSuccess() {
        Empty response = new Empty();
        when(mockDataManager.controlDataManager().request(0))
                .thenReturn(Observable.just(response));

        requestPresenter.requestSong(0);
        verify(mockRequestView).showRequestSuccess();
        verify(mockRequestView, never()).showRequestError();
    }

    @Test
    public void requestSongShowsError() {
        when(mockDataManager.controlDataManager().request(0))
                .thenReturn(Observable.error(new RuntimeException()));

        requestPresenter.requestSong(0);
        verify(mockRequestView).showRequestError();
        verify(mockRequestView, never()).showRequestSuccess();
    }

}