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


/**
 * Customized ArrayAdapter in order to show the task title and remaining time
 * in two columns
 **/
public class PatientListAdapter extends ArrayAdapter {
    private Context context;
    private List<Patient> codeList = new ArrayList<>();

    public PatientListAdapter(@NonNull Context context, ArrayList<Patient> codeList) {
        super(context, 0, codeList);
        this.context = context;
        this.codeList = codeList;
    }

    /**
     * fulfill the customized arrayadapter with task titles and remianing time
     * Each time the adapter is called, e.g. notifyDataSetChanged,
     * the View method will be called and the remaining time will be updated
     **/
    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        Date current_time = new Date();
        View listItem = convertView;
        listItem = LayoutInflater.from(this.context).inflate(R.layout.patient_list_adapter, parent,
                false);
        Patient currentCode = this.codeList.get(position);

        TextView name = (TextView) listItem.findViewById(R.id.patient_name);
        name.setText(currentCode.getFirstName()+" " +currentCode.getLastName());

        TextView gender = listItem.findViewById(R.id.patient_gender);
        gender.setText(currentCode.getGender());

        TextView age = listItem.findViewById(R.id.patient_age);
        age.setText(currentCode.getAge());


        return listItem;
    }


}
