package com.jagerdev.laughingwidgets.widget_providers;

import com.jagerdev.laughingwidgets.R;

public class MuttleyLaughWidget extends BaseLaughWidgetProvider
{
       public static final String WIDGET_CLASS = "muttley";
       public static final int WIDGET_CLASS_ID = 10004;
       public static final int[] SOUND_RESOURCES =
               {
                       R.raw.muttley_1,
                       R.raw.muttley_2,
                       R.raw.muttley_3,
               };

       @Override
       public int getCalmResourceId() {
              return R.drawable.muttlay;
       }

       @Override
       public int getLaughingResourceId() {
              return R.drawable.muttlay_laugh;
       }

       @Override
       public int[] getSoundResources() {
              return SOUND_RESOURCES;
       }

       @Override
       public String getWidgetName() {
              return "Muttley";
       }

       @Override
       public int getWidgetClassId() {
              return WIDGET_CLASS_ID;
       }
}

