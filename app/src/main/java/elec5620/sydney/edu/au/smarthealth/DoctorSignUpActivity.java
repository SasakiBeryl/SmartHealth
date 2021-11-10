package elec5620.sydney.edu.au.smarthealth;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wx.wheelview.widget.WheelViewDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DoctorSignUpActivity extends AppCompatActivity {
    EditText editTextDoctorEmail;
    EditText editTextDoctorPassword;
    //EditText editTextDoctorSpec;
    TextView editTextProfession;
    EditText editTextFirstName;
    EditText editTextLastName;
    EditText editTextPhoneNumber;
    EditText editTextAddress;
    Button buttonDoctorCreatAccount;
    Button buttonBack;
    String email;
    String address;
    String phoneNumber;
    String password;
    String firstName;
    String lastName;
    //String spec;
    String profession;
    ArrayList<String> arrayListProf= new ArrayList<>();


    FirebaseFirestore db;
    private FirebaseAuth mAuth;
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
        setContentView(R.layout.activity_doctor_sign_up);

        // Access a Cloud Firestore instance from your Activity

        db= FirebaseFirestore.getInstance();

        editTextDoctorEmail = findViewById(R.id.edittext_doctor_signup_email);
        editTextPhoneNumber = findViewById(R.id.edittext_doctor_signup_phone);
        editTextDoctorPassword = findViewById(R.id.edittext_doctor_signup_password);
        editTextFirstName = findViewById(R.id.edittext_doctor_first_name);
        editTextLastName = findViewById(R.id.edittext_doctor_last_name);
        //editTextDoctorSpec = findViewById(R.id.edittext_doctor_signup_prof);
        editTextProfession = findViewById(R.id.edittext_doctor_signup_prof);
        editTextProfession.setOnClickListener(this::onProfessionClick);
        editTextAddress = findViewById(R.id.edittext_doctor_address);
        buttonBack = findViewById(R.id.button_doctor_signup_back);
        buttonBack.setOnClickListener(this::onBackClick);

        buttonDoctorCreatAccount = findViewById(R.id.button_doctor_confirm);

        buttonDoctorCreatAccount.setOnClickListener(this::onDoctorCreateClick);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        String profList =
                "Allergologist," +
                "Angiologist," +
                "Cardiologist," +
                "Dentist," +
                "Dermatologist," +
                "Diabetologist," +
                "Endocrinologist," +
                "ENT doctor," +
                "Gastroenterologist," +
                "General Practitioner," +
                "Gynecologist," +
                "Hematologist," +
                "Infectiologist," +
                "Internal Diseases Specialist," +
                "Nephrologist," +
                "Neurologist," +
                "Oncologist," +
                "Ophthalmologist," +
                "Pediatrician," +
                "Psychiatrist," +
                "Pulmonologist," +
                "Rheumatologist," +
                "Surgeon," +
                "Toxicologist," +
                "Urologist";
        for(String s: profList.split(","))
        {
            arrayListProf.add(s);
        }


    }
    public void onDoctorCreateClick(View view)
    {
        creatAccount();


    }



    public void creatAccount()
    {
        email = editTextDoctorEmail.getText().toString();
        phoneNumber = editTextPhoneNumber.getText().toString();
        password = editTextDoctorPassword.getText().toString();
        firstName = editTextFirstName.getText().toString();
        lastName = editTextLastName.getText().toString();
        //spec = editTextDoctorSpec.getText().toString();
        address = editTextAddress.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("fangpei", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            // Create a new user with a first and last name
                            Map<String, Object> doctor = new HashMap<>();
                            doctor.put("email", email);
                            doctor.put("phone_number",phoneNumber);
                            doctor.put("first_name", firstName);
                            doctor.put("last_name", lastName);
                            //doctor.put("specialization", spec);
                            doctor.put("profession", profession);
                            doctor.put("address", address);

// Add a new document with a generated ID
                            db.collection("specialists")
                                    .add(doctor)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d("fangpei", "DocumentSnapshot added with ID: " + documentReference.getId());
                                            finish();
                                            Intent intent = new Intent(DoctorSignUpActivity.this,MainActivity.class);
                                            mLaucher.launch(intent);

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("fangpei", "Error adding document", e);
                                        }
                                    });

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("fangpei", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(DoctorSignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });





    }

    public void onProfessionClick(View view)
    {
        WheelViewDialog dialog = new WheelViewDialog(this);
        dialog.setTitle("Professions")
                .setItems(arrayListProf)
                .setDialogStyle(Color.parseColor("#6699ff"))
                .setCount(3)
                .setLoop(true)
                .setButtonText("Confirm")
                .setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener() {
                    @Override
                    public void onItemClick(int position, String s) {
                        profession = s;
                        editTextProfession.setText(profession);
                    }
                }).show();

    }

    public void onBackClick(View view)
    {
        Intent intent = new Intent(DoctorSignUpActivity.this,MainActivity.class);
        finish();
        mLaucher.launch(intent);
    }


}