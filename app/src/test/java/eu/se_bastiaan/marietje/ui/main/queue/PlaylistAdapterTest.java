package eu.se_bastiaan.marietje.ui.main.queue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import eu.se_bastiaan.marietje.test.common.TestDataFactory;
import eu.se_bastiaan.marietje.util.RxSchedulersOverrideRule;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(MockitoJUnitRunner.class)
public class PlaylistAdapterTest {

    @Rule
    public final RxSchedulersOverrideRule overrideSchedulersRule = new RxSchedulersOverrideRule();

    @Test
    public void testGetCount() {
        PlaylistAdapter songsAdapter = new PlaylistAdapter();
        songsAdapter.setQueue(TestDataFactory.makeQueueResponse());
        assertThat(songsAdapter.getItemCount(), equalTo(11));
    }

}