package eu.se_bastiaan.marietje.ui.main.request;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import eu.se_bastiaan.marietje.test.common.TestDataFactory;
import eu.se_bastiaan.marietje.util.RxSchedulersOverrideRule;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(MockitoJUnitRunner.class)
public class SongsAdapterTest {

    @Rule
    public final RxSchedulersOverrideRule overrideSchedulersRule = new RxSchedulersOverrideRule();

    @Test
    public void testGetCount() {
        SongsAdapter songsAdapter = new SongsAdapter();
        songsAdapter.setSongs(TestDataFactory.makeListSongs(5));
        assertThat(songsAdapter.getItemCount(), equalTo(5));
        songsAdapter.addSongs(TestDataFactory.makeListSongs(5));
        assertThat(songsAdapter.getItemCount(), equalTo(10));
        songsAdapter.setSongs(TestDataFactory.makeListSongs(5));
        assertThat(songsAdapter.getItemCount(), equalTo(5));
    }

}
