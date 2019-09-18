package com.jagerdev.laughingwidgets.configurators;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.jagerdev.laughingwidgets.AndroidUtils;
import com.jagerdev.laughingwidgets.BaseLaughWidgetProvider;
import com.jagerdev.laughingwidgets.PlayerService;
import com.jagerdev.laughingwidgets.R;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseWidgetConfigActivity extends Activity implements CompoundButton.OnCheckedChangeListener
{
       private static final int CHECKBOX_BASE_ID = 100000;
       int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
       CheckBox checkAllLaughs;
       LinearLayout panelLaughs;
       Button addButton;
       private MediaPlayer player;

       View.OnClickListener mOnClickListener = new View.OnClickListener()
       {
              public void onClick(View v)
              {
                     final Context context = BaseWidgetConfigActivity.this;
                     stopMediaPlayer();
                     releaseMediaPlayer();
                     // When the button is clicked, store the string locally
                     List<String> laughIndexes = getChosenSoundIndexes();
                     AndroidUtils.saveSelectedIndexPrefs(context, mAppWidgetId, laughIndexes);

                     // It is the responsibility of the configuration activity to update the app widget
                     AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                     BaseLaughWidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId, R.layout.laugh_widget, R.id.laugh_widget_img);

                     // Make sure we pass back the original appWidgetId
                     Intent resultValue = new Intent();
                     resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                     setResult(RESULT_OK, resultValue);
                     finish();
              }
       };

       public BaseWidgetConfigActivity()
       {
              super();
       }

       @Override
       public void onCreate(Bundle icicle)
       {
              super.onCreate(icicle);

              // Set the result to CANCELED.  This will cause the widget host to cancel
              // out of the widget placement if the user presses the back button.
              setResult(RESULT_CANCELED);

              setContentView(R.layout.laugh_widget_configure);

              String appId = getResources().getString(R.string.admob_app_id);
              MobileAds.initialize(this, appId);
              AdView mainAdView = findViewById(R.id.adview_widget_config);
              AdRequest adRequest = new AdRequest.Builder().build();
              mainAdView.loadAd(adRequest);

              checkAllLaughs = findViewById(R.id.check_all_laughs);
              checkAllLaughs.setOnCheckedChangeListener(this);
              panelLaughs = findViewById(R.id.panel_laughs);
              addButton = findViewById(R.id.add_button);

              // Find the widget id from the intent.
              Intent intent = getIntent();
              Bundle extras = intent.getExtras();
              if (extras != null)
              {
                     mAppWidgetId = extras.getInt(
                             AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
              }

              // If this activity was started with an intent without an app widget ID, finish with an error.
              if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID)
              {
                     finish();
                     return;
              }

              AndroidUtils.savePrefs(this, mAppWidgetId, PlayerService.KEY_WIDGET_CLASS, getWidgetClass());

              int i = 0;

              int[] widgetSoundResources = BaseLaughWidgetProvider.getWidgetSoundResources(getWidgetClass());
              for(int res : widgetSoundResources)
              {
                     CheckBox laughCheckbox = new CheckBox(this);
                     LayoutParams params = new LayoutParams(
                             LayoutParams.MATCH_PARENT,
                             LayoutParams.WRAP_CONTENT
                     );
                     params.setMargins(3, 5, 3, 5);
                     laughCheckbox.setLayoutParams(params);
                     laughCheckbox.setText(String.format("Laugh %s", i+1));
                     int id = CHECKBOX_BASE_ID + i;
                     i++;
                     laughCheckbox.setId(id);
                     laughCheckbox.setOnCheckedChangeListener(this);
                     panelLaughs.addView(laughCheckbox);
              }

              checkAllLaughs.setChecked(true);

              findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

//              mAppWidgetText.setText(loadChosenIndexPrefs(BaseWidgetConfigActivity.this, mAppWidgetId));
       }

       public abstract String getWidgetClass();

       private List<String> getChosenSoundIndexes()
       {
              List<String> indexes = new ArrayList<>();
              for (int i = 0; i < panelLaughs.getChildCount(); i++)
              {
                     CheckBox checkBox = (CheckBox) panelLaughs.getChildAt(i);
                     if (checkBox.getId() != checkAllLaughs.getId() && checkBox.isChecked())
                     {
                            indexes.add(String.valueOf(checkBox.getId() - CHECKBOX_BASE_ID));
                     }
              }
              return indexes;
       }

       private void stopMediaPlayer()
       {
              if (player != null && player.isPlaying())
              {
                     player.stop();
              }
       }

       private void releaseMediaPlayer()
       {
              if (player != null)
              {
                     player.release();
              }
       }

       private void playExampleLaugh(int soundID)
       {
              Log.d(BaseWidgetConfigActivity.class.getName(), String.format("Playing example laugh sound %s", soundID));
              stopMediaPlayer();
              player = MediaPlayer.create(this, soundID);
              player.start();
       }

       boolean isAtLeastOneChecked()
       {
              for (int i = 0; i < panelLaughs.getChildCount(); i++)
              {
                     CheckBox checkBox = (CheckBox) panelLaughs.getChildAt(i);
                     if (checkBox.isChecked()) return true;
              }
              return false;
       }

       @Override
       public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
       {
              if (buttonView.getId() == R.id.check_all_laughs)
              {
                     checkAllLaughs.setText(isChecked? "Uncheck all": "Select all");
                     for (int i = 0; i < panelLaughs.getChildCount(); i++)
                     {
                            CheckBox checkBox = (CheckBox) panelLaughs.getChildAt(i);
                            if (checkBox.getId() != buttonView.getId())
                            {
                                   checkBox.setOnCheckedChangeListener(null);
                                   checkBox.setChecked(isChecked);
                                   checkBox.setOnCheckedChangeListener(this);
                            }
                     }
              }
              else
              {
                     if (!isChecked)
                     {
                            checkAllLaughs.setOnCheckedChangeListener(null);
                            checkAllLaughs.setChecked(false);
                            checkAllLaughs.setOnCheckedChangeListener(this);
                            stopMediaPlayer();
                     }
                     else
                     {
                            int soundId = BaseLaughWidgetProvider.getWidgetSoundResources(getWidgetClass())[buttonView.getId() - CHECKBOX_BASE_ID];
                            playExampleLaugh(soundId);
                     }
              }
              if(isAtLeastOneChecked()) addButton.setEnabled(true);
              else addButton.setEnabled(false);
       }
}

