package elec5620.sydney.edu.au.smarthealth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;

public class DoctorViewActivity extends AppCompatActivity {
    ListView ListViewPatient;
    FirebaseFirestore db;
//    private DatabaseReference mDatabase;
    ArrayList<Patient> patients;
    ArrayAdapter<Patient> adaptorPatient;


//    ActivityResultLauncher<Intent> mLaucher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            result -> {
//                if (result.getResultCode() == RESULT_OK ){
//
//                }
//            }
//    );

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view_list);

        db = FirebaseFirestore.getInstance();
//        mDatabase = FirebaseDatabase.getInstance().getReference();

        ListViewPatient = findViewById(R.id.listview_patient);

        patients = getPatientsList(new VolleyCallBackPatient() {
            public void onSuccess(ArrayList<Patient> result) {
                patients=result;
                adaptorPatient = new PatientListAdapter(DoctorViewActivity.this, patients);
                ListViewPatient.setAdapter(adaptorPatient);
            }
        });
        setupListViewListener();
        
    }

    private void setupListViewListener() {
        ListViewPatient.setOnItemClickListener((parent, view, position, id) -> {
            AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
            Patient item = patients.get(position);
            String firstName = item.getFirstName();
            String lastName = item.getLastName();
            String email = item.getEmail();
            String gender = item.getGender();
            String age = item.getAge();
            String phoneNumber = item.getPhoneNumber();
            String tips = item.getTips();
            if (tips.equals(null))
            {
                tips = "";
            }
            String detailsContent = "Name: " + firstName + " " +lastName+ "\n"
                    + "age: "+ age +"\n"
                    + "gender: " + gender +"\n"
                    + "email: " + email + "\n"
                    + "PhoneNumber: " + phoneNumber + "\n"
                    + "Tips:" + tips;
            normalDialog.setTitle("Patient Details");
            normalDialog.setMessage(detailsContent);
            normalDialog.setPositiveButton("Close", (dialog, which) -> {});
            normalDialog.show();
        });
    }

    public ArrayList<Patient> getPatientsList(final VolleyCallBackPatient callback)
    {
        ArrayList<Patient> patients = new ArrayList<>();
        db.collection("patients")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                String patientFirstName = document.getData().get("first_name").toString();
                                String patientLastName = document.getData().get("last_name").toString();
                                String age = document.getData().get("age").toString();
                                String gender = document.getData().get("gender").toString();
                                String patientPhoneNumber = document.getData().get("phone_number").toString();
                                String patientEmail = document.getData().get("email").toString();
                                String patientTips = document.getData().get("tips").toString();

                                Patient patient = new Patient(patientFirstName, patientLastName, patientEmail, patientPhoneNumber, age, gender,patientTips);
                                patients.add(patient);

                            }
                            callback.onSuccess(patients);
                        } else {
                            Log.d("yujing", "91");
                            Log.w("yujing", "Error getting patient list.", task.getException());
                        }
                    }
                });
        return patients;
    }


}
