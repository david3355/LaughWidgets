package com.jagerdev.laughingwidgets;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.jagerdev.laughingwidgets.configurators.BaseWidgetConfigActivity;
import com.jagerdev.laughingwidgets.widget_providers.RisitasLaughWidget;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnUpdateWidgets, btnRequestAddingWidgets;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String appId = getResources().getString(R.string.admob_app_id);
        MobileAds.initialize(this, appId);
        AdView mainAdView = findViewById(R.id.adview_main_menu);
        AdRequest adRequest = new AdRequest.Builder().build();
        mainAdView.loadAd(adRequest);


        btnUpdateWidgets = findViewById(R.id.btn_update_widgets);
        btnUpdateWidgets.setOnClickListener(this);

        btnRequestAddingWidgets = findViewById(R.id.btn_request_adding_widgets);
        btnRequestAddingWidgets.setOnClickListener(this);
    }

    void updateAllWidgets()
    {
        int[] ids = PlayerService.updateAllWidgets(this);
        Toast.makeText(this, String.format("All %s widgets were updated", ids.length), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_update_widgets:
                updateAllWidgets();
                break;
            case R.id.btn_request_adding_widgets:
                requestAppWidget();
                break;
        }
    }

    private void requestAppWidget() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            AppWidgetManager appWidgetManager =
                    this.getSystemService(AppWidgetManager.class);

            ComponentName myProvider = new ComponentName(this, RisitasLaughWidget.class);

            if (appWidgetManager != null && appWidgetManager.isRequestPinAppWidgetSupported()) {
                Toast.makeText(this, "Choose a widget, and pull it to your homescreen!", Toast.LENGTH_LONG).show();
                Intent callbackIntent = new Intent(this, BaseWidgetConfigActivity.class);
                PendingIntent successCallback = PendingIntent.getActivity(
                        this, 0, callbackIntent, PendingIntent.FLAG_UPDATE_CURRENT);

//                RemoteViews appWindgetPreview = new RemoteViews(this.getPackageName(), R.layout.laugh_widget);
//                Bundle bundle = new Bundle();
//                bundle.putParcelable(AppWidgetManager.EXTRA_APPWIDGET_PREVIEW, appWindgetPreview);
                appWidgetManager.requestPinAppWidget(myProvider, null, successCallback);
            }
        }
    }

}
