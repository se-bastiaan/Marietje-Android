package com.digitalisma.boilerplate.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.digitalisma.boilerplate.injection.ApplicationContext;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferencesHelper {

    private static final String PREF_FILE_NAME = "biermaster_prefs";

    private static final String API_URL = "api_url";
    private static final String TOKEN = "token";

    private final SharedPreferences pref;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    // IN THIS SECTION ADD METHODS PER PREFERENCE

    public String getToken() {
        return get(TOKEN, "");
    }

    public void setToken(String token) {
        save(TOKEN, token);
    }

    public String getApiUrl() {
        return get(TOKEN, "");
    }

    public void setApiUrl(String apiUrl) {
        save(API_URL, apiUrl);
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