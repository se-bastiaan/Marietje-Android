package nl.ru.science.mariedroid.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Sebastiaan on 12-06-14.
 */
public class AppUtils {

    /**
     * Get application versionCode
     * @param context Context
     * @return versionCode as Integer
     */
    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * Set length of integer string
     * @param str Input string
     * @param length Required length of string
     * @return String input with prepended 0's until length reached
     */
    public static String setStringLength(String str, int length) {
        while(str.length() < length) {
            str = "0" + str;
        }
        return str;
    }

}
