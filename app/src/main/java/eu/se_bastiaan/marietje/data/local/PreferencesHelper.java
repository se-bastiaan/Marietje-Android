package eu.se_bastiaan.marietje.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import eu.se_bastiaan.marietje.BuildConfig;
import eu.se_bastiaan.marietje.injection.ApplicationContext;

@Singleton
public class PreferencesHelper {

    private static final String PREF_FILE_NAME = "biermaster_prefs";

    public static final String API_URL = "api_url";
    public static final String CSRFTOKEN = "csrftoken";
    public static final String SESSIONID = "sessionid";
    public static final String CAN_CANCEL = "can_cancel";
    public static final String CAN_MOVE = "can_move";
    public static final String CAN_SKIP = "can_skip";

    private final SharedPreferences pref;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        this.pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    // IN THIS SECTION ADD METHODS PER PREFERENCE

    public String getCsrfToken() {
        return get(CSRFTOKEN + getApiUrl(), "");
    }

    public void setCsrftoken(String token) {
        save(CSRFTOKEN + getApiUrl(), token);
    }

    public String getSessionId() {
        return get(SESSIONID + getApiUrl(), "");
    }

    public void setSessionId(String sessionId) {
        save(SESSIONID + getApiUrl(), sessionId);
    }

    public String getApiUrl() {
        return get(API_URL, BuildConfig.API_URL);
    }

    public void setApiUrl(String apiUrl) {
        save(API_URL, apiUrl);
    }

    public Boolean canCancel() {
        return get(CAN_CANCEL, false);
    }

    public void setCanCancel(boolean canCancel) {
        save(CAN_CANCEL, canCancel);
    }

    public Boolean canMove() {
        return get(CAN_MOVE, false);
    }

    public void setCanMove(boolean canMove) {
        save(CAN_MOVE, canMove);
    }

    public Boolean canSkip() {
        return get(CAN_SKIP, false);
    }

    public void setCanSkip(boolean canSkip) {
        save(CAN_SKIP, canSkip);
    }

    // DO NOT EDIT BELOW

    /**
     * Clear the central {@link SharedPreferences}
     */
    public void clear() {
        pref.edit().clear().apply();
    }

    /**
     * Save an object to the central {@link SharedPreferences}
     *
     * @param key   Key of the preference
     * @param value Value of the preference
     */
    public void save(String key, Object value) {
        Gson gson = new GsonBuilder().create();
        pref.edit().putString(key, gson.toJson(value)).apply();
    }

    /**
     * Get a saved object from the central {@link SharedPreferences}
     *
     * @param key Key of the preference
     * @return The saved bool
     */
    public <T> T get(String key, Class<T> classType) {
        SharedPreferences preferences = pref;
        if (preferences.contains(key)) {
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(preferences.getString(key, ""), classType);
        }
        return null;
    }

    /**
     * Get a saved object from the central {@link SharedPreferences}
     *
     * @param key Key of the preference
     * @return The saved bool
     */
    public <T> T get(String key, Type classType) {
        SharedPreferences preferences = pref;
        if (preferences.contains(key)) {
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(preferences.getString(key, ""), classType);
        }

        try {
            return (T) classType.getClass().newInstance();
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    /**
     * Save a string to the central {@link .content.SharedPreferences}
     *
     * @param key   Key of the preference
     * @param value Value of the preference
     */
    public void save(String key, String value) {
        pref.edit().putString(key, value).apply();
    }

    /**
     * Get a saved string from the central {@link SharedPreferences}
     *
     * @param key          Key of the preference
     * @param defaultValue Default
     * @return The saved String
     */
    public String get(String key, String defaultValue) {
        return pref.getString(key, defaultValue);
    }

    /**
     * Save a boolean to the central {@link SharedPreferences}
     *
     * @param key   Key of the preference
     * @param value Value of the preference
     */
    public void save(String key, boolean value) {
        pref.edit().putBoolean(key, value).apply();
    }

    /**
     * Get a saved boolean from the central {@link SharedPreferences}
     *
     * @param key          Key of the preference
     * @param defaultValue Default
     * @return The saved bool
     */
    public Boolean get(String key, boolean defaultValue) {
        return pref.getBoolean(key, defaultValue);
    }

    /**
     * Save a long to the central {@link SharedPreferences}
     *
     * @param key   Key of the preference
     * @param value Value of the preference
     */
    public void save(String key, long value) {
        pref.edit().putLong(key, value).apply();
    }

    /**
     * Get a saved long from the central {@link SharedPreferences}
     *
     * @param key          Key of the preference
     * @param defaultValue Default
     * @return The saved long
     */
    public long get(String key, long defaultValue) {
        return pref.getLong(key, defaultValue);
    }

    /**
     * Save a int to the central {@link SharedPreferences}
     *
     * @param key   Key of the preference
     * @param value Value of the preference
     */
    public void save(String key, int value) {
        pref.edit().putInt(key, value).apply();
    }

    /**
     * Get a saved integer from the central {@link SharedPreferences}
     *
     * @param key          Key of the preference
     * @param defaultValue Default
     * @return The saved integer
     */
    public int get(String key, int defaultValue) {
        return pref.getInt(key, defaultValue);
    }

    /**
     * Save a string set to the central {@link SharedPreferences}
     *
     * @param key   Key of the preference
     * @param value Value of the preference
     */
    public void save(String key, Set<String> value) {
        pref.edit().putStringSet(key, value).apply();
    }

    /**
     * Get a saved string set from the central {@link SharedPreferences}
     *
     * @param key          Key of the preference
     * @param defaultValue Default
     * @return The saved set
     */
    public Set<String> get(String key, Set<String> defaultValue) {
        return pref.getStringSet(key, defaultValue);
    }

    /**
     * Check if the central {@link SharedPreferences} contains a preference that uses that key
     *
     * @param key Key
     * @return {@code true} if there exists a preference
     */
    public Boolean contains(String key) {
        return pref.contains(key);
    }

    /**
     * Remove item from the central {@link SharedPreferences} if it exists
     *
     * @param key Key
     */
    public void remove(String key) {
        pref.edit().remove(key).apply();
    }

}