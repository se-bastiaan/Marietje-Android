package nl.ru.science.mariedroid.utils;

import android.content.Context;
import android.content.SharedPreferences;

import nl.ru.science.mariedroid.Constants;

/**
 * Created by Sebastiaan on 11-06-14.
 */
public class PrefUtils {

    public static void saveUserLogin(Context context, String username, String password) {
        getPrefs(context).edit().putString("username", username).putString("password", password).apply();
    }

    public static String getUsername(Context context, String defaultValue) {
        return getPrefs(context).getString("username", defaultValue);
    }

    public static String getPassword(Context context, String defaultValue) {
        return getPrefs(context).getString("password", defaultValue);
    }

    public static void saveInstanceIndex(Context context, Integer index) {
        getPrefs(context).edit().putInt("instance_index", index).apply();
    }

    public static Integer getInstanceIndex(Context context) {
        return getPrefs(context).getInt("instance_index", -1);
    }

    public static void save(Context context, String key, String value) {
        getPrefs(context).edit().putString(key, value).apply();
    }

    public static String get(Context context, String key, String defaultValue) {
        return getPrefs(context).getString(key, defaultValue);
    }

    public static void save(Context context, String key, Boolean value) {
        getPrefs(context).edit().putBoolean(key, value).apply();
    }

    public static Boolean get(Context context, String key, Boolean defaultValue) {
        return getPrefs(context).getBoolean(key, defaultValue);
    }

    public static Boolean contains(Context context, String key) {
        return getPrefs(context).contains(key);
    }

    public static void reset(Context context) {
        getPrefs(context).edit().clear().apply();
    }

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(Constants.PREFS_FILE, Context.MODE_PRIVATE);
    }

}
