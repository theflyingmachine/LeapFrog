package com.ericabraham.leapfrog.Adapter;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.ericabraham.leapfrog.Ui.ManageTask;
import com.ericabraham.leapfrog.R;

public class ReminderListAdapter extends RecyclerView.Adapter<ReminderListAdapter.ViewHolder> {
  private int[] taskID;
  private String[] task;
  private String[] pname;
  private String[] date;
  private String[] address;

  private final Activity context;

  public ReminderListAdapter(Activity context, int[] taskID, String[] task, String[] pname, String[] date,
      String[] address) {
    this.context = context;
    this.taskID = taskID;
    this.task = task;
    this.pname = pname;
    this.address = address;
    this.date = date;
  }

  @NonNull @Override
  public ReminderListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    // Inflate the custom layout
    View contactView = LayoutInflater.from(context).inflate(R.layout.item_single_reminder, viewGroup, false);

    // Return a new holder instance
    ViewHolder viewHolder = new ViewHolder(contactView);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull ReminderListAdapter.ViewHolder viewHolder, final int position) {
    viewHolder.textViewTask.setText(task[position]);
    viewHolder.textViewName.setText(pname[position]);
    viewHolder.textViewDate.setText(date[position]);
    viewHolder.textViewAddress.setText(address[position]);
    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent = new Intent(context, ManageTask.class);
        intent.putExtra("taskID", taskID[position]);
        context.startActivity(intent);
        context.finish();
      }
    });
  }

  @Override public int getItemCount() {
    return task.length;
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.txttask)
    TextView textViewTask;
    @BindView(R.id.txtname)
    TextView textViewName;
    @BindView(R.id.txtdate)
    TextView textViewDate;
    @BindView(R.id.txtaddress)
    TextView textViewAddress;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
