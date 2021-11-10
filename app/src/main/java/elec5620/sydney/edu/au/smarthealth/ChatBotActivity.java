package elec5620.sydney.edu.au.smarthealth;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.SyncParams;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
/*
Part of code below is modified based on the tutorial
on :
https://www.geeksforgeeks.org/how-to-create-a-chatbot-in-android-with-brainshop-api/
 */

public class ChatBotActivity extends AppCompatActivity {

    // creating variables for our
    // widgets in xml file.
    private RecyclerView chatsRV;
    private ImageButton sendMsgIB;
    private EditText userMsgEdt;
    private final String USER_KEY = "user";
    private final String BOT_KEY = "bot";
    private BotQuestion botQuestion;
    private final String YES = "present";
    private final String NO = "absent";
    private final String DONTKNOW = "unknown";
    private String email = "";
    boolean ENDFLAG = false;
    boolean FIRST_VALID_RESPONSE = false;

    String diseaseName="";
    String gender="";
    Integer age = 0;

    ActivityResultLauncher<Intent> mLaucher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK ){

                }
            }
    );

    JSONObject jsonDisableGroup = new JSONObject();
    String questionID = "";
    JSONArray singleEvidenceArray = new JSONArray();
    // creating a variable for
    // our volley request queue.
    private RequestQueue mRequestQueue;

    // creating a variable for array list and adapter class.
    private ArrayList<MessageModal> messageModalArrayList;
    private MessageRVAdapter messageRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        Intent i = getIntent();
        gender = getIntent().getStringExtra("gender").toLowerCase(Locale.ROOT);
        age = Integer.valueOf(getIntent().getStringExtra("age"));
        email = getIntent().getStringExtra("email");

        // on below line we are initializing all our views.
        chatsRV = findViewById(R.id.idRVChats);
        sendMsgIB = findViewById(R.id.idIBSend);
        userMsgEdt = findViewById(R.id.idEdtMessage);

        // below line is to initialize our request queue.
        mRequestQueue = Volley.newRequestQueue(this);
        mRequestQueue.getCache().clear();

        // creating a new array list
        messageModalArrayList = new ArrayList<>();

        // adding on click listener for send message button.
        sendMsgIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking if the message entered
                // by user is empty or not.
                if (userMsgEdt.getText().toString().isEmpty()) {
                    // if the edit text is empty display a toast message.
                    Toast.makeText(ChatBotActivity.this, "Please enter your message..", Toast.LENGTH_SHORT).show();
                    return;
                }

                // calling a method to send message
                // to our bot to get response.
                sendMessage(userMsgEdt.getText().toString());

                // below line we are setting text in our edit text as empty
                userMsgEdt.setText("");
            }
        });

        // on below line we are initialing our adapter class and passing our array lit to it.
        messageRVAdapter = new MessageRVAdapter(messageModalArrayList, this);

        // below line we are creating a variable for our linear layout manager.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatBotActivity.this, RecyclerView.VERTICAL, false);

        // below line is to set layout
        // manager to our recycler view.
        chatsRV.setLayoutManager(linearLayoutManager);

        // below line we are setting
        // adapter to our recycler view.
        chatsRV.setAdapter(messageRVAdapter);
    }

    private void sendMessage(String userMsg) {
        // below line is to pass message to our
        // array list which is entered by the user.
        messageModalArrayList.add(new MessageModal(userMsg, USER_KEY));
        messageRVAdapter.notifyDataSetChanged();
        if (ENDFLAG==true &&userMsg.toLowerCase(Locale.ROOT).contains("yes"))
        {
            //////////////////////////////////////////////////////////////////
                                /*
                           user choose to see the recommend specialist
                                 */
            ////////////////////////////////////////////////////////////////
            JSONObject dignosisJsonObj = new JSONObject();
            JSONObject ageJson = new JSONObject();
            try {
                ageJson.put("value",age);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //JSONArray singleEvidenceArray = new JSONArray();

            try {
                dignosisJsonObj.put("sex",gender);
                dignosisJsonObj.put("age",ageJson);
                dignosisJsonObj.put("evidence",singleEvidenceArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String url = "https://api.infermedica.com/v3/recommend_specialist";

            // creating a variable for our request queue.
            RequestQueue queue = Volley.newRequestQueue(ChatBotActivity.this);

            // on below line we are making a json object request for a get request and passing our url .
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        // in on response method we are extracting data
                        // from json response and adding this response to our array list.
                        //String botResponse = response.getString("cnt");
                        ///////////////////////////////////////////////////////
                                            /*
                                            Now we first time have the bot'question.
                                            From now on, the bot will ask couple of
                                            "Yes" and "No" questions for the user.
                                            We need to record the bot's quetion.
                                             */
                        ////////////////////////////////////////////////////////
                        JSONObject jsonObject = response.getJSONObject("recommended_specialist");
                        Log.i("special", jsonObject.toString());
                        String professionName = jsonObject.getString("name");
//                        JSONArray items = jsonObject.getJSONArray("items");
//                        questionID = items.getString(0).split(",")[0].split(":")[1];
//                        questionID = questionID.replace("\"","");

 //                       String botResponse = jsonObject.getString("text")+"\nPlease only answer Yes or No.";

                        Intent intent = new Intent(ChatBotActivity.this, ViewDoctorRecommendActivity.class);
                        intent.putExtra("recommended_profession", professionName);
                        intent.putExtra("disease",diseaseName);
                        intent.putExtra("gender", gender);
                        intent.putExtra("age", age.toString());
                        intent.putExtra("email",email);
                        finish();
                        mLaucher.launch(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();

                        // handling error response from bot.
                        messageModalArrayList.add(new MessageModal("No response", BOT_KEY));
                        messageRVAdapter.notifyDataSetChanged();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // error handling.
                    messageModalArrayList.add(new MessageModal("Sorry no response found", BOT_KEY));
                    Toast.makeText(ChatBotActivity.this, "No response from the bot..", Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                public byte[] getBody() {

                    String my_json = dignosisJsonObj.toString();
                    return my_json.getBytes();

                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("Content-Type", "application/json");
                    map.put("Accept", "application/json");
                    map.put("app_id", "90f10c6a");
                    map.put("app_key", "9d23d9250d9f2ee8aa49efda732e4d3d");

                    return map;
                }
            };

            // at last adding json object
            // request to our queue.
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));

            queue.add(jsonObjectRequest);
        }
        else if (ENDFLAG==true&&userMsg.toLowerCase(Locale.ROOT).contains("no"))
        {
            Intent intent = new Intent(ChatBotActivity.this, PatientMainActivity.class);
            intent.putExtra("gender", gender);
            intent.putExtra("age",age.toString());
            intent.putExtra("email", email);
            finish();
            mLaucher.launch(intent);
        }

        if (FIRST_VALID_RESPONSE==false)
        {


            String URL = "https://api.infermedica.com/v2/parse";
            JSONObject textObj = new JSONObject();
            ArrayList<Symptom> symptoms = new ArrayList<>();
            try {
                textObj.put("text",userMsg);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //ArrayList<Symptom> symptoms = new ArrayList<>();
            RequestQueue queue = Volley.newRequestQueue(ChatBotActivity.this);
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
                            try {
                                JSONArray jsonArray = json.getJSONArray("mentions");
                                jsonArray.get(0);
                                String info = jsonArray.toString();

                                ////////////////////////////////////////////////////////////////////
                                /*
                                We got the symptoms. next we will use the symptoms to do further
                                dignosis
                                 */
                                ///////////////////////////////////////////////////////////////////
                                /*
                                convert the mention to symptom arraylist
                                 */
                                Tools.getSymptomsFromJsonObj(json,symptoms);
                                /*
                                we got the symptom array lsit.
                                Next we need to convert the arraylist to evidence
                                 */
                                //                        JSONObject dignosisJsonObj = new JSONObject();
                                JSONObject dignosisJsonObj = new JSONObject();
                                //JSONArray singleEvidenceArray = new JSONArray();
                                JSONObject jsonDisableGroup = new JSONObject();
                                jsonDisableGroup.put("disable_groups", true);
                                for (Symptom s:symptoms)
                                {
                                    JSONObject singleEvidence = new JSONObject();
                                    String id = s.getId();
                                    String choiceId = s.getChoid_id();
                                    String source = "initial";

                                    try {
                                        singleEvidence.put("id",id);
                                        singleEvidence.put("choice_id",choiceId);
                                        singleEvidence.put("source",source);

                                        singleEvidenceArray.put(singleEvidence);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                                try {
                                    dignosisJsonObj.put("sex",gender);
                                    dignosisJsonObj.put("age",age);
                                    dignosisJsonObj.put("evidence",singleEvidenceArray);
                                    dignosisJsonObj.put("extras", jsonDisableGroup);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                //////////////////////////////////////////////////////////////////
                                /*
                                we got the evidence. next we will first time call
                                diagnosis
                                 */
                                ////////////////////////////////////////////////////////////////
                                int p = 0;
                                String url = "https://api.infermedica.com/v2/diagnosis";

                                // creating a variable for our request queue.
                                RequestQueue queue = Volley.newRequestQueue(ChatBotActivity.this);

                                // on below line we are making a json object request for a get request and passing our url .
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            // in on response method we are extracting data
                                            // from json response and adding this response to our array list.
                                            //String botResponse = response.getString("cnt");
                                            ///////////////////////////////////////////////////////
                                            /*
                                            Now we first time have the bot'question.
                                            From now on, the bot will ask couple of
                                            "Yes" and "No" questions for the user.
                                            We need to record the bot's quetion.
                                             */
                                            ////////////////////////////////////////////////////////
                                            JSONObject jsonObject = response.getJSONObject("question");
                                            JSONArray items = jsonObject.getJSONArray("items");
                                            questionID = items.getString(0).split(",")[0].split(":")[1];
                                            questionID = questionID.replace("\"","");
                                            String botResponse = jsonObject.getString("text")+"\nPlease only answer Yes or No.";
                                            FIRST_VALID_RESPONSE = true;
                                            messageModalArrayList.add(new MessageModal(botResponse, BOT_KEY));

                                            // notifying our adapter as data changed.
                                            messageRVAdapter.notifyDataSetChanged();
                                        } catch (JSONException e) {
                                            e.printStackTrace();

                                            // handling error response from bot.
                                            messageModalArrayList.add(new MessageModal("Your input is invalid. Please tell me your feeling rather than your guess of disease.", BOT_KEY));
                                            messageRVAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // error handling.
                                        messageModalArrayList.add(new MessageModal("Sorry no response found", BOT_KEY));
                                        Toast.makeText(ChatBotActivity.this, "No response from the bot..", Toast.LENGTH_SHORT).show();
                                    }
                                }){
                                    @Override
                                    public byte[] getBody() {

                                        String my_json = dignosisJsonObj.toString();
                                        return my_json.getBytes();

                                    }

                                    @Override
                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                        Map<String, String> map = new HashMap<>();
                                        map.put("Content-Type", "application/json");
                                        map.put("Accept", "application/json");
                                        map.put("app_id", "90f10c6a");
                                        map.put("app_key", "9d23d9250d9f2ee8aa49efda732e4d3d");
                                        return map;
                                    }
                                };

                                // at last adding json object
                                // request to our queue.
                                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));

                                queue.add(jsonObjectRequest);
///////////////////////////////////////End of first diagnosis//////////////////////////////////////////



                            } catch (JSONException e) {
                                messageModalArrayList.add(new MessageModal("Your input is invalid. Please tell me your feeling rather than your guess of disease.", BOT_KEY));
                                messageRVAdapter.notifyDataSetChanged();
                                e.printStackTrace();
                            }
                            //callback.onSuccess(response);
                            Log.e("Chatbot",json.toString());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            messageModalArrayList.add(new MessageModal("Your input is invalid. Please tell me your feeling rather than your guess of disease.", BOT_KEY));
                            messageRVAdapter.notifyDataSetChanged();
                            Log.e("Chatbot Check Error","Error");
                        }
                    }
            ){
                @Override
                public byte[] getBody() throws AuthFailureError {
                    String my_json = textObj.toString();
                    return my_json.getBytes();

                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> map = new HashMap<>();
                    map.put("Content-Type","application/json");
                    map.put("Accept", "application/json");
                    map.put("app_id","90f10c6a");
                    map.put("app_key","9d23d9250d9f2ee8aa49efda732e4d3d");
                    return map;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
            queue.add(request);
        }
        ////////////////////////////
        /*
        Not the first time
         */
        ///////////////////////////
        else
        {
            /*
            First, consturct the user's answer
             */
            if (userMsg.toLowerCase(Locale.ROOT).contains("yes"))
            {
                botQuestion = new BotQuestion(questionID,YES);
            }
            else if (userMsg.toLowerCase(Locale.ROOT).contains("no"))
            {
                botQuestion = new BotQuestion(questionID,NO);
            }
            else
            {
                botQuestion = new BotQuestion(questionID,DONTKNOW);
            }
            /*
            Then, add the answer to previous evidence
             */
            JSONObject dignosisJsonObj = new JSONObject();
            //JSONArray singleEvidenceArray = new JSONArray();
            JSONObject jsonDisableGroup = new JSONObject();
            try {
                jsonDisableGroup.put("disable_groups", true);
                jsonDisableGroup.put("enable_triage_5", true);
                jsonDisableGroup.put("interview_mode", "triage");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject singleEvidence = new JSONObject();
            String id = botQuestion.getId();
            String choiceId = botQuestion.getChoices();
            String source = "initial";

            try {
                singleEvidence.put("id",id);
                singleEvidence.put("choice_id",choiceId);
                singleEvidence.put("source",source);

                singleEvidenceArray.put(singleEvidence);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                dignosisJsonObj.put("sex",gender);
                dignosisJsonObj.put("age",age);
                dignosisJsonObj.put("evidence",singleEvidenceArray);
                dignosisJsonObj.put("extras", jsonDisableGroup);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //////////////////////////////////////////////////////////////////
                                /*
                                we got the new evidence. next we will first time call
                                diagnosis
                                 */
            ////////////////////////////////////////////////////////////////
            int p = 0;
            String url = "https://api.infermedica.com/v2/diagnosis";

            // creating a variable for our request queue.
            RequestQueue queue = Volley.newRequestQueue(ChatBotActivity.this);

            // on below line we are making a json object request for a get request and passing our url .
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        // in on response method we are extracting data
                        // from json response and adding this response to our array list.
                        //String botResponse = response.getString("cnt");
                        ///////////////////////////////////////////////////////
                                            /*
                                            Now we first time have the bot'question.
                                            From now on, the bot will ask couple of
                                            "Yes" and "No" questions for the user.
                                            We need to record the bot's quetion.
                                             */
                        ////////////////////////////////////////////////////////
                        JSONObject jsonObject = response.getJSONObject("question");
                        JSONArray items = jsonObject.getJSONArray("items");
                        JSONArray conditions = response.getJSONArray("conditions");
                        //String diseaseName="";
                        Double prob = 0.0;
                        Integer firstConditionSize = conditions.getString(0).split(",").length;

                        questionID = items.getString(0).split(",")[0].split(":")[1];
                        questionID = questionID.replace("\"","");

                        diseaseName = conditions.getString(0).split(",")[1].split(":")[1];
                        prob = Double.valueOf(conditions.getString(0).split(",")[firstConditionSize-1].split(":")[1].replace("}",""));
                        /*
                        If prob>50, then ask patient whether go to see recommend doctors or not.
                        if patient's answer is no, then back to previous activity.
                        if pastien's answer is yes, then go to doctor recommendation page.
                         */
                        if (prob>0.5)
                        {
                            String botResponse = "I guess you got: "+diseaseName+".";
                            ENDFLAG = true;
                            messageModalArrayList.add(new MessageModal(botResponse, BOT_KEY));
                            messageRVAdapter.notifyDataSetChanged();
                            botResponse = "Do you want to see recommend doctors?" +
                                    "\nPlease only answer Yes or No." +
                                    "\nAnswer No will back to previous page.";
                            ENDFLAG = true;
                            messageModalArrayList.add(new MessageModal(botResponse, BOT_KEY));
                            messageRVAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            String botResponse = jsonObject.getString("text")+"\nPlease only answer Yes or No.";
                            messageModalArrayList.add(new MessageModal(botResponse, BOT_KEY));
                            Log.i("Botoutput", response.toString());

                            // notifying our adapter as data changed.
                            messageRVAdapter.notifyDataSetChanged();
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();

                        // handling error response from bot.
                        messageModalArrayList.add(new MessageModal("No response", BOT_KEY));
                        messageRVAdapter.notifyDataSetChanged();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // error handling.
                    messageModalArrayList.add(new MessageModal("Sorry no response found", BOT_KEY));
                    Toast.makeText(ChatBotActivity.this, "No response from the bot..", Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                public byte[] getBody() {

                    String my_json = dignosisJsonObj.toString();
                    return my_json.getBytes();

                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("Content-Type", "application/json");
                    map.put("Accept", "application/json");
                    map.put("app_id", "90f10c6a");
                    map.put("app_key", "9d23d9250d9f2ee8aa49efda732e4d3d");
                    return map;
                }
            };

            // at last adding json object
            // request to our queue.
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));

            queue.add(jsonObjectRequest);
        }
    }

}