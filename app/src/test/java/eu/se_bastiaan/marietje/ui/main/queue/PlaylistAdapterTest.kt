package eu.se_bastiaan.marietje.ui.main.queue

import android.os.Handler
import android.os.Looper
import eu.se_bastiaan.marietje.test.common.TestDataFactory
import eu.se_bastiaan.marietje.util.RxSchedulersOverrideRule
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PlaylistAdapterTest {

    @get:Rule
    val overrideSchedulersRule = RxSchedulersOverrideRule()

    @Test
    fun testGetCount() {
        val handler = Handler(mock(Looper::class.java))
        val songsAdapter = PlaylistAdapter(handler)
        songsAdapter.setQueue(TestDataFactory.makeQueueResponse())
        assertThat(songsAdapter.itemCount, equalTo(11))
    }

}