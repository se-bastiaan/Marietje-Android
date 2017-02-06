package eu.se_bastiaan.marietje.ui.main.queue;

import android.os.Handler;
import android.os.Looper;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import eu.se_bastiaan.marietje.test.common.TestDataFactory;
import eu.se_bastiaan.marietje.util.RxSchedulersOverrideRule;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class PlaylistAdapterTest {

    @Rule
    public final RxSchedulersOverrideRule overrideSchedulersRule = new RxSchedulersOverrideRule();

    @Test
    public void testGetCount() {
        Handler handler = new Handler(mock(Looper.class));
        PlaylistAdapter songsAdapter = new PlaylistAdapter(handler);
        songsAdapter.setQueue(TestDataFactory.makeQueueResponse());
        assertThat(songsAdapter.getItemCount(), equalTo(11));
    }



}