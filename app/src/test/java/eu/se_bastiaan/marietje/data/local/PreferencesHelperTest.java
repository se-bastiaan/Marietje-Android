package eu.se_bastiaan.marietje.data.local;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.empty;
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
    public void saveCookiesTest() {
        Set<String> cookies = new HashSet<>();
        cookies.add("test");
        preferencesHelper.setCookies(cookies);

        assertThat(preferencesHelper.getCookies(), is(equalTo(cookies)));
    }

    @Test
    public void saveApiUrlTest() {
        String apiUrl = "url";
        preferencesHelper.setApiUrl(apiUrl);

        assertThat(preferencesHelper.getApiUrl(), is(equalTo(apiUrl)));
    }

    @Test
    public void defaultValuesTest() {
        assertThat(preferencesHelper.getApiUrl(), isEmptyString());
        assertThat(preferencesHelper.getCookies(), empty());
    }

}