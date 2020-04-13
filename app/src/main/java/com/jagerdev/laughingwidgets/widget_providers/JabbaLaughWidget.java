package com.jagerdev.laughingwidgets.widget_providers;

import com.jagerdev.laughingwidgets.R;

public class JabbaLaughWidget extends BaseLaughWidgetProvider
{
       public static final String WIDGET_CLASS = "jabba";
       public static final int WIDGET_CLASS_ID = 10008;
       public static final int[] SOUND_RESOURCES =
               {
                       R.raw.jabba1,
                       R.raw.jabba2,
                       R.raw.jabba3,
                       R.raw.jabba4,
                       R.raw.jabba5
               };

       @Override
       public int getCalmResourceId() {
              return R.drawable.jabba;
       }

       @Override
       public int getLaughingResourceId() {
              return R.drawable.jabba_laugh;
       }

       @Override
       public int[] getSoundResources() {
              return SOUND_RESOURCES;
       }

       @Override
       public String getWidgetName() {
              return "Jabba";
       }

       @Override
       public int getWidgetClassId() {
              return WIDGET_CLASS_ID;
       }
}

