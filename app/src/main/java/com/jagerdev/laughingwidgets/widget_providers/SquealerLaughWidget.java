package com.jagerdev.laughingwidgets.widget_providers;

import com.jagerdev.laughingwidgets.R;

public class SquealerLaughWidget extends BaseLaughWidgetProvider
{
       public static final String WIDGET_CLASS = "squealer";
       public static final int WIDGET_CLASS_ID = 10003;
       public static final int[] SOUND_RESOURCES =
               {
                       R.raw.squealer_1,
               };

       @Override
       public int getCalmResourceId() {
              return R.drawable.squealer;
       }

       @Override
       public int getLaughingResourceId() {
              return R.drawable.squealer_laugh;
       }

       @Override
       public int[] getSoundResources() {
              return SOUND_RESOURCES;
       }

       @Override
       public String getWidgetName() {
              return "Squealer";
       }

       @Override
       public int getWidgetClassId() {
              return WIDGET_CLASS_ID;
       }
}

