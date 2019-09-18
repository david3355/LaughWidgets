package com.jagerdev.laughingwidgets;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Random;

/**
 * Created by Jager on 2019.03.22.
 */
public class WidgetInstance implements MediaPlayer.OnCompletionListener
{
       public WidgetInstance(int widgetID, Context context, Intent intent, MediaStoppedHandler mediaStoppedHandler, String[] widgetIds, int[] soundResources, int calmResourceId, int laughingResourceId)
       {
              this.widgetID = widgetID;
              this.context = context;
              this.intent = intent;
              this.mediaStoppedHandler = mediaStoppedHandler;
              this.soundResources = soundResources;
              this.calmResourceId = calmResourceId;
              this.laughingResourceId = laughingResourceId;
              setUsedIds(widgetIds);
       }

       private Intent intent;
       private Context context;
       private MediaPlayer player;
       private int widgetID;
       private static Random rnd = new Random();
       private MediaStoppedHandler mediaStoppedHandler;
       private int[] soundResources;

       private int[] usedIds;

       private int calmResourceId, laughingResourceId;

       private void setUsedIds(String[] ids)
       {
              usedIds = new int[ids.length];
              for (int i = 0; i < ids.length; i++)
              {
                     usedIds[i] = Integer.parseInt(ids[i]);
              }
       }

       private int getRandomSoundID()
       {
              if (usedIds.length == 0) return soundResources[rnd.nextInt(soundResources.length)];
              return soundResources[usedIds[rnd.nextInt(usedIds.length)]];
       }

       private void updateWidget(Intent intent, int imageID)
       {
              int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
              Log.d(WidgetInstance.class.getName(), String.format("Widget instance is being updated: %s", widgetId));
              AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
              RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.laugh_widget);
              remoteViews.setImageViewResource(R.id.laugh_widget_img, imageID);
              appWidgetManager.updateAppWidget(widgetId, remoteViews);
       }

       private void mediaStopped()
       {
              Log.d(WidgetInstance.class.getName(), String.format("Playing stopped for widget: %s", widgetID));
              updateWidget(intent, calmResourceId);
              if (player != null)
              {
                     player.reset();
                     player.release();
              }
              player = null;
              mediaStoppedHandler.mediaStopped(widgetID);
       }

       public int getWidgetID()
       {
              return widgetID;
       }

       @Override
       public void onCompletion(MediaPlayer mp)
       {
              mediaStopped();
       }

       public void playMedia()
       {
              updateWidget(intent, laughingResourceId);
              int soundID = getRandomSoundID();
              player = MediaPlayer.create(context, soundID);
              player.setOnCompletionListener(this);
              Log.d(WidgetInstance.class.getName(), String.format("Widget %s starts playing sound: %s", widgetID, soundID));
              player.start();
       }

       public void stopMedia()
       {
              if (player != null && player.isPlaying())
              {
                     player.stop();
                     mediaStopped();
              }
       }

       public boolean isPlaying()
       {
              return (player!= null && player.isPlaying());
       }

       @Override
       public boolean equals(Object obj)
       {
              if (obj == null) return false;
              if (obj instanceof  WidgetInstance)
              {
                     WidgetInstance wi = (WidgetInstance) obj;
                     return wi.widgetID == this.widgetID;
              }
              return false;
       }
}
