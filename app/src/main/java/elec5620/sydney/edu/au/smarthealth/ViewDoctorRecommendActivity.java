package elec5620.sydney.edu.au.smarthealth;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    TextView diseaseTextView;
    ListView listViewDoctor;
    ArrayList<Doctor> doctors;
    ArrayAdapter<Doctor> adaptorDcotor;
    FirebaseFirestore db;

    Button buttonBack;

    String diseaseName = "";
    String professionName = "";
    String gender="";
    String age = "";
    String email= "";

    ActivityResultLauncher<Intent> mLaucher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK ){

                }
            }
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_doctor_recommend);

        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();
        listViewDoctor = findViewById(R.id.lisview_doctor);
        buttonBack = findViewById(R.id.view_recm_back);
        buttonBack.setOnClickListener(this::onBackClick);

        diseaseName = getIntent().getStringExtra("disease");
        professionName = getIntent().getStringExtra("recommended_profession");
        gender = getIntent().getStringExtra("gender");
        age = getIntent().getStringExtra("age");
        email = getIntent().getStringExtra("email");
        diseaseTextView = findViewById(R.id.textview_disease);

        diseaseTextView.setText("You may got: "+diseaseName);


        listViewDoctor = findViewById(R.id.lisview_doctor);
        doctors = getDoctors(new VolleyCallBackDoctor() {
            @Override
            public void onSuccess(ArrayList<Doctor> result) {
                doctors=result;
                adaptorDcotor = new RecommendDcotorAdapter(ViewDoctorRecommendActivity.this, doctors);
                listViewDoctor.setAdapter(adaptorDcotor);
            }
        });
        setupListViewListener();
        //db = ToDoTaskDB.getDatabase(getActivity().getApplication().getApplicationContext());
        //toDoTaskDao = db.toDoTaskDao();
        //readTasksFromDatabase(tasks);
        // read existing tasks from local database

        //Tools.setId(tasks.size()-1);
        //Collections.sort(tasks);


        //setUpListViewLisener();
    }

    private void onBackClick(View view)
    {
        Intent intent = new Intent(ViewDoctorRecommendActivity.this, PatientMainActivity.class);
        intent.putExtra("gender", gender);
        intent.putExtra("age",age);
        intent.putExtra("email",email);
        finish();
        mLaucher.launch(intent);
    }

    private void setupListViewListener() {
        listViewDoctor.setOnItemClickListener((parent, view, position, id) -> {
            AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
            Doctor item = doctors.get(position);
            String firstName = item.getFirstName();
            String lastName = item.getLastName();
            String email = item.getEmail();
            String addres = item.getAddress();
            String medProfession = item.getSpecialization();
            String phoneNumber = item.getPhoneNumber();
            String eventContent = "Name: " + firstName + " " +lastName+ "\n"
                    + "Profession: "+ medProfession +"\n"
                    + "Email: " + email +"\n"
                    + "Phone: " + phoneNumber + "\n"
                    + "Address: " + addres;
            normalDialog.setTitle("Specialist Info");
            normalDialog.setMessage(eventContent);
            normalDialog.setPositiveButton("OK", (dialog, which) -> {});
            normalDialog.show();
        });
    }

    public ArrayList<Doctor> getDoctors(final VolleyCallBackDoctor callback)
    {
        ArrayList<Doctor> doctors = new ArrayList<>();
        db.collection("specialists")
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
                                String profession = document.getData().get("profession").toString();
                                //String patientSymptom = symptoms.get(0).name.toLowerCase(Locale.ROOT).split(", ")[0];
                                String doctorFirstName = document.getData().get("first_name").toString();
                                String doctorLastName = document.getData().get("last_name").toString();
                                String phoneNumber = document.getData().get("phone_number").toString();
                                String address = document.getData().get("address").toString();
                                String email = document.getData().get("email").toString();

                                boolean contain = profession.toLowerCase(Locale.ROOT).contains(professionName.toLowerCase(Locale.ROOT));
                                //Log.i("checkbug", "spec: "+spec+" Patient's symp: "+patientSymptom);
                                //Log.i("checkbug", String.valueOf(contain));
                                if (contain==true)
                                {
                                    Doctor doctor = new Doctor(doctorFirstName, doctorLastName, profession, address,phoneNumber, email);
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