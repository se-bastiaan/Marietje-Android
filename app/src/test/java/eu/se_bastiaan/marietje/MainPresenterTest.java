package eu.se_bastiaan.marietje;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import eu.se_bastiaan.marietje.data.DataManager;
import eu.se_bastiaan.marietje.data.SongsDataManager;
import eu.se_bastiaan.marietje.data.local.PreferencesHelper;
import eu.se_bastiaan.marietje.data.model.Song;
import eu.se_bastiaan.marietje.data.model.SongsResponse;
import eu.se_bastiaan.marietje.test.common.TestDataFactory;
import eu.se_bastiaan.marietje.ui.main.MainPresenter;
import eu.se_bastiaan.marietje.ui.main.MainView;
import eu.se_bastiaan.marietje.util.RxSchedulersOverrideRule;
import rx.Observable;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {

    @Mock
    MainView mockMainView;
    @Mock
    DataManager mockDataManager;
    private MainPresenter mainPresenter;

    @Rule
    public final RxSchedulersOverrideRule overrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp() {
        when(mockDataManager.songsDataManager()).thenReturn(mock(SongsDataManager.class));
        when(mockDataManager.preferencesHelper()).thenReturn(mock(PreferencesHelper.class));

        mainPresenter = new MainPresenter(mockDataManager);
        mainPresenter.attachView(mockMainView);
    }

    @After
    public void tearDown() {
        mainPresenter.detachView(mockMainView);
    }

    @Test
    public void loadSongsReturnsSongs() {
        SongsResponse response = TestDataFactory.makeSongsResponse(0);
        when(mockDataManager.songsDataManager().songs(0, null, null))
                .thenReturn(Observable.just(response));

        mainPresenter.loadSongs();
        verify(mockMainView).showSongs(response.data());
        verify(mockMainView, never()).showSongsEmpty();
        verify(mockMainView, never()).showError();
    }

    @Test
    public void loadSongsReturnsEmptyList() {
        SongsResponse response = TestDataFactory.makeEmptySongsResponse(0);

        when(mockDataManager.songsDataManager().songs(0, null, null))
                .thenReturn(Observable.just(response));

        mainPresenter.loadSongs();
        verify(mockMainView).showSongsEmpty();
        verify(mockMainView, never()).showSongs(anyListOf(Song.class));
        verify(mockMainView, never()).showError();
    }

    @Test
    public void loadSongsFails() {
        when(mockDataManager.songsDataManager().songs(0, null, null))
                .thenReturn(Observable.error(new RuntimeException()));

        mainPresenter.loadSongs();
        verify(mockMainView).showError();
        verify(mockMainView, never()).showSongsEmpty();
        verify(mockMainView, never()).showSongs(anyListOf(Song.class));
    }

}