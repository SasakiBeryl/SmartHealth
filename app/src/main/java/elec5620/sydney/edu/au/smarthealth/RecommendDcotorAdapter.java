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
public class RecommendDcotorAdapter extends ArrayAdapter {
    private Context context;
    private List<Doctor> codeList = new ArrayList<>();

    public RecommendDcotorAdapter(@NonNull Context context, ArrayList<Doctor> codeList) {
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
        listItem = LayoutInflater.from(this.context).inflate(R.layout.recm_doctor_adapter, parent,
                false);
        Doctor currentCode = this.codeList.get(position);
        //HashMap<String, String> addresses = Tools.CoordinateToAddress(currentCode.getLatLng(),this.context);
        //String address = addresses.get("address");
        //String others = addresses.get("others");
        // Bitmap coverImage = Tools.StringToBitMap(currentCode.getCoverImage());
        //String startTime = Tools.timeToString(currentCode.startTime);
        //String endTimeStr = Tools.timeToString(currentCode.endTime);


        //ImageView viewCoverImage = (ImageView)listItem.findViewById(R.id.view_cover_image) ;
        //viewCoverImage.setImageBitmap(coverImage);

        TextView title = (TextView) listItem.findViewById(R.id.recm_dcotor_name);
        title.setText(currentCode.getFirstName()+" " +currentCode.getLastName());

        TextView spec = listItem.findViewById(R.id.recm_doctor_spec);
        spec.setText(currentCode.getSpecialization());

        TextView adddres = listItem.findViewById(R.id.recm_doctor_addr);
        adddres.setText(currentCode.getAddress());
        //TextView list_address = (TextView) listItem.findViewById(R.id.list_address);
        //list_address.setText(address);

        //TextView list_surburb = (TextView) listItem.findViewById(R.id.list_others);
        //list_surburb.setText(others);

        //TextView list_startTime = (TextView) listItem.findViewById(R.id.list_start_time);
        //list_startTime.setText(startTime);

//        TextView list_active = (TextView) listItem.findViewById(R.id.list_active);
//        //String active = currentCode.getState();   // get the task's due date
//        if(timeDiffCalculator(endTime,current_time).equals("Expired")) {
//            currentCode.setActive(false);
//            list_active.setText("Expired");
//        } else {
//            list_active.setText("Active");
//        }

        return listItem;
    }

//    public String timeDiffCalculator(Date d1, Date d2) {
//        Instant dateOneInstant = d1.toInstant();
//        ZonedDateTime zoneTimeOne = dateOneInstant.atZone(ZoneId.systemDefault());
//
//        Instant dateTwoInstant = d2.toInstant();
//        ZonedDateTime zoneTimeTwo = dateTwoInstant.atZone(ZoneId.systemDefault());
//
//        long duration = 0;
//        duration= Duration.between(zoneTimeTwo, zoneTimeOne).toMinutes();
//
//        /*
//        The below if statement responsible for the case that
//        the user input a date which is before the current date
//         */
//        if (!d1.after(d2)) {
//            duration = 0-duration;
//            String remaintime = "Expired";
//            return  remaintime;
//        }
//
//        String remainTime = duration/(24*60)+"days "+duration/60%24+"hours "+duration%60+"minutes";
//        return remainTime;
//    }
}
