package com.jagerdev.laughingwidgets.widget_providers;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.jagerdev.laughingwidgets.AndroidUtils;
import com.jagerdev.laughingwidgets.PlayerService;
import com.jagerdev.laughingwidgets.R;

import java.util.Set;

public abstract class BaseLaughWidgetProvider extends AppWidgetProvider {

    public static final String LAUGH_IDS = "laugh_ids";

    public static int[] getWidgetSoundResources(String widgetClass)
    {
        return getProviderByClass(widgetClass).getSoundResources();
    }

    public static int getCalmResourceId(String widgetClass)
    {
        return getProviderByClass(widgetClass).getCalmResourceId();
    }

    public static int getLaughingResourceId(String widgetClass)
    {
        return getProviderByClass(widgetClass).getLaughingResourceId();
    }

    public static String getWidgetName(String widgetClass)
    {
        return getProviderByClass(widgetClass).getWidgetName();
    }

    public static int getWidgetClassId(String widgetClass)
    {
        return getProviderByClass(widgetClass).getWidgetClassId();
    }

    public static Class getWidgetClass(String widgetClass)
    {
        return getProviderByClass(widgetClass).getClass();
    }

    private static BaseLaughWidgetProvider getProviderByClass(String widgetClass)
    {
        switch (widgetClass)
        {
            case RisitasLaughWidget.WIDGET_CLASS: return new RisitasLaughWidget();
            case JokerLaughWidget.WIDGET_CLASS: return new JokerLaughWidget();
            case SquealerLaughWidget.WIDGET_CLASS: return new SquealerLaughWidget();
            case MuttleyLaughWidget.WIDGET_CLASS: return new MuttleyLaughWidget();
            case AceVenturaLaughWidget.WIDGET_CLASS: return new AceVenturaLaughWidget();
            case EmperorLaughWidget.WIDGET_CLASS: return new EmperorLaughWidget();
            case DrEvilLaughWidget.WIDGET_CLASS: return new DrEvilLaughWidget();
            case JabbaLaughWidget.WIDGET_CLASS: return new JabbaLaughWidget();
        }
        return null;
    }

    public static String getWidgetClassByClassId(int widgetClassId)
    {
        switch (widgetClassId)
        {
            case RisitasLaughWidget.WIDGET_CLASS_ID: return RisitasLaughWidget.WIDGET_CLASS;
            case JokerLaughWidget.WIDGET_CLASS_ID: return JokerLaughWidget.WIDGET_CLASS;
            case SquealerLaughWidget.WIDGET_CLASS_ID: return SquealerLaughWidget.WIDGET_CLASS;
            case MuttleyLaughWidget.WIDGET_CLASS_ID: return MuttleyLaughWidget.WIDGET_CLASS;
            case AceVenturaLaughWidget.WIDGET_CLASS_ID: return AceVenturaLaughWidget.WIDGET_CLASS;
            case EmperorLaughWidget.WIDGET_CLASS_ID: return EmperorLaughWidget.WIDGET_CLASS;
            case DrEvilLaughWidget.WIDGET_CLASS_ID: return DrEvilLaughWidget.WIDGET_CLASS;
            case JabbaLaughWidget.WIDGET_CLASS_ID: return JabbaLaughWidget.WIDGET_CLASS;
        }
        return null;
    }

    public abstract int getCalmResourceId();
    public abstract int getLaughingResourceId();
    public abstract int[] getSoundResources();
    public abstract String getWidgetName();
    public abstract int getWidgetClassId();

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId)
    {
        Log.i(BaseLaughWidgetProvider.class.getName(), String.format("Widget %s is being updated", appWidgetId));
        Set<String> chosenIndexes = AndroidUtils.loadChosenIndexPrefs(context, appWidgetId);
        String widgetClass = AndroidUtils.loadPrefs(context, appWidgetId, AndroidUtils.LaughPrefKeys.WIDGET_NAME);
        if (chosenIndexes == null) return;

        String[] indexArray = {};
        indexArray = chosenIndexes.toArray(new String[0]);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.laugh_widget);
        views.setImageViewResource(R.id.laugh_widget_img, getCalmResourceId(widgetClass));
        appWidgetManager.updateAppWidget(appWidgetId,  views);

        Intent svc = new Intent(context, PlayerService.class);
        svc.setAction(PlayerService.START_PLAYER);
        svc.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        svc.putExtra(PlayerService.KEY_WIDGET_CLASS, widgetClass);
        Bundle bundle = new Bundle();
        bundle.putStringArray(LAUGH_IDS, indexArray);
        svc.putExtras(bundle);
        int requestcode = appWidgetId;  // a request code-nak egyedinek kell lennie, különben ugyanaz a PendingIntent objektum jön létre, hiába különböznek az extra adatok!

        try
        {
            PendingIntent pendingIntent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                Log.i(BaseLaughWidgetProvider.class.getName(), "Create foreground service pending intent");
                pendingIntent = PendingIntent.getForegroundService(context, requestcode, svc, 0);
            } else pendingIntent = PendingIntent.getService(context, requestcode, svc, 0);

            views.setOnClickPendingIntent(R.id.laugh_widget_img, pendingIntent);

            // Instruct the widget manager to update the widget

            appWidgetManager.updateAppWidget(appWidgetId, views);
            Log.i(BaseLaughWidgetProvider.class.getName(), String.format("Widget %s is updated", appWidgetId));
        } catch (Exception e)
        {
            Toast.makeText(context, "Failed to update widget: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        Log.d(this.getClass().getName(), String.format("Updating all %s widgets", appWidgetIds.length));
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds)
        {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    @Override
    public void onDeleted(Context context, int[] appWidgetIds)
    {
        Log.d(this.getClass().getName(), String.format("Deleting %s widgets", appWidgetIds.length));
        super.onDeleted(context, appWidgetIds);
        PlayerService.deleteUnusedWidgetInstances(appWidgetIds);

        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds)
        {
            AndroidUtils.deleteChosenIndexPrefs(context, appWidgetId);
            AndroidUtils.deletePreferences(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context)
    {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context)
    {
        // Enter relevant functionality for when the last widget is disabled
    }
}
