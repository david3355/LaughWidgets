package com.jagerdev.laughingwidgets.widget_providers;

import com.jagerdev.laughingwidgets.R;

public class AceVenturaLaughWidget extends BaseLaughWidgetProvider
{
       public static final String WIDGET_CLASS = "aceventura";
       public static final int WIDGET_CLASS_ID = 10005;
       public static final int[] SOUND_RESOURCES =
               {
                       R.raw.ace1,
                       R.raw.ace2,
                       R.raw.ace3,
                       R.raw.ace4,
                       R.raw.ace5,
                       R.raw.ace6,
                       R.raw.ace7,
                       R.raw.ace8
               };

       @Override
       public int getCalmResourceId() {
              return R.drawable.aceventura;
       }

       @Override
       public int getLaughingResourceId() {
              return R.drawable.aceventura_laugh;
       }

       @Override
       public int[] getSoundResources() {
              return SOUND_RESOURCES;
       }

       @Override
       public String getWidgetName() {
              return "Ace Ventura";
       }

       @Override
       public int getWidgetClassId() {
              return WIDGET_CLASS_ID;
       }
}

