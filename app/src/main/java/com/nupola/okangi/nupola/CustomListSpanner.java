package com.nupola.okangi.nupola;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListSpanner extends ArrayAdapter<String> {
    private final Activity context;
    private final Integer[] imageId;
    private final String[] web;

    public CustomListSpanner(Activity context, String[] web, Integer[] imageId) {
        super(context, R.layout.list_single, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;
    }

    public View getView(int position, View view, ViewGroup parent) {
        View rowView = this.context.getLayoutInflater().inflate(R.layout.list_single, null, true);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imgh1);
        ((TextView) rowView.findViewById(R.id.txth1)).setText(this.web[position]);
        imageView.setImageResource(this.imageId[position].intValue());
        return rowView;
    }
}
