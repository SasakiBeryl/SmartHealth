package elec5620.sydney.edu.au.smarthealth;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Tools {
    public static void getNLPSymptoms(JSONObject obj, Context context, final VolleyCallback callback){
        String URL = "https://api.infermedica.com/v2/parse";
        //ArrayList<Symptom> symptoms = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(context);
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
                            int p = 0;

                            //HashMap<String, String> symptom = new HashMap<>();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callback.onSuccess(response);
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
                //map.put("text", "i feel smoach pain but no couoghing today");
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        queue.add(request);
        //return symptoms;
    }



    public static void getDiagnosis(JSONObject obj, Context context, final VolleyCallback callback) {
        String URL = "https://api.infermedica.com/v2/diagnosis";
        //ArrayList<Symptom> symptoms = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(context);
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
                            JSONObject jsonObject = json.getJSONObject("question");
                            //jsonArray.get(0);
                            JSONObject e = jsonObject;
                            String text = e.getString("text");
                            int q = 5;

                            //HashMap<String, String> symptom = new HashMap<>();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callback.onSuccess(response);
                        Log.e("Check Response", json.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Check Error", "Error");
                    }
                }
        ) {
            @Override
            public byte[] getBody() throws AuthFailureError {

                String my_json = obj.toString();
                return my_json.getBytes();

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json");
                map.put("Accept", "application/json");
                map.put("app_id", "90f10c6a");
                map.put("app_key", "9d23d9250d9f2ee8aa49efda732e4d3d");
//                map.put("sex", "male");
//                map.put("age", "21");
                //map.put("text", "i feel smoach pain but no couoghing today");
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        queue.add(request);
        //return symptoms;
    }
    /*

     */
    public static void getSymptomsFromJsonObj(JSONObject jsonObject, ArrayList<Symptom> symptoms)
    {

        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = jsonObject.getJSONArray("mentions");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i=0; i< jsonArray.length();i++) {
            Symptom symptom = new Symptom();
            String id = null;
            try {
                id = jsonArray.getJSONObject(i).getString("id");
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
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}
