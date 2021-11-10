package elec5620.sydney.edu.au.smarthealth;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/*
Customized ArrayAdapter in order to show the task title and remaining time
in two columns
 */
public class CustomArrayAdapter extends ArrayAdapter {
    private Context context;
    private List<Task> taskList = new ArrayList<>();
    public CustomArrayAdapter(@NonNull Context context, ArrayList<Task> taskList) {
        super(context, 0, taskList);
        this.context = context;
        this.taskList = taskList;
    }
    /*
    fulfill the customized arrayadapter with task titles and remianing time
    Each time the adapter is called, e.g. notifyDataSetChanged,
    the View method will be called and the remaining time will be updated
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        listItem = LayoutInflater.from(this.context).inflate(R.layout.custom_listview, parent,
                false);
        Task currentTask = this.taskList.get(position);

        TextView title = (TextView) listItem.findViewById(R.id.custom_adapter_title);
        title.setText(currentTask.getTitle());

        TextView dateText = (TextView) listItem.findViewById(R.id.custom_adapter_time);
        Date due_date = currentTask.getDueDate();   // get the task's due date
        Date current_date = new Date(); // get the current time
        String timeDiff = Tools.timeDiffCalculator(due_date, current_date);  //calculate remaining time
        dateText.setText(timeDiff);

        return listItem;
    }


}