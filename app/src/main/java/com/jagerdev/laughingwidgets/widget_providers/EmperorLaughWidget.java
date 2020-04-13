package com.jagerdev.laughingwidgets.widget_providers;

import com.jagerdev.laughingwidgets.R;

public class EmperorLaughWidget extends BaseLaughWidgetProvider
{
       public static final String WIDGET_CLASS = "emperor";
       public static final int WIDGET_CLASS_ID = 10006;
       public static final int[] SOUND_RESOURCES =
               {
                       R.raw.palpatine1,
                       R.raw.palpatine2,
                       R.raw.palpatine3,
                       R.raw.palpatine4,
                       R.raw.palpatine5,
                       R.raw.palpatine6,
                       R.raw.palpatine7,
                       R.raw.palpatine8
               };

       @Override
       public int getCalmResourceId() {
              return R.drawable.palpatine;
       }

       @Override
       public int getLaughingResourceId() {
              return R.drawable.palpatine_laugh;
       }

       @Override
       public int[] getSoundResources() {
              return SOUND_RESOURCES;
       }

       @Override
       public String getWidgetName() {
              return "The Emperor";
       }

       @Override
       public int getWidgetClassId() {
              return WIDGET_CLASS_ID;
       }
}

