package com.jagerdev.laughingwidgets.configurators;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.jagerdev.laughingwidgets.AndroidUtils;
import com.jagerdev.laughingwidgets.widget_providers.BaseLaughWidgetProvider;
import com.jagerdev.laughingwidgets.R;

import java.util.List;

public abstract class BaseWidgetConfigActivity extends Activity implements AddWidgetState
{
       int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
       CheckBox checkAllLaughs;
       LinearLayout panelLaughs;
       Button addButton;
       ImageView chosenImage;
       GridView gridFaces;
       TextView txtChoseWidget, txtSelectConfig, txtWidgetInfo;
       ScrollView scrollSounds;

       private Configurator configurator;



       View.OnClickListener mOnClickListener = new View.OnClickListener()
       {
              public void onClick(View v)
              {
                     final Context context = BaseWidgetConfigActivity.this;
                     configurator.stopMediaPlayer();
                     // When the button is clicked, store the string locally
                     List<String> laughIndexes = configurator.getChosenSoundIndexes();
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
       protected void onPause() {
              super.onPause();
              configurator.stopMediaPlayer();
       }

       @Override
       protected void onStop() {
              super.onStop();
              configurator.releaseMediaPlayer();
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


              chosenImage = findViewById(R.id.img_chosen_image);
              gridFaces = findViewById(R.id.grid_faces);
              txtChoseWidget = findViewById(R.id.txt_chose_widget);
              checkAllLaughs = findViewById(R.id.check_all_laughs);
              panelLaughs = findViewById(R.id.panel_laughs);
              addButton = findViewById(R.id.add_button);
              txtSelectConfig = findViewById(R.id.txt_select_config);
              txtWidgetInfo = findViewById(R.id.txt_widget_info);
              scrollSounds = findViewById(R.id.scroll_sounds);

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

              configurator = new Configurator(this, this, getSpecificWidgetClass(),  mAppWidgetId,
                       chosenImage,  gridFaces,  txtChoseWidget,  txtSelectConfig, txtWidgetInfo,
                       scrollSounds,  checkAllLaughs,  panelLaughs);

              configurator.initConfigActivity();

              findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

//              mAppWidgetText.setText(loadChosenIndexPrefs(BaseWidgetConfigActivity.this, mAppWidgetId));
       }


       @Override
       public void atLeaseOneCheckBoxEnabled()
       {
              addButton.setEnabled(true);
       }

       @Override
       public void noCheckBoxIsEnabled()
       {
              addButton.setEnabled(false);
       }

       @Override
       public void onWidgetFaceChosen()
       {
              addButton.setVisibility(View.VISIBLE);
       }

       @Override
       public void onChoosingFaceWidget()
       {
              addButton.setVisibility(View.GONE);
       }

       public abstract String getSpecificWidgetClass();

}

