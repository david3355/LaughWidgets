package com.jagerdev.laughingwidgets.widget_utils;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jagerdev.laughingwidgets.AndroidUtils;
import com.jagerdev.laughingwidgets.PlayerService;
import com.jagerdev.laughingwidgets.widget_providers.BaseLaughWidgetProvider;

import java.util.List;

public class CustomWidgetPickReceiver extends BroadcastReceiver {

    public static final String KEY_CUSTOM_FACE = "customFace";
    public static final String KEY_CHOSEN_INDEXES = "chosenIndexes";

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean custom = intent.getBooleanExtra(KEY_CUSTOM_FACE, false);
        if (!custom) return;
        int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        String widgetClass = intent.getStringExtra(PlayerService.KEY_WIDGET_CLASS);
        Log.i(this.getClass().getName(), String.format("Manual widget pinning received for %s: %s", widgetClass, widgetId));
        List<String> selectedIndexes = intent.getStringArrayListExtra(KEY_CHOSEN_INDEXES);

        AndroidUtils.savePrefs(context, widgetId, AndroidUtils.LaughPrefKeys.WIDGET_NAME, widgetClass);
        AndroidUtils.saveSelectedIndexPrefs(context, widgetId, selectedIndexes);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        BaseLaughWidgetProvider.updateAppWidget(context, appWidgetManager, widgetId);
    }
}
