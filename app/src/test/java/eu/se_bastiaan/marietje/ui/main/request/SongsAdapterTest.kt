package eu.se_bastiaan.marietje.ui.main.request

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

import eu.se_bastiaan.marietje.test.common.TestDataFactory
import eu.se_bastiaan.marietje.util.RxSchedulersOverrideRule

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo

@RunWith(MockitoJUnitRunner::class)
class SongsAdapterTest {

    @Rule
    val overrideSchedulersRule = RxSchedulersOverrideRule()

    @Test
    fun testGetCount() {
        val songsAdapter = SongsAdapter()
        songsAdapter.setSongs(TestDataFactory.makeListSongs(5))
        assertThat(songsAdapter.itemCount, equalTo(5))
        songsAdapter.addSongs(TestDataFactory.makeListSongs(5))
        assertThat(songsAdapter.itemCount, equalTo(10))
        songsAdapter.setSongs(TestDataFactory.makeListSongs(5))
        assertThat(songsAdapter.itemCount, equalTo(5))
    }

}
