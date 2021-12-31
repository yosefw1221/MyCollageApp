package com.yotor.solution.mytimetable.portal;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class ShardPref {
    public static final String DEFAULT_ENABLED_DAY = "2,3,4,5,6";
    public static final String ENABLED_DAY_key = "enabled_day";
    public static final String LESSON_SIZE_key = "LESSON_SIZE";
    public static final String LESSON_BREAK_key = "LESSON_BREAK";
    public static int DEFAULT_LESSON_SIZE = 8;
    public static String LESSON_TIME_Key = "LESSON_TIME";
    public static String LocalLessonTimes = "2:10,3:15,4:20,5:25,7:30,8:35,9:40,10:45";
    public static String InteLessonTimes = "8:10,9:15,10:20,11:25,13:30,14:35,15:40,16:45";
    public static int DEFAULT_LESSON_BREAK = 5;
    public static int DEFAULT_LESSON_DURATION = 60;
    public static String LESSON_DURATION_key = "LESSON_DURATION";
    public static void saveValue(Context context, String key, Object value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        if (value instanceof String)
            editor.putString(key, (String) value);
        else if (value instanceof Integer)
            editor.putInt(key, (Integer) value);
        else if (value instanceof Boolean)
            editor.putBoolean(key, (Boolean) value);
        else if (value instanceof Long)
            editor.putLong(key, (Long) value);
        editor.apply();
    }
    public static String getString(Context context, String key,String def) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key,def);
    }
    public static int getInt(Context context, String key,int def) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(key, def);
    }
    public static boolean getBool(Context context, String key,boolean def) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, def);
    }
    public static long getLong(Context context,String key,long def){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(key, def);
    }

}

