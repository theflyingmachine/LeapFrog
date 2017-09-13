package com.ericabraham.leapfrog;

/**
 * Created by Eric Abraham on 13-Sep-17.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Belal on 7/22/2015.
 */
public class CustomList extends ArrayAdapter<String> {
    private String[] lat;
    private String[] longi;
    private String[] task;

    private Activity context;

    public CustomList(Activity context, String[] lat, String[] longi, String[] task) {
        super(context, R.layout.list_layout, lat);
        this.context = context;
        this.lat = lat;
        this.longi = longi;
        this.task = task;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewLat = (TextView) listViewItem.findViewById(R.id.textViewlat);
        TextView textViewLong = (TextView) listViewItem.findViewById(R.id.textViewlong);
        String pos = new Integer(position).toString();


        textViewLong.setText(lat[position]);

        textViewLat.setText(longi[position]);

        textViewName.setText(task[position]);

        return  listViewItem;
    }


}