package elec5620.sydney.edu.au.smarthealth;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {



    Button buttonPatientLogin;
    Button buttonPatientSignup;
    Button buttonDoctorSignup;
    Button buttonDoctorSignin;

    ActivityResultLauncher<Intent> mLaucher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK ){

                }
            }
    );
    ActivityResultLauncher<Intent> doctorSignupLaucher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK ){

                }
            }
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        buttonPatientLogin = findViewById(R.id.button_patient_login);
        buttonPatientSignup = findViewById(R.id.button_patient_signup);
        buttonDoctorSignin = findViewById(R.id.button_doctor_login);
        buttonDoctorSignup = findViewById(R.id.button_patient_confirm);

        buttonPatientLogin.setOnClickListener(this::onPatientSignInClick);
        buttonPatientSignup.setOnClickListener(this::onPatientSignUpClick);

        buttonDoctorSignup.setOnClickListener(this::onDoctorSignupClick);
        buttonDoctorSignin.setOnClickListener(this::onDoctorSignInClick);

        Log.i("fangpei", "111");


    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//    }

    public void onPatientSignInClick(View view)
    {

        Intent intent = new Intent(MainActivity.this, PatientSignInActivity.class);
        finish();
        mLaucher.launch(intent);

    }

    public void onPatientSignUpClick(View view)
    {

        Intent intent = new Intent(MainActivity.this, PatientSignUpActivity.class);
        mLaucher.launch(intent);

    }

    public void onDoctorSignInClick(View view)
    {
        Intent intent = new Intent(MainActivity.this, DoctorSignInActivity.class);
        finish();
        mLaucher.launch(intent);

    }

    public void onDoctorSignupClick(View view)
    {
        Intent intent = new Intent(MainActivity.this, DoctorSignUpActivity.class);
        finish();
        mLaucher.launch(intent);

    }




}