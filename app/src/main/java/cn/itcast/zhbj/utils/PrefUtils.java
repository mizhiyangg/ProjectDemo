package cn.itcast.zhbj.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class PrefUtils {

    private PrefUtils() {

    }

    private static Context context = null;
    private static SharedPreferences currentSharedPreferences = null;
    private static final String SHARE_PREFS_NAME="itcast";

    //init
    public static void init(Context context) {
        init(context, "config");
    }

    public static void init(Context context, String defSharedPreferencesName){
        PrefUtils.context = context;
        setCurrentSharedPreferences(defSharedPreferencesName);
    }

    //set
    public static void setCurrentSharedPreferences(String sharedPreferencesName) {
        currentSharedPreferences = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE);
    }

    //Boolean
    public static void putBoolean(Context applicationContext, String key, boolean value) {
        currentSharedPreferences=applicationContext.getSharedPreferences(SHARE_PREFS_NAME,Context.MODE_PRIVATE);
        currentSharedPreferences.edit().putBoolean(key,value).commit();

    }

    public static boolean getBoolean(Context applicationContext, String key, boolean defValue) {
        currentSharedPreferences=applicationContext.getSharedPreferences(SHARE_PREFS_NAME,Context.MODE_PRIVATE);
        return currentSharedPreferences.getBoolean(key, defValue);
    }

    //String
    public static boolean putString(Context ctx, String key, String value) {
        return currentSharedPreferences.edit().putString(key, value).commit();
    }

    public static String getString(Context ctx, String key, String defValue) {
        return currentSharedPreferences.getString(key, defValue);
    }

    //Int
    public static boolean putInt(String key, int value) {
        return currentSharedPreferences.edit().putInt(key, value).commit();
    }

    public static int getInt(String key, int defValue) {
        return currentSharedPreferences.getInt(key, defValue);
    }

    //Float
    public static boolean putFloat(String key, float value) {
        return currentSharedPreferences.edit().putFloat(key, value).commit();
    }

    public static float getFloat(String key, float defValue) {
        return currentSharedPreferences.getFloat(key, defValue);
    }

    //Long
    public static boolean putLong(String key, long value) {
        return currentSharedPreferences.edit().putLong(key, value).commit();
    }

    public static long getLong(String key, long defValue) {
        return currentSharedPreferences.getLong(key, defValue);
    }

    //StringSet
    public static boolean putStringSet(String key, Set<String> value) {
        return currentSharedPreferences.edit().putStringSet(key, value).commit();
    }

    public static Set<String> getStringSet(String key, Set<String> defValue) {
        return currentSharedPreferences.getStringSet(key, defValue);
    }

    //clear
    public static boolean clear() {
        return currentSharedPreferences.edit().clear().commit();
    }

    //remove
    public static boolean remove(String key) {
        return currentSharedPreferences.edit().remove(key).commit();
    }
}