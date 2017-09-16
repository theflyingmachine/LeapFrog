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
    private String[] task;
    private String[] pname;
    private String[] date;
    private String[] address;
    private int[] id;

    private Activity context;

    public CustomList(Activity context, String[] task, String[] pname, String[] date, String[] address, int[] id) {
        super(context, R.layout.list_layout, task);
        this.context = context;
        this.task = task;
        this.pname = pname;
        this.address = address;
        this.date = date;
        this.id = id;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);
        TextView textViewTask = (TextView) listViewItem.findViewById(R.id.txttask);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.txtname);
        TextView textViewDate = (TextView) listViewItem.findViewById(R.id.txtdate);
        TextView textViewAddress = (TextView) listViewItem.findViewById(R.id.txtaddress);
        String pos = new Integer(position).toString();


        textViewTask.setText(task[position]);
        textViewName.setText(pname[position]);
        textViewDate.setText(date[position]);
        textViewAddress.setText(address[position]);

        return  listViewItem;
    }

}