package eu.se_bastiaan.marietje.data.local;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import eu.se_bastiaan.marietje.BuildConfig;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class PreferencesHelperTest {

    private PreferencesHelper preferencesHelper;

    @Before
    public void setUp() {
        preferencesHelper = new PreferencesHelper(RuntimeEnvironment.application);
    }

    @Test
    public void setCrsfTokenTest() {
        String token = "token";
        preferencesHelper.setCsrftoken(token);
        assertThat(preferencesHelper.getCsrfToken(), is(equalTo(token)));
    }

    @Test
    public void setSessionIdTest() {
        String sessionId = "id";
        preferencesHelper.setSessionId(sessionId);
        assertThat(preferencesHelper.getSessionId(), is(equalTo(sessionId)));
    }

    @Test
    public void saveApiUrlTest() {
        String apiUrl = "url";
        preferencesHelper.setApiUrl(apiUrl);
        assertThat(preferencesHelper.getApiUrl(), is(equalTo(apiUrl)));
    }

    @Test
    public void setCanCancelTest() {
        preferencesHelper.setCanCancel(true);
        assertThat(preferencesHelper.canCancel(), is(equalTo(true)));
    }

    @Test
    public void setCanMoveTest() {
        preferencesHelper.setCanMove(true);
        assertThat(preferencesHelper.canMove(), is(equalTo(true)));
    }

    @Test
    public void setCanSkipTest() {
        preferencesHelper.setCanSkip(true);
        assertThat(preferencesHelper.canSkip(), is(equalTo(true)));
    }

    @Test
    public void defaultValuesTest() {
        assertThat(preferencesHelper.getApiUrl(), is(equalTo(BuildConfig.API_URL)));
        assertThat(preferencesHelper.getSessionId(), isEmptyString());
        assertThat(preferencesHelper.getCsrfToken(), isEmptyString());
        assertThat(preferencesHelper.canMove(), is(equalTo(false)));
        assertThat(preferencesHelper.canCancel(), is(equalTo(false)));
        assertThat(preferencesHelper.canSkip(), is(equalTo(false)));
    }

}