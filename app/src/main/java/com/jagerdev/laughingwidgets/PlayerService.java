package com.jagerdev.laughingwidgets;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.jagerdev.laughingwidgets.widget_providers.BaseLaughWidgetProvider;
import com.jagerdev.laughingwidgets.widget_providers.RisitasLaughWidget;
import com.jagerdev.laughingwidgets.widget_utils.MediaStoppedHandler;
import com.jagerdev.laughingwidgets.widget_utils.WidgetInstance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlayerService extends Service implements MediaStoppedHandler
{
       public PlayerService()
       {
       }

       private final static int NOTIFICATION_ID = 255;
       private final static String CHANNEL_ID = "laughing_widget_notifications";
       public static final String KEY_WIDGET_CLASS = "widget_class";
       private static List<WidgetInstance> widgetInstances = new ArrayList<>();

       private BroadcastReceiver screenOffReceiver = new BroadcastReceiver()
       {
              @Override
              public void onReceive(Context context, Intent intent)
              {
                     Log.i(PlayerService.class.getName(), "Phone screen is switched off");
                     stopAllMedia();
                     shutdownService();
              }
       };

       public static final String START_PLAYER = "startplayer";

       @Override
       public void onCreate()
       {
              registerScreenOffReceiver();
              super.onCreate();
       }

       @Override
       public int onStartCommand(Intent intent, int flags, int startId)
       {
              if (intent != null) {
                     Log.i(PlayerService.class.getName(), "PlayerService is started");
                     int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
                     String widgetClass = intent.getStringExtra(KEY_WIDGET_CLASS);
                     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            Log.d(PlayerService.class.getName(), "Starting foreground");
                            startForeground(NOTIFICATION_ID, buildNotification(BaseLaughWidgetProvider.getWidgetName(widgetClass)));
                     }
                     operate(intent, widgetId, widgetClass);
                     Log.d(PlayerService.class.getName(), "Operation finished in PlayerService");
              }
              return super.onStartCommand(intent, flags, startId);
       }

       private Notification buildNotification(String laughWidgetName)
       {
              NotificationManager notificationManager =
                      (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
              if (notificationManager == null) return null;

              NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this.getApplicationContext(), CHANNEL_ID);
              notifBuilder.setContentTitle(String.format("%s is laughing", laughWidgetName))
                      .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.laugh_notif))
                      .setSmallIcon(R.drawable.laugh_notif)
                      .setStyle(new NotificationCompat.BigTextStyle());

              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
              {
                     NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                             "Laughing widget channel",
                             NotificationManager.IMPORTANCE_MIN);
                     notificationManager.createNotificationChannel(channel);
                     notifBuilder.setChannelId(CHANNEL_ID);
              }

              Notification notif = notifBuilder.build();
              notif.flags |= Notification.FLAG_AUTO_CANCEL;
              return notif;
       }

       public static int[] updateAllWidgets(Context context)
       {
              AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
              int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(context, RisitasLaughWidget.class));
              for (int widgetId : ids)
              {
                     RisitasLaughWidget.updateAppWidget(context, appWidgetManager, widgetId);
              }
              return ids;
       }

       private void operate(Intent intent, int widgetId, String widgetClass)
       {
              Bundle bundle = intent.getExtras();
              String[] laughIds = bundle.getStringArray(RisitasLaughWidget.LAUGH_IDS);
              WidgetInstance widget = getWidgetByID(widgetId);
              if (widget == null)
              {
                     Log.d(PlayerService.class.getName(), String.format("New widget instance is created: %s", widgetId));
                     int[] soundResources = BaseLaughWidgetProvider.getWidgetSoundResources(widgetClass);
                     int calmResourceId = BaseLaughWidgetProvider.getCalmResourceId(widgetClass);
                     int laughingResourceId = BaseLaughWidgetProvider.getLaughingResourceId(widgetClass);
                     widget = new WidgetInstance(widgetId, getApplicationContext(), intent, this, laughIds, soundResources, calmResourceId, laughingResourceId);
                     widgetInstances.add(widget);
              }
              if (!widget.isPlaying())
              {
                     widget.playMedia();
              } else widget.stopMedia();
       }

       private WidgetInstance getWidgetByID(int widgetID)
       {
              for (WidgetInstance winstance : widgetInstances)
              {
                     if (winstance.getWidgetID() == widgetID) return winstance;
              }
              return null;
       }

       private void stopAllMedia()
       {
              Log.i(PlayerService.class.getName(), "Stopping all playing widget");
              for (WidgetInstance winstance : widgetInstances)
              {
                     if (winstance.isPlaying()) winstance.stopMedia();
              }
       }

       private boolean isAnyWidgetPlaying()
       {
              for (WidgetInstance winstance : widgetInstances)
              {
                     if (winstance.isPlaying()) return true;
              }
              return false;
       }

       public static void deleteUnusedWidgetInstances(int[] widgetIDs)
       {
              for (int wid : widgetIDs)
              {
                     removeWidgetInstanceByID(wid);
              }
       }

       private static void removeWidgetInstanceByID(int widgetID)
       {
              Log.d(PlayerService.class.getName(), String.format("Removing widget: %s", widgetID));
              Iterator<WidgetInstance> widget_iterator = widgetInstances.iterator();
              while (widget_iterator.hasNext())
              {
                     WidgetInstance next = widget_iterator.next();
                     if (next.getWidgetID() == widgetID)
                     {
                            if (next.isPlaying()) next.stopMedia();
                            widget_iterator.remove();
                     }
              }
       }

       private void registerScreenOffReceiver()
       {
              IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
              registerReceiver(screenOffReceiver, filter);
              Log.i(PlayerService.class.getName(), "Screen off receiver is registered");
       }

       @Override
       public IBinder onBind(Intent intent)
       {
              return null;
       }

       @Override
       public void onDestroy()
       {
              unregisterReceiver(screenOffReceiver);
              super.onDestroy();
       }

       private void shutdownService()
       {
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
              {
                     Log.d(PlayerService.class.getName(), "Stopping foreground");
                     stopForeground(true);
              }
              // stopService makes sure that service is stopped at any circumstances.
              this.stopService(new Intent(this, PlayerService.class));
              Log.i(PlayerService.class.getName(), "PlayerService stopped");
              // stopSelf() ensures that the service is not stopped until started intents have been processed
       }

       @Override
       public void mediaStopped(int widgetID)
       {
              Log.d(PlayerService.class.getName(), String.format("Widget %s is stopped playing", widgetID));
              if (!isAnyWidgetPlaying())
              {
                     Log.d(PlayerService.class.getName(), String.format("Stopping PlayerService (last widget: %s)", widgetID));
                     shutdownService();
              }
       }
}