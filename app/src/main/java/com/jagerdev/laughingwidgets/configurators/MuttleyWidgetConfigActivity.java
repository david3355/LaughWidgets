package com.jagerdev.laughingwidgets.configurators;


import com.jagerdev.laughingwidgets.widget_providers.MuttleyLaughWidget;

public class MuttleyWidgetConfigActivity extends BaseWidgetConfigActivity {

    @Override
    public String getSpecificWidgetClass() {
        return MuttleyLaughWidget.WIDGET_CLASS;
    }
}
