package elec5620.sydney.edu.au.smarthealth;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;


public class PatientSignInActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText editTextEmail;
    EditText editTextPassword;
    Button buttonConfirm;
    Button buttonBack;
    String email;
    String password;
    String gender;
    String age;
    FirebaseFirestore db;
    private boolean EMAIL_CHECK=false;
    ActivityResultLauncher<Intent> patientMainLaucher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK ){

                }
            }
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_sign_in);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.edittext_patient_signup_email);
        editTextPassword = findViewById(R.id.edittext_patient_signup_phone);
        buttonConfirm = findViewById(R.id.button_patient_confirm);
        buttonBack = findViewById(R.id.button_patient_back);

        buttonConfirm.setOnClickListener(this::onPatientConfirmClick);
        buttonBack.setOnClickListener(this::onPatientBackClick);



    }
    public void onPatientBackClick(View view)
    {
        Intent intent = new Intent(PatientSignInActivity.this,MainActivity.class);
        finish();
        patientMainLaucher.launch(intent);
    }
    public void onPatientConfirmClick(View view)
    {
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();
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
                                    EMAIL_CHECK = true;
                                    break;
                                }


                            }
                        } else {
                            Log.d("fangpei", "89");
                            Log.w("fangpei", "Error getting documents.", task.getException());
                        }
                    }
                });

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() && EMAIL_CHECK==true) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("fangpei", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("fangpei", "signInWithEmail:failure", task.getException());
                            Toast.makeText(PatientSignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });


    }

    private void updateUI(FirebaseUser user)
    {
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


                                if (userEmail.equals(email))
                                {
                                    gender = document.getData().get("gender").toString();
                                    age = document.getData().get("age").toString();
                                    break;
                                }


                            }
                            Intent intent = new Intent(PatientSignInActivity.this, PatientMainActivity.class);
                            //String ser_patient = Tools.firebaseUserToString(user);
                            intent.putExtra("gender", gender);
                            intent.putExtra("age",age);
                            intent.putExtra("email",email);
                            finish();
                            patientMainLaucher.launch(intent);
                        } else {
                            Log.d("fangpei", "89");
                            Log.w("fangpei", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

}