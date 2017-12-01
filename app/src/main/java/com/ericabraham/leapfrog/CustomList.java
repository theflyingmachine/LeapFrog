package com.ericabraham.leapfrog;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


class CustomList extends ArrayAdapter<String> {
    private final String[] task;
    private final String[] pname;
    private final String[] date;
    private final String[] address;

    private final Activity context;

    public CustomList(Activity context, String[] task, String[] pname, String[] date, String[] address) {
        super(context, R.layout.list_layout, task);
        this.context = context;
        this.task = task;
        this.pname = pname;
        this.address = address;
        this.date = date;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);
        TextView textViewTask = listViewItem.findViewById(R.id.txttask);
        TextView textViewName = listViewItem.findViewById(R.id.txtname);
        TextView textViewDate = listViewItem.findViewById(R.id.txtdate);
        TextView textViewAddress = listViewItem.findViewById(R.id.txtaddress);



        textViewTask.setText(task[position]);
        textViewName.setText(pname[position]);
        textViewDate.setText(date[position]);
        textViewAddress.setText(address[position]);

        return listViewItem;
    }

}