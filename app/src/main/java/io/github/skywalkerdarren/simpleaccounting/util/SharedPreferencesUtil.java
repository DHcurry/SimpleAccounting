package io.github.skywalkerdarren.simpleaccounting.util;

import android.app.Activity;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
    public static String name;
    public static Activity activity;
    public static SharedPreferences getSharedPre(String name, Activity activity){
        SharedPreferencesUtil.name = name;
        SharedPreferencesUtil.activity = activity;
        return activity.getSharedPreferences(name,activity.MODE_ENABLE_WRITE_AHEAD_LOGGING | activity.MODE_MULTI_PROCESS);
    }

    public static void save(String key,String value){
        SharedPreferences.Editor editor = getSharedPre(name,activity).edit();//获取Editor
        editor.putString(key,value);
        editor.commit();
    }

    public static String get(String key){
        return getSharedPre(name,activity).getString(key,null);
    }
}
