package elec5620.sydney.edu.au.smarthealth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class DoctorMainActivity extends AppCompatActivity {

    Button buttonDoctorViewList;
    Button buttonDoctorProvideTips;

    FirebaseFirestore DocDb;
    private FirebaseAuth mAuth;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);

        DocDb = FirebaseFirestore.getInstance();

        buttonDoctorProvideTips = (Button)findViewById(R.id.button_doctor_provide_tips);
        buttonDoctorViewList = (Button)findViewById(R.id.button_doctor_view_list);

        buttonDoctorProvideTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(DoctorMainActivity.this,DoctorTipsActivity.class);
                startActivity(intent);
            }
        });

        buttonDoctorViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(DoctorMainActivity.this,DoctorViewActivity.class);
                startActivity(intent);
            }
        });
    }

    private void onDoctorProvideTipsClick(View view) {
    }

    private void onDoctorViewListClick(View view) {
    }
}