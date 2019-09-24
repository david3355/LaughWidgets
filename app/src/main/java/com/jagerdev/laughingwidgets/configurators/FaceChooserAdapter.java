package com.jagerdev.laughingwidgets.configurators;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jagerdev.laughingwidgets.widget_providers.BaseLaughWidgetProvider;

public class FaceChooserAdapter extends BaseAdapter {

    private final Context mContext;
    private final String[] widgetClasses;
    private View.OnClickListener onFaceChosenClick;

    public FaceChooserAdapter(Context context, String[] widgetClasses, View.OnClickListener onFaceChosenClick) {
        this.mContext = context;
        this.widgetClasses = widgetClasses;
        this.onFaceChosenClick = onFaceChosenClick;
    }

    @Override
    public int getCount() {
        return widgetClasses.length;
    }

    @Override
    public Object getItem(int i) {
        return widgetClasses[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        int resourceId = BaseLaughWidgetProvider.getLaughingResourceId(widgetClasses[i]);
        ImageView view = new ImageView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                250,
                250
        );
        params.setMargins(5, 5, 5, 5);
        view.setLayoutParams(params);
        view.setImageResource(resourceId);
        view.setOnClickListener(onFaceChosenClick);
        view.setId(BaseLaughWidgetProvider.getWidgetClassId(widgetClasses[i]));
        return view;
    }
}
