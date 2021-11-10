package elec5620.sydney.edu.au.smarthealth;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DoctorTipsActivity extends AppCompatActivity {


    ListView ListViewPatient;
    FirebaseFirestore db;
    //    private DatabaseReference mDatabase;
    ArrayList<Patient> patients;
    ArrayAdapter<Patient> adaptorPatient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_tips);

        db = FirebaseFirestore.getInstance();
        ListViewPatient = findViewById(R.id.listview_patient);

        patients = getPatientsList(new VolleyCallBackPatient() {
            public void onSuccess(ArrayList<Patient> result) {
                patients=result;
                adaptorPatient = new PatientListAdapter(DoctorTipsActivity.this, patients);
                ListViewPatient.setAdapter(adaptorPatient);
            }
        });
        setupTipsListener();
    }

    private void setupTipsListener() {
        ListViewPatient.setOnItemClickListener((parent, view, position, id) -> {
            EditText tips = new EditText(this);
            Patient item = patients.get(position);

            String firstName = item.getFirstName();
            String lastName = item.getLastName();
            String email = item.getEmail();
            String gender = item.getGender();
            String age = item.getAge();
            String phoneNumber = item.getPhoneNumber();

            AlertDialog.Builder tipsDialog = new AlertDialog.Builder(this);
            tipsDialog.setTitle("Enter the tips for " + firstName+ " " +lastName + ":");
            tipsDialog.setIcon(android.R.drawable.ic_dialog_info);
            tipsDialog.setView(tips);
            tipsDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String tipsForPatient= tips.getText().toString();
                    createSinglePatientTips(tipsForPatient,email);

                    //////////////////////////////////////////////
                    /*
                    find the patient's document id
                     */
                    //////////////////////////////////////////////
                    db.collection("patients")
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
                                            String userEmail = document.getData().get("email").toString();
                                            //String documentId = document.getId();


                                            if (userEmail.equals(email))
                                            {
                                                String documentID = document.getId();
                                                DocumentReference washingtonRef = db.collection("patients").document(documentID);

                                                washingtonRef
                                                        .update("tips", tipsForPatient)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                            }
                                                        });
                                                break;
                                            }


                                        }
                                    } else {
                                        Log.d("fangpei", "89");
                                        Log.w("fangpei", "Error getting documents.", task.getException());
                                    }
                                }
                            });
                    ///////////////////////////////////////////////////////////////////////////////////////////////
                    /*
                    End of update
                     */
                    ///////////////////////////////////////////////////////////////////////////////////////////////
                    //adaptorPatient.notifyDataSetChanged();
                    //dialog.notify();
                }
            });
            tipsDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

        tipsDialog.show();
        });
    }

    private void createSinglePatientTips(String tips,String email) {
        CollectionReference patientsRef = db.collection("patients");
        Query query = patientsRef.whereEqualTo("email", email);

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
