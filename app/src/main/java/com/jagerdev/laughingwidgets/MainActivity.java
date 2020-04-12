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
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.jagerdev.laughingwidgets.configurators.AddWidgetState;
import com.jagerdev.laughingwidgets.configurators.Configurator;
import com.jagerdev.laughingwidgets.widget_providers.BaseLaughWidgetProvider;
import com.jagerdev.laughingwidgets.widget_utils.CustomWidgetPickReceiver;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AddWidgetState {

    Button btnUpdateWidgets;

    ImageView chosenImage;
    GridView gridFaces;
    TextView txtChoseWidget, txtSelectConfig, txtWidgetInfo, txtWidgetName;
    ScrollView scrollSounds;
    CheckBox checkAllLaughs;
    LinearLayout panelLaughs;
    Button addButton;

    private Configurator configurator;

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

        chosenImage = findViewById(R.id.img_chosen_image);
        gridFaces = findViewById(R.id.grid_faces);

        txtChoseWidget = findViewById(R.id.txt_chose_widget);
        checkAllLaughs = findViewById(R.id.check_all_laughs);
        panelLaughs = findViewById(R.id.panel_laughs);
        txtSelectConfig = findViewById(R.id.txt_select_config);
        txtWidgetInfo = findViewById(R.id.txt_widget_info);
        scrollSounds = findViewById(R.id.scroll_sounds);
        txtWidgetName = findViewById(R.id.txt_widget_name);
        addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(this);

        configurator = new Configurator(this, this, null,  null,
                chosenImage,  gridFaces,  txtChoseWidget,  txtSelectConfig, txtWidgetInfo, txtWidgetName,
                scrollSounds,  checkAllLaughs,  panelLaughs);
        configurator.showWidgetFaces();
    }

    void updateAllWidgets()
    {
        int[] ids = PlayerService.updateAllWidgets(this);
        Toast.makeText(this, String.format("All %s widgets were updated", ids.length), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (!configurator.isFacesViewShown()) configurator.showWidgetFaces();
        else super.onBackPressed();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_update_widgets:
                updateAllWidgets();
                break;
            case R.id.add_button:
                requestAppWidget();
                break;
        }
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

    private void requestAppWidget() {
        configurator.stopMediaPlayer();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            AppWidgetManager appWidgetManager =
                    this.getSystemService(AppWidgetManager.class);

            String widgetClassId = configurator.getWidgetClass();
            ComponentName myProvider = new ComponentName(this, BaseLaughWidgetProvider.getWidgetClass(widgetClassId));

            String pinningNotSupportedError = "Widget pinning is not supported by your launcher. Please use your launcher's picker to put the widget to your home screen.";

            if (appWidgetManager != null && appWidgetManager.isRequestPinAppWidgetSupported()) {
                Toast.makeText(this, "Choose a widget, and pull it to your home screen! Or use your launcher's picker to put the widget to home screen.", Toast.LENGTH_LONG).show();
                Intent callbackIntent = new Intent(this, CustomWidgetPickReceiver.class);
                callbackIntent.putExtra("customFace", true);
                callbackIntent.putExtra(PlayerService.KEY_WIDGET_CLASS, widgetClassId);
                callbackIntent.putStringArrayListExtra(CustomWidgetPickReceiver.KEY_CHOSEN_INDEXES, new ArrayList<>(configurator.getChosenSoundIndexes()));
                PendingIntent successCallback = PendingIntent.getBroadcast(
                        this, 0, callbackIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                if (!appWidgetManager.requestPinAppWidget(myProvider, null, successCallback))
                    Toast.makeText(this, pinningNotSupportedError , Toast.LENGTH_LONG).show();
            }
            else Toast.makeText(this, pinningNotSupportedError, Toast.LENGTH_LONG).show();
        }
        else Toast.makeText(this, "Widget pinning is available only after Android Oreo. Please use your launcher's widget picker.", Toast.LENGTH_LONG).show();
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


}
