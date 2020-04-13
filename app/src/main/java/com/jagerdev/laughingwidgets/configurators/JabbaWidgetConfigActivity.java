package com.jagerdev.laughingwidgets.configurators;


import com.jagerdev.laughingwidgets.widget_providers.JabbaLaughWidget;

public class JabbaWidgetConfigActivity extends BaseWidgetConfigActivity {

    @Override
    public String getSpecificWidgetClass() {
        return JabbaLaughWidget.WIDGET_CLASS;
    }
}
