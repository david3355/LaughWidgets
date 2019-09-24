package com.jagerdev.laughingwidgets.widget_providers;

import com.jagerdev.laughingwidgets.R;

public class CustomLaughWidget extends BaseLaughWidgetProvider {
    public static final String WIDGET_CLASS = "CUSTOM";
    public static final int WIDGET_CLASS_ID = 10000;

    @Override
    public int getCalmResourceId() {
        return R.drawable.laugh_notif;
    }

    @Override
    public int getLaughingResourceId() {
        return R.drawable.laugh_notif;
    }

    @Override
    public int[] getSoundResources() {
        return null;
    }

    @Override
    public String getWidgetName() {
        return "CUSTOM";
    }

    @Override
    public int getWidgetClassId() {
        return WIDGET_CLASS_ID;
    }
}

