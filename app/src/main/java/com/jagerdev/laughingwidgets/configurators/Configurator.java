package com.jagerdev.laughingwidgets.configurators;

import android.app.Activity;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jagerdev.laughingwidgets.AndroidUtils;
import com.jagerdev.laughingwidgets.widget_providers.BaseLaughWidgetProvider;
import com.jagerdev.laughingwidgets.PlayerService;
import com.jagerdev.laughingwidgets.R;
import com.jagerdev.laughingwidgets.widget_providers.JokerLaughWidget;
import com.jagerdev.laughingwidgets.widget_providers.MuttleyLaughWidget;
import com.jagerdev.laughingwidgets.widget_providers.RisitasLaughWidget;
import com.jagerdev.laughingwidgets.widget_providers.SquealerLaughWidget;

import java.util.ArrayList;
import java.util.List;

public class Configurator implements CompoundButton.OnCheckedChangeListener {

    public Configurator(Activity activityContext, AddWidgetState widgetStateChange, String activityWidgetClass, Integer appWidgetId,
                        ImageView chosenImage, GridView gridFaces, TextView txtChoseWidget, TextView txtSelectConfig, TextView txtWidgetInfo,
                        ScrollView scrollSounds, CheckBox checkAllLaughs, LinearLayout panelLaughs)
    {
        this.activityContext = activityContext;
        this.widgetStateChange = widgetStateChange;
        this.widgetClass = activityWidgetClass;
        this.mAppWidgetId = appWidgetId;

        this.chosenImage = chosenImage;
        this.gridFaces = gridFaces;
        this.txtChoseWidget = txtChoseWidget;
        this.txtSelectConfig = txtSelectConfig;
        this.txtWidgetInfo = txtWidgetInfo;
        this.scrollSounds = scrollSounds;
        this.checkAllLaughs = checkAllLaughs;
        this.panelLaughs = panelLaughs;

        FaceChooserAdapter adapter = new FaceChooserAdapter(activityContext, faceIds, onFaceChosenClick);
        gridFaces.setAdapter(adapter);
        gridFaces.deferNotifyDataSetChanged();
        checkAllLaughs.setOnCheckedChangeListener(this);
        chosenImage.setOnClickListener(onChosenFaceClick);
    }

    static
    {
        faceIds = new String[] {
                RisitasLaughWidget.WIDGET_CLASS,
                JokerLaughWidget.WIDGET_CLASS,
                SquealerLaughWidget.WIDGET_CLASS,
                MuttleyLaughWidget.WIDGET_CLASS};
    }

    private MediaPlayer player;

    private Activity activityContext;
    private AddWidgetState widgetStateChange;
    private String widgetClass;
    private Integer mAppWidgetId;

    private ImageView chosenImage;
    private GridView gridFaces;
    private TextView txtChoseWidget, txtSelectConfig, txtWidgetInfo;
    private ScrollView scrollSounds;
    private CheckBox checkAllLaughs;
    private LinearLayout panelLaughs;

    private static final int CHECKBOX_BASE_ID = 100000;
    private static String[] faceIds;

    public String getWidgetClass()
    {
        return widgetClass;
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
        if(isAtLeastOneChecked()) widgetStateChange.atLeaseOneCheckBoxEnabled();
        else widgetStateChange.noCheckBoxIsEnabled();
    }

    public void initConfigActivity()
    {
        int laughingResourceId = BaseLaughWidgetProvider.getLaughingResourceId(getWidgetClass());
        chosenImage.setImageResource(laughingResourceId);

        if (mAppWidgetId != null) AndroidUtils.savePrefs(activityContext, mAppWidgetId, PlayerService.KEY_WIDGET_CLASS, getWidgetClass());

        int i = 0;
        int[] widgetSoundResources = BaseLaughWidgetProvider.getWidgetSoundResources(getWidgetClass());
        for(int res : widgetSoundResources)
        {
            CheckBox laughCheckbox = new CheckBox(activityContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(3, 5, 3, 5);
            laughCheckbox.setLayoutParams(params);
            laughCheckbox.setText(String.format("Laugh %s", i+1));
            int id = CHECKBOX_BASE_ID + i;
            i++;
            laughCheckbox.setId(id);
            laughCheckbox.setChecked(true);
            laughCheckbox.setOnCheckedChangeListener(this);
            panelLaughs.addView(laughCheckbox);
        }

        checkAllLaughs.setChecked(true);
    }

    private View.OnClickListener onFaceChosenClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            widgetClass = BaseLaughWidgetProvider.getWidgetClassByClassId(id);
            panelLaughs.removeAllViews();
            initConfigActivity();
            gridFaces.setVisibility(View.GONE);
            txtChoseWidget.setVisibility(View.GONE);
            chosenImage.setVisibility(View.VISIBLE);
            ImageView chosenView = activityContext.findViewById(id);
            chosenImage.setImageDrawable(chosenView.getDrawable());
            txtWidgetInfo.setVisibility(View.VISIBLE);
            checkAllLaughs.setVisibility(View.VISIBLE);
            scrollSounds.setVisibility(View.VISIBLE);
            txtSelectConfig.setVisibility(View.VISIBLE);
            widgetStateChange.onWidgetFaceChosen();
        }
    };

    private View.OnClickListener onChosenFaceClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showWidgetFaces();
        }
    };

    public void showWidgetFaces()
    {
        chosenImage.setVisibility(View.GONE);
        gridFaces.setVisibility(View.VISIBLE);
        txtChoseWidget.setVisibility(View.VISIBLE);
        txtWidgetInfo.setVisibility(View.GONE);
        checkAllLaughs.setVisibility(View.GONE);
        scrollSounds.setVisibility(View.GONE);
        txtSelectConfig.setVisibility(View.GONE);
        widgetStateChange.onChoosingFaceWidget();
    }

    public List<String> getChosenSoundIndexes()
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

    public void stopMediaPlayer()
    {
        if (player != null && player.isPlaying())
        {
            player.stop();
        }
    }

    public void releaseMediaPlayer()
    {
        if (player != null)
        {
            player.release();
            player = null;
        }
    }

    private void playExampleLaugh(int soundID)
    {
        Log.d(BaseWidgetConfigActivity.class.getName(), String.format("Playing example laugh sound %s", soundID));
        stopMediaPlayer();
        player = MediaPlayer.create(activityContext, soundID);
        player.start();
    }

    private boolean isAtLeastOneChecked()
    {
        for (int i = 0; i < panelLaughs.getChildCount(); i++)
        {
            CheckBox checkBox = (CheckBox) panelLaughs.getChildAt(i);
            if (checkBox.isChecked()) return true;
        }
        return false;
    }
}
