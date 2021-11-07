package elec5620.sydney.edu.au.smarthealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;


public class ViewDoctorRecommendActivity extends AppCompatActivity {
    TextView symptomTextView;
    String serSymptoms;
    ListView listViewDoctor;
    ArrayList<Symptom> symptoms = new ArrayList<>();
    ArrayList<Doctor> doctors;
    ArrayAdapter<Doctor> adaptorDcotor;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_doctor_recommend);
        // Access a Cloud Firestore instance from your Activity

        db = FirebaseFirestore.getInstance();

        listViewDoctor = findViewById(R.id.lisview_doctor);

        serSymptoms = getIntent().getStringExtra("symptoms");
        symptomTextView = findViewById(R.id.textview_symptom);
        //symptomTextView.setText();
        JSONObject json = new JSONObject();
        try {
            json = new JSONObject(serSymptoms);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONArray jsonArray = json.getJSONArray("mentions");

            //HashMap<String, String> symptom = new HashMap<>();

            for (int i=0; i< jsonArray.length();i++)
            {
                Symptom symptom = new Symptom();
                String id = jsonArray.getJSONObject(i).getString("id");
                String orth = jsonArray.getJSONObject(i).getString("orth");
                String choice_id = jsonArray.getJSONObject(i).getString("choice_id");
                String name = jsonArray.getJSONObject(i).getString("name");
                String common_name = jsonArray.getJSONObject(i).getString("common_name");
                String type = jsonArray.getJSONObject(i).getString("type");
                symptom.setId(id);
                symptom.setOrth(orth);
                symptom.setChoid_id(choice_id);
                symptom.setName(name);
                symptom.setCommon_name(common_name);
                symptom.setType(type);
                symptoms.add(symptom);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        symptomTextView.setText("Your sympotoms are: "+symptoms.get(0).name);


        listViewDoctor = findViewById(R.id.lisview_doctor);
        doctors = getDoctors(new VolleyCallBackDoctor() {
            @Override
            public void onSuccess(ArrayList<Doctor> result) {
                doctors=result;
                adaptorDcotor = new RecommendDcotorAdapter(ViewDoctorRecommendActivity.this, doctors);
                listViewDoctor.setAdapter(adaptorDcotor);
            }
        });
        //db = ToDoTaskDB.getDatabase(getActivity().getApplication().getApplicationContext());
        //toDoTaskDao = db.toDoTaskDao();
        //readTasksFromDatabase(tasks);
        // read existing tasks from local database

        //Tools.setId(tasks.size()-1);
        //Collections.sort(tasks);


        //setUpListViewLisener();
    }

    public ArrayList<Doctor> getDoctors(final VolleyCallBackDoctor callback)
    {
        ArrayList<Doctor> doctors = new ArrayList<>();
        db.collection("doctors")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                //Log.d("fangpei", "85");
                                //Log.d("fangpei", document.getId() + " => " + document.getData().get("name"));
                                String spec = document.getData().get("specialization").toString().toLowerCase(Locale.ROOT);
                                String patientSymptom = symptoms.get(0).name.toLowerCase(Locale.ROOT).split(", ")[0];
                                String doctorFirstName = document.getData().get("first_name").toString();
                                String doctorLastName = document.getData().get("last_name").toString();
                                String phoneNumber = document.getData().get("phone_number").toString();
                                String address = document.getData().get("address").toString();

                                boolean contain = spec.contains(patientSymptom);
                                //Log.i("checkbug", "spec: "+spec+" Patient's symp: "+patientSymptom);
                                //Log.i("checkbug", String.valueOf(contain));
                                if (contain==true)
                                {
                                    Doctor doctor = new Doctor(doctorFirstName, doctorLastName, spec, address,phoneNumber);
                                    doctors.add(doctor);
                                }

                            }
                            callback.onSuccess(doctors);
                        } else {
                            Log.d("fangpei", "89");
                            Log.w("fangpei", "Error getting documents.", task.getException());
                        }
                    }
                });
        return doctors;
    }
}