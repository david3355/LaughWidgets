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

    public enum LaughPrefKeys
    {
        WIDGET_NAME(PlayerService.KEY_WIDGET_CLASS);

        LaughPrefKeys(String key)
        {
            this.key = key;
        }

        private String key;

        public String getKey()
        {
            return key;
        }
    }

    // Write the prefix to the SharedPreferences object for this widget
    public static void saveSelectedIndexPrefs(Context context, int appWidgetId, List<String> ids)
    {
        Log.d(context.getClass().getName(), String.format("Saving preferences for widget: %s", appWidgetId));
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
        Log.d(context.getClass().getName(), String.format("Removing preferences for widget: %s", appWidgetId));
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    public static void savePrefs(Context context, int appWidgetId, LaughPrefKeys key, String data)
    {
        Log.d(context.getClass().getName(), String.format("Saving preferences for widget: %s", appWidgetId));
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(key.getKey() + appWidgetId, data);
        prefs.apply();
    }

    public static String loadPrefs(Context context, int appWidgetId, LaughPrefKeys key)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(key.getKey() + appWidgetId, null);
    }

    public static void deletePreferences(Context context, int appWidgetId)
    {
        for(LaughPrefKeys key : LaughPrefKeys.values())
        {
            Log.d(context.getClass().getName(), String.format("Removing preference (%s) for widget: %s", key.getKey(), appWidgetId));
            SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
            prefs.remove(key.getKey() + appWidgetId);
            prefs.apply();
        }
    }
}
