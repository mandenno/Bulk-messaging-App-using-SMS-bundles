package com.nupola.okangi.nupola;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListSpannerlarge extends ArrayAdapter<String> {
    private final Activity context;
    private final Integer[] imageId;
    private final String[] web;

    public CustomListSpannerlarge(Activity context, String[] web, Integer[] imageId) {
        super(context, R.layout.list_singlelarge, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;
    }

    public View getView(int position, View view, ViewGroup parent) {
        View rowView = this.context.getLayoutInflater().inflate(R.layout.list_singlelarge, null, true);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imgh1op);
        ((TextView) rowView.findViewById(R.id.txth1op)).setText(this.web[position]);
        imageView.setImageResource(this.imageId[position].intValue());
        return rowView;
    }
}
