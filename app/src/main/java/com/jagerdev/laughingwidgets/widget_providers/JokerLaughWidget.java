package com.jagerdev.laughingwidgets.widget_providers;

import com.jagerdev.laughingwidgets.BaseLaughWidgetProvider;
import com.jagerdev.laughingwidgets.R;

public class JokerLaughWidget extends BaseLaughWidgetProvider
{
       public static final String WIDGET_CLASS = "joker";
       public static final int[] SOUND_RESOURCES = {R.raw.joker_laugh_1, R.raw.joker_laugh_2};

       @Override
       public int getCalmResourceId() {
              return R.drawable.joker;
       }

       @Override
       public int getLaughingResourceId() {
              return R.drawable.joker_laugh;
       }

       @Override
       public int[] getSoundResources() {
              return SOUND_RESOURCES;
       }

       @Override
       public String getWidgetName() {
              return "Joker";
       }
}

