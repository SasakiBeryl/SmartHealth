package elec5620.sydney.edu.au.smarthealth;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import elec5620.sydney.edu.au.smarthealth.database.ToDoTask;
import elec5620.sydney.edu.au.smarthealth.database.ToDoTaskDB;
import elec5620.sydney.edu.au.smarthealth.database.ToDoTaskDao;

public class PatientMainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    EditText textDescrEditText;
    Button buttonChatBot;
    String situation = "";
    String gender="";
    String age = "";
    String email="";
    ///////////////////////////
    ListView taskListView;
    ArrayAdapter<Task> taskArrayAdapter;
    ArrayList<Task> tasks;
    Button buttonNewTask;
    ToDoTaskDB db;
    ToDoTaskDao toDoTaskDao;
    ActivityResultLauncher<Intent> mLaucher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK ){
                    String ser_task = result.getData().getExtras().getString("task");
                    Task editedTask = Tools.stringToTask((ser_task));

                    int position = result.getData().getIntExtra("position", -1);
                    String newItemChecker = result.getData().getStringExtra("newItem");
                    if(newItemChecker.compareTo("false")==0){
                        tasks.set(position, editedTask);
                        Collections.sort(tasks);
                        taskArrayAdapter.notifyDataSetChanged();
                        saveTasksToDatabase();

                    }
                    else
                    {
                        tasks.add(editedTask);
                        Collections.sort(tasks);
                        taskArrayAdapter.notifyDataSetChanged();

                        saveTasksToDatabase();

                    }

                }
            }
    );
    //////////////
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

        gender= getIntent().getStringExtra("gender");
        age = getIntent().getStringExtra("age");
        email = getIntent().getStringExtra("email");

        textDescrEditText = findViewById(R.id.edittext_patient_signup_password);
        buttonChatBot=findViewById(R.id.button_chatbot);
        buttonChatBot.setOnClickListener(this::onChatbotClick);
        String jsonString = " ";
        ///////////////////////////////
        buttonNewTask = findViewById(R.id.addNew);
        buttonNewTask.setOnClickListener(this::onNewTaskCLick);
        taskListView = findViewById(R.id.taskListView);
        tasks = new ArrayList<Task>();
        db = ToDoTaskDB.getDatabase(this.getApplication().getApplicationContext());
        toDoTaskDao = db.toDoTaskDao();
        readTasksFromDatabase(tasks);   // read existing tasks from local database
        Collections.sort(tasks);    // sort tasks accoding to date
        taskArrayAdapter = new CustomArrayAdapter(this, tasks);
        taskListView.setAdapter(taskArrayAdapter);
        setUpListViewLisener();
        ///////////////////////////////

        Log.i("fangpei", "111");


    }

    public void onChatbotClick(View view)
    {
        Intent intent = new Intent(PatientMainActivity.this, ChatBotActivity.class);
        intent.putExtra("gender",gender);
        intent.putExtra("age", age);
        intent.putExtra("email", email);
        finish();
        viewRecommendLaucher.launch(intent);
    }

    public void onNewTaskCLick(View view){
        Intent intent = new Intent(PatientMainActivity.this, TaskEditActivity.class);
        if (intent!=null){
            intent.putExtra("newItem","true");
            intent.putExtra("position", tasks.size());
            intent.putExtra("email", email);
            mLaucher.launch(intent);

            taskArrayAdapter.notifyDataSetChanged();

        }

    }

    /*
    setup the long click listener and click listener on licview.
     */
    public void setUpListViewLisener(){
        taskListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position,
                                           long rowId) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PatientMainActivity.this);
                builder.setTitle("Delete a task")
                        .setMessage("Do you want to delete this task?")
                        .setPositiveButton("Delete", new
                                DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        tasks.remove(position);
                                        taskArrayAdapter.notifyDataSetChanged();

                                        saveTasksToDatabase();
                                    }
                                })
                        .setNegativeButton("Cancel", new
                                DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                builder.create().show();
                return true;
            }
        });
        /*
        if user click any of the task, then jump to
        TaskEdit view and pass the current task information
         */
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Task updateTask = taskArrayAdapter.getItem(position);   // get the clicked task
                String ser_task = Tools.taskToString(updateTask);    // serialize the task to string
                Intent intent = new Intent(PatientMainActivity.this, TaskEditActivity.class);
                if (intent != null){
                    intent.putExtra("newItem", "false");
                    intent.putExtra("task", ser_task);
                    intent.putExtra("position", position);
                    intent.putExtra("email",email);
                    mLaucher.launch(intent);
                    taskArrayAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /*
    Read data fron local database
     */
    private void readTasksFromDatabase(ArrayList<Task> tasks) {
        try{
            CompletableFuture<Void> future = CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {
                    List<ToDoTask> taskFromDB = toDoTaskDao.listAll();
                    if (taskFromDB != null & taskFromDB.size()>0){
                        for (ToDoTask task : taskFromDB){
                            Task newTask = Tools.stringToTask(task.getToDoTaskContent());
                            if (newTask.getUserEmail().equals(email))
                            {
                                tasks.add(newTask);
                            }

                        }
                    }
                }
            });

            future.get();
        }
        catch (Exception ex) {
            Log.e("readItemsFromDatabase", ex.getStackTrace().toString());
        }
    }
    /*
    save task data to local databse
     */
    private void saveTasksToDatabase(){
        try{
            CompletableFuture<Void> future = CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {
                    toDoTaskDao.deleteAll();
                    for (Task task : tasks) {
//                        ToDoTask content = new ToDoTask(taskToString(task));
                        ToDoTask content = new ToDoTask(Tools.taskToString(task));
                        toDoTaskDao.insert(content);
                    }
                }
            });
            future.get();
        }
        catch (Exception ex){

        }
    }




}