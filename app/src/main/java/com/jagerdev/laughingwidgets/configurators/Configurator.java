package com.jagerdev.laughingwidgets.configurators;

import android.app.Activity;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jagerdev.laughingwidgets.AndroidUtils;
import com.jagerdev.laughingwidgets.widget_providers.AceVenturaLaughWidget;
import com.jagerdev.laughingwidgets.widget_providers.BaseLaughWidgetProvider;
import com.jagerdev.laughingwidgets.R;
import com.jagerdev.laughingwidgets.widget_providers.DrEvilLaughWidget;
import com.jagerdev.laughingwidgets.widget_providers.EmperorLaughWidget;
import com.jagerdev.laughingwidgets.widget_providers.JabbaLaughWidget;
import com.jagerdev.laughingwidgets.widget_providers.JokerLaughWidget;
import com.jagerdev.laughingwidgets.widget_providers.MuttleyLaughWidget;
import com.jagerdev.laughingwidgets.widget_providers.RisitasLaughWidget;
import com.jagerdev.laughingwidgets.widget_providers.SquealerLaughWidget;

import java.util.ArrayList;
import java.util.List;

public class Configurator implements CompoundButton.OnCheckedChangeListener {

    public Configurator(Activity activityContext, AddWidgetState widgetStateChange, String activityWidgetClass, Integer appWidgetId,
                        ImageView chosenImage, GridView gridFaces, TextView txtChoseWidget, TextView txtSelectConfig, TextView txtWidgetInfo,
                        TextView txtWidgetName, ScrollView scrollSounds, CheckBox checkAllLaughs, LinearLayout panelLaughs)
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
        this.txtWidgetName = txtWidgetName;
        this.scrollSounds = scrollSounds;
        this.checkAllLaughs = checkAllLaughs;
        this.panelLaughs = panelLaughs;
        this.playingId = null;

        FaceChooserAdapter adapter = new FaceChooserAdapter(activityContext, faceIds, onFaceChosenClick);
        gridFaces.setAdapter(adapter);
        gridFaces.deferNotifyDataSetChanged();
        checkAllLaughs.setOnCheckedChangeListener(this);
        chosenImage.setOnClickListener(onChosenFaceClick);

