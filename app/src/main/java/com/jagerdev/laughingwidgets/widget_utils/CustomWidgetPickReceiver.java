package com.jagerdev.laughingwidgets.widget_utils;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jagerdev.laughingwidgets.AndroidUtils;
import com.jagerdev.laughingwidgets.PlayerService;
import com.jagerdev.laughingwidgets.R;
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
        List<String> selectedIndexes = intent.getStringArrayListExtra(KEY_CHOSEN_INDEXES);

        AndroidUtils.savePrefs(context, widgetId, PlayerService.KEY_WIDGET_CLASS, widgetClass);
        AndroidUtils.saveSelectedIndexPrefs(context, widgetId, selectedIndexes);

//        int[] widgetIDs = AppWidgetManager.getInstance(context).getAppWidgetIds(myProvider);
//        callbackIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIDs);

//        Intent widgetUpdater = new Intent(context, RisitasLaughWidget.class);
//        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//        widgetUpdater.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
//        context.sendBroadcast(widgetUpdater);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        BaseLaughWidgetProvider.updateAppWidget(context, appWidgetManager, widgetId, R.layout.laugh_widget, R.id.laugh_widget_img);

//        BaseLaughWidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId, R.layout.laugh_widget, R.id.laugh_widget_img);
//        Intent configActivity = new Intent(context, CustomWidgetConfigActivity.class);
//        configActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
//        context.startActivity(configActivity);

    }
}
