package eu.se_bastiaan.marietje.data.local

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

import eu.se_bastiaan.marietje.BuildConfig

import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.isEmptyString
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class PreferencesHelperTest {

    private var preferencesHelper: PreferencesHelper? = null

    @Before
    fun setUp() {
        preferencesHelper = PreferencesHelper(RuntimeEnvironment.application)
    }

    @Test
    fun setCrsfTokenTest() {
        val token = "token"
        preferencesHelper!!.setCsrftoken(token)
        assertThat(preferencesHelper!!.csrfToken, `is`(equalTo(token)))
    }

    @Test
    fun setSessionIdTest() {
        val sessionId = "id"
        preferencesHelper!!.sessionId = sessionId
        assertThat(preferencesHelper!!.sessionId, `is`(equalTo(sessionId)))
    }

    @Test
    fun saveApiUrlTest() {
        val apiUrl = "url"
        preferencesHelper!!.apiUrl = apiUrl
        assertThat(preferencesHelper!!.apiUrl, `is`(equalTo(apiUrl)))
    }

    @Test
    fun setCanCancelTest() {
        preferencesHelper!!.setCanCancel(true)
        assertThat(preferencesHelper!!.canCancel(), `is`(equalTo(true)))
    }

    @Test
    fun setCanMoveTest() {
        preferencesHelper!!.setCanMove(true)
        assertThat(preferencesHelper!!.canMove(), `is`(equalTo(true)))
    }

    @Test
    fun setCanSkipTest() {
        preferencesHelper!!.setCanSkip(true)
        assertThat(preferencesHelper!!.canSkip(), `is`(equalTo(true)))
    }

    @Test
    fun defaultValuesTest() {
        assertThat(preferencesHelper!!.apiUrl, `is`(equalTo(BuildConfig.API_URL)))
        assertThat(preferencesHelper!!.sessionId, isEmptyString())
        assertThat(preferencesHelper!!.csrfToken, isEmptyString())
        assertThat(preferencesHelper!!.canMove(), `is`(equalTo(false)))
        assertThat(preferencesHelper!!.canCancel(), `is`(equalTo(false)))
        assertThat(preferencesHelper!!.canSkip(), `is`(equalTo(false)))
    }

}