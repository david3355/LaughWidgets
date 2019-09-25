package com.jagerdev.laughingwidgets.widget_providers;

import com.jagerdev.laughingwidgets.R;

public class JokerLaughWidget extends BaseLaughWidgetProvider
{
       public static final String WIDGET_CLASS = "joker";
       public static final int WIDGET_CLASS_ID = 10002;
       public static final int[] SOUND_RESOURCES =
               {
                       R.raw.joker_1,
                       R.raw.joker_2,
                       R.raw.joker_3,
                       R.raw.joker_4,
                       R.raw.joker_5,
                       R.raw.joker_6,
                       R.raw.joker_7,
                       R.raw.joker_8,
                       R.raw.joker_9,
                       R.raw.joker_10,
                       R.raw.joker_11,
                       R.raw.joker_12,
                       R.raw.joker_13,
                       R.raw.joker_14,
                       R.raw.joker_15,
                       R.raw.joker_16,
                       R.raw.joker_17,
                       R.raw.joker_18,
                       R.raw.joker_19,
                       R.raw.joker_20,
                       R.raw.joker_21,
                       R.raw.joker_22,
                       R.raw.joker_23,
                       R.raw.joker_24,
                       R.raw.joker_25,
                       R.raw.joker_26,
                       R.raw.joker_27,
                       R.raw.joker_28,
                       R.raw.joker_29,
                       R.raw.joker_100,
                       R.raw.joker_101,
                       R.raw.joker_102,
                       R.raw.joker_103,
                       R.raw.joker_104,
                       R.raw.joker_105,
                       R.raw.joker_106,
               };

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

       @Override
       public int getWidgetClassId() {
              return WIDGET_CLASS_ID;
       }
}

