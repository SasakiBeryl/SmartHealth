package elec5620.sydney.edu.au.smarthealth;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PatientMainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    EditText textDescrEditText;
    Button getRecommendButton;
    String situation = "";
    //String serPatient;
    //FirebaseUser currentPatient;
    ArrayList<Symptom> symptoms = new ArrayList<>();
    ActivityResultLauncher<Intent> viewRecommendLaucher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK ){

                }
            }
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);
        //serPatient = getIntent().getStringExtra("patient_firebase");
        //currentPatient = Tools.stringToFireBaseUser(serPatient);

        mAuth = FirebaseAuth.getInstance();

        textDescrEditText = findViewById(R.id.edittext_patient_signup_password);
        getRecommendButton = findViewById(R.id.button_patient_login);
        String jsonString = " ";
        getRecommendButton.setOnClickListener(this::onGetRecommendClick);

        Log.i("fangpei", "111");


    }


    public void onGetRecommendClick(View view)
    {
        situation = textDescrEditText.getText().toString();
        if (!situation.equals(""))
        {
            JSONObject obj= new JSONObject();
            try {
                obj.put("text",situation);
                //obj.put("text","I cannot hear anything");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Tools.getResponse(obj, this, new VolleyCallback()
            {
                @Override
                public void onSuccess(String result) {
                    JSONObject json = new JSONObject();
                    try {
                        json = new JSONObject(result);
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
                        Intent intent = new Intent(PatientMainActivity.this, ViewDoctorRecommendActivity.class);
                        intent.putExtra("symptoms", result);
                        viewRecommendLaucher.launch(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }



            });
        }

    }


    private void getResponse(JSONObject obj){
        String URL = "https://api.infermedica.com/v2/parse";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Mentions mentions = new GsonBuilder().create().fromJson(response, Mentions.class);
                        JSONObject json = new JSONObject();
                        try {
                            json = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("Check Response",json.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Check Error","Error");
                    }
                }
        ){
            @Override
            public byte[] getBody() throws AuthFailureError {
                String my_json = obj.toString();
                return my_json.getBytes();

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("Content-Type","application/json");
                map.put("Accept", "application/json");
                map.put("app_id","90f10c6a");
                map.put("app_key","9d23d9250d9f2ee8aa49efda732e4d3d");
//                map.put("sex", "male");
//                map.put("age", "21");
                map.put("text", "i feel smoach pain but no couoghing today");
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        queue.add(request);
    }

}