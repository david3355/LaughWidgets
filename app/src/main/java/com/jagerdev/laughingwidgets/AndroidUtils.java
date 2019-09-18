package com.jagerdev.laughingwidgets;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.jagerdev.laughingwidgets.configurators.BaseWidgetConfigActivity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AndroidUtils {
    private static final String PREFS_NAME = "com.jagerdev.laughingwidgets.widget_providers.RisitasLaughWidget";
    private static final String PREF_PREFIX_KEY = "widget_laugh_id_array_";

    // Write the prefix to the SharedPreferences object for this widget
    public static void saveSelectedIndexPrefs(Context context, int appWidgetId, List<String> ids)
    {
        Log.d(BaseWidgetConfigActivity.class.getName(), String.format("Saving preferences for widget: %s", appWidgetId));
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        Set<String> idSet= new HashSet<>(ids);
        prefs.putStringSet(PREF_PREFIX_KEY + appWidgetId, idSet);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    public static Set<String> loadChosenIndexPrefs(Context context, int appWidgetId)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getStringSet(PREF_PREFIX_KEY + appWidgetId, null);
    }

    public static void deleteChosenIndexPrefs(Context context, int appWidgetId)
    {
        Log.d(BaseWidgetConfigActivity.class.getName(), String.format("Removing preferences for widget: %s", appWidgetId));
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    public static void savePrefs(Context context, int appWidgetId, String key, String data)
    {
        Log.d(BaseWidgetConfigActivity.class.getName(), String.format("Saving preferences for widget: %s", appWidgetId));
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(key + appWidgetId, data);
        prefs.apply();
    }

    public static String loadPrefs(Context context, int appWidgetId, String key)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(key + appWidgetId, null);
    }
}