        facesViewShown = false;
    }

    static
    {
        faceIds = new String[] {
                RisitasLaughWidget.WIDGET_CLASS,
                JokerLaughWidget.WIDGET_CLASS,
                SquealerLaughWidget.WIDGET_CLASS,
                MuttleyLaughWidget.WIDGET_CLASS,
                AceVenturaLaughWidget.WIDGET_CLASS,
                EmperorLaughWidget.WIDGET_CLASS,
                DrEvilLaughWidget.WIDGET_CLASS,
                JabbaLaughWidget.WIDGET_CLASS
        };
    }

    private MediaPlayer player;
    private Integer playingId;

    private Activity activityContext;
    private AddWidgetState widgetStateChange;
    private String widgetClass;
    private Integer mAppWidgetId;

    private ImageView chosenImage;
    private GridView gridFaces;
    private TextView txtChoseWidget, txtSelectConfig, txtWidgetInfo, txtWidgetName;
    private ScrollView scrollSounds;
    private CheckBox checkAllLaughs;
    private LinearLayout panelLaughs;

    private static final int CHECKBOX_BASE_ID = 100000;
    private static String[] faceIds;
    private boolean facesViewShown;

    private static final int CHECKBOX_ID = 0;
    private static final int IMAGE_BTN_ID = 1;

    public String getWidgetClass()
    {
        return widgetClass;
    }

    private View.OnClickListener onLaughTestButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ImageButton laughButton = (ImageButton) view;


            if (playingId != null) {
                boolean stopOnly = playingId == laughButton.getId();
                stopMediaPlayer();
                ImageButton playingButton = activityContext.findViewById(playingId);
                if (playingButton != null) playingButton.setImageResource(R.drawable.start);
                playingId = null;
                if (stopOnly) return;
            }

            playingId = laughButton.getId();

            laughButton.setImageResource(R.drawable.stop);
            int soundId = BaseLaughWidgetProvider.getWidgetSoundResources(getWidgetClass())[laughButton.getId() - CHECKBOX_BASE_ID];
            playExampleLaugh(soundId);
        }
    };

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        if (buttonView.getId() == R.id.check_all_laughs)
        {
            checkAllLaughs.setText(isChecked? "Uncheck all": "Select all");
            for (int i = 0; i < panelLaughs.getChildCount(); i++)
            {
                LinearLayout panel = (LinearLayout) panelLaughs.getChildAt(i);
                CheckBox checkBox = (CheckBox) panel.getChildAt(CHECKBOX_ID);
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
            }
        }
        if(isAtLeastOneChecked()) widgetStateChange.atLeaseOneCheckBoxEnabled();
        else widgetStateChange.noCheckBoxIsEnabled();
    }

    public void initConfigActivity()
    {
        int laughingResourceId = BaseLaughWidgetProvider.getLaughingResourceId(getWidgetClass());
        chosenImage.setImageResource(laughingResourceId);

        txtWidgetName.setText(BaseLaughWidgetProvider.getWidgetName(getWidgetClass()));

        if (mAppWidgetId != null) AndroidUtils.savePrefs(activityContext, mAppWidgetId, AndroidUtils.LaughPrefKeys.WIDGET_NAME, getWidgetClass());

        int i = 0;
        int[] widgetSoundResources = BaseLaughWidgetProvider.getWidgetSoundResources(getWidgetClass());
        LayoutInflater inflater = LayoutInflater.from(activityContext);
        for(int res : widgetSoundResources)
        {
            int id = CHECKBOX_BASE_ID + i;

            View view = inflater.inflate(R.layout.laugh_instance, null, false);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(3, 5, 3, 30);
            view.setLayoutParams(params);

            CheckBox laughCheckbox = view.findViewById(R.id.check_laugh);
            ImageButton btnLaughTest = view.findViewById(R.id.btn_laugh_test);

            laughCheckbox.setChecked(true);
            laughCheckbox.setOnCheckedChangeListener(this);
            laughCheckbox.setText(String.format("Laugh %s", i+1));
            btnLaughTest.setId(id);
            btnLaughTest.setOnClickListener(onLaughTestButton);
            panelLaughs.addView(view);
            i++;
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
            txtWidgetName.setText(BaseLaughWidgetProvider.getWidgetName(widgetClass));
            txtWidgetName.setVisibility(View.VISIBLE);
            facesViewShown = false;
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
        txtWidgetName.setVisibility(View.GONE);
        widgetStateChange.onChoosingFaceWidget();
        stopMediaPlayer();
        facesViewShown = true;
    }

    public boolean isFacesViewShown()
    {
        return facesViewShown;
    }

    public List<String> getChosenSoundIndexes()
    {
        List<String> indexes = new ArrayList<>();
        for (int i = 0; i < panelLaughs.getChildCount(); i++)
        {
            LinearLayout panel = (LinearLayout) panelLaughs.getChildAt(i);
            ImageButton button = (ImageButton) panel.getChildAt(IMAGE_BTN_ID);
            CheckBox checkBox = (CheckBox) panel.getChildAt(CHECKBOX_ID);
            if (checkBox.getId() != checkAllLaughs.getId() && checkBox.isChecked())
            {
                indexes.add(String.valueOf(button.getId() - CHECKBOX_BASE_ID));
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
        Log.d(this.getClass().getName(), String.format("Playing example laugh sound %s", soundID));
        stopMediaPlayer();
        player = MediaPlayer.create(activityContext, soundID);
        player.setOnCompletionListener(laughPlayCompleted);
        player.start();
    }

    private MediaPlayer.OnCompletionListener laughPlayCompleted = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            if (playingId != null) {
                ImageButton playingButton = activityContext.findViewById(playingId);
                if (playingButton != null) playingButton.setImageResource(R.drawable.start);
                playingId = null;
            }
        }
    };

    private boolean isAtLeastOneChecked()
    {
        for (int i = 0; i < panelLaughs.getChildCount(); i++)
        {
            LinearLayout panel = (LinearLayout) panelLaughs.getChildAt(i);
            CheckBox checkBox = (CheckBox) panel.getChildAt(CHECKBOX_ID);
            if (checkBox.isChecked()) return true;
        }
        return false;
    }
}
