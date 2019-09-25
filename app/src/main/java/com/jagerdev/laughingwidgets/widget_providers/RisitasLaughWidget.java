package com.jagerdev.laughingwidgets.widget_providers;

import com.jagerdev.laughingwidgets.R;

public class RisitasLaughWidget extends BaseLaughWidgetProvider {
    public static final String WIDGET_CLASS = "risitas";
    public static final int WIDGET_CLASS_ID = 10001;
    public static final int[] SOUND_RESOURCES = {R.raw.risitas1, R.raw.risitas2, R.raw.risitas3, R.raw.risitas4, R.raw.risitas5, R.raw.risitas6, R.raw.risitas7, R.raw.risitas8, R.raw.risitas9, R.raw.risitas10};

    @Override
    public int getCalmResourceId() {
        return R.drawable.risitas;
    }

    @Override
    public int getLaughingResourceId() {
        return R.drawable.risitas_laugh;
    }

    @Override
    public int[] getSoundResources() {
        return SOUND_RESOURCES;
    }

    @Override
    public String getWidgetName() {
        return "Risitas";
    }

    @Override
    public int getWidgetClassId() {
        return WIDGET_CLASS_ID;
    }
}

