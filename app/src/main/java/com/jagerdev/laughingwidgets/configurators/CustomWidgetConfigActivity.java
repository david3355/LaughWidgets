package com.jagerdev.laughingwidgets.configurators;


import com.jagerdev.laughingwidgets.widget_providers.CustomLaughWidget;

public class CustomWidgetConfigActivity extends BaseWidgetConfigActivity {

    @Override
    public String getSpecificWidgetClass() {
        return CustomLaughWidget.WIDGET_CLASS;
    }
}
