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


public class PatientSignUpActivity extends AppCompatActivity {
    EditText editTextPatientEmail;
    EditText editTextPatientPassword;
    EditText editTextFirstName;
    EditText editTextLastName;
    EditText editTextPhoneNumber;
    TextView editTextPatientGender;
    TextView editTextPatientAge;

    Button buttonDoctorCreatAccount;

    String email;
    String phoneNumber;
    String password;
    String firstName;
    String lastName;
    String gender;
    String age;

    ArrayList<String> arrayListGender= new ArrayList<>();
    ArrayList<String> arrayListAge= new ArrayList<>();

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
        setContentView(R.layout.activity_patient_sign_up);

        db= FirebaseFirestore.getInstance();

        editTextPatientEmail = findViewById(R.id.edittext_patient_signup_email);
        editTextPhoneNumber = findViewById(R.id.edittext_patient_signup_phone);
        editTextPatientPassword = findViewById(R.id.edittext_patient_signup_password);
        editTextFirstName = findViewById(R.id.edittext_patient_first_name);
        editTextLastName = findViewById(R.id.edittext_patient_last_name);
        editTextPatientGender = findViewById(R.id.edittext_patient_gender);
        editTextPatientGender.setOnClickListener(this::onGenderClick);
        editTextPatientAge = findViewById(R.id.edittext_patient_age);
        editTextPatientAge.setOnClickListener(this::onAgeClick);

        arrayListGender.add("Male");
        arrayListGender.add("Female");
        for (int i=0;i<150;i++)
        {
            arrayListAge.add(Integer.toString(i));
        }

        buttonDoctorCreatAccount = findViewById(R.id.button_patient_confirm);
        buttonDoctorCreatAccount.setOnClickListener(this::onDoctorCreateClick);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    public void onDoctorCreateClick(View view)
    {
        creatAccount();


    }



    public void creatAccount()
    {
        email = editTextPatientEmail.getText().toString();
        phoneNumber = editTextPhoneNumber.getText().toString();
        password = editTextPatientPassword.getText().toString();
        firstName = editTextFirstName.getText().toString();
        lastName = editTextLastName.getText().toString();
        gender = editTextPatientGender.getText().toString();
        age = editTextPatientAge.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("fangpei", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            // Create a new user with a first and last name
                            Map<String, Object> patient = new HashMap<>();
                            patient.put("email", email);
                            patient.put("phone_number",phoneNumber);
                            patient.put("first_name", firstName);
                            patient.put("last_name", lastName);
                            patient.put("gender", gender);
                            patient.put("age", age);
                            patient.put("tips","");


// Add a new document with a generated ID
                            db.collection("patients")
                                    .add(patient)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d("fangpei", "DocumentSnapshot added with ID: " + documentReference.getId());
                                            finish();
                                            Intent intent = new Intent(PatientSignUpActivity.this,MainActivity.class);
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
                            Toast.makeText(PatientSignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });





    }

    public void onGenderClick(View view)
    {
        WheelViewDialog dialog = new WheelViewDialog(this);
        dialog.setTitle("Gender")
                .setItems(arrayListGender)
                .setDialogStyle(Color.parseColor("#6699ff"))
                .setCount(3)
                .setLoop(true)
                .setButtonText("Confirm")
                .setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener() {
                    @Override
                    public void onItemClick(int position, String s) {
                        gender = s;
                        editTextPatientGender.setText(gender);
                    }
                }).show();

    }

    public void onAgeClick(View view)
    {
        WheelViewDialog dialog = new WheelViewDialog(this);
        dialog.setTitle("Age")
                .setItems(arrayListAge)
                .setDialogStyle(Color.parseColor("#6699ff"))
                .setCount(3)
                .setLoop(true)
                .setButtonText("Confirm")
                .setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener() {
                    @Override
                    public void onItemClick(int position, String s) {
                        age = s;
                        editTextPatientAge.setText(age);
                    }
                }).show();

    }
}