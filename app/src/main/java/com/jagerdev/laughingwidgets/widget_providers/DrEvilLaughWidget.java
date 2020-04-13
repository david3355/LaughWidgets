package com.jagerdev.laughingwidgets.widget_providers;

import com.jagerdev.laughingwidgets.R;

public class DrEvilLaughWidget extends BaseLaughWidgetProvider
{
       public static final String WIDGET_CLASS = "dr_evil";
       public static final int WIDGET_CLASS_ID = 10007;
       public static final int[] SOUND_RESOURCES =
               {
                       R.raw.drevil1,
                       R.raw.drevil2,
                       R.raw.drevil3
               };

       @Override
       public int getCalmResourceId() {
              return R.drawable.drevil;
       }

       @Override
       public int getLaughingResourceId() {
              return R.drawable.drevil_laugh;
       }

       @Override
       public int[] getSoundResources() {
              return SOUND_RESOURCES;
       }

       @Override
       public String getWidgetName() {
              return "Dr. Evil";
       }

       @Override
       public int getWidgetClassId() {
              return WIDGET_CLASS_ID;
       }
}

