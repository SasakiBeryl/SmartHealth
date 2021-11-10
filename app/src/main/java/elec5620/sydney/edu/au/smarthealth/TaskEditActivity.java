package elec5620.sydney.edu.au.smarthealth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/*
Represents the "Edit/Add Task" view
 */
public class TaskEditActivity extends AppCompatActivity {
    public int position = 0;
    EditText etItem;
    TextView etTime;
    Date date;
    String newItemChecker;
    String email="";
    Button buttonTimePick;
    Button buttonSave;
    Button buttonCancel;


    /*
    Start the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);
        etItem = findViewById(R.id.editTextTitle);
        etTime = findViewById(R.id.editTextTime);
        newItemChecker = getIntent().getStringExtra("newItem");
        buttonTimePick = findViewById(R.id.timeButton);
        buttonTimePick.setOnClickListener(this::onTime);
        buttonCancel = findViewById(R.id.cancelButton);
        buttonCancel.setOnClickListener(this::onCancel);
        buttonSave = findViewById(R.id.saveButton);
        buttonSave.setOnClickListener(this::onSubmit);
        /*
        Check whether this activity is start for adding new task
        or update an existing task
         */
        email = getIntent().getStringExtra("email");
        if (newItemChecker.compareTo("false") == 0) {
            Task task = Tools.stringToTask(getIntent().getStringExtra("task"));
            date = task.getDueDate();
            String dt = timeToString(date);
            String title = task.getTitle();
            position = getIntent().getIntExtra("position", -1);


            etItem.setText(title);
            etTime.setText(dt);
        }
        /*
        if this activity is started for creating a new
        task, then set the date to current time
         */
        else {
            position = getIntent().getIntExtra("position", -1);
            date = new Date();
            String dt = timeToString(date);
            etTime.setText(dt);
        }

    }

    /*
    Responsible for pass current task's title and date
    back to main activity
     */
    public void onSubmit(View v)
    {
        etItem = (EditText) findViewById(R.id.editTextTitle);
        etTime = (TextView) findViewById(R.id.editTextTime);
        String title = etItem.getText().toString();
        Task task = new Task(title, date, email);
        String ser_task = Tools.taskToString(task);

        // Prepare data intent for sending it back
        Intent data = new Intent();
        if (newItemChecker.compareTo("false") == 0) {
            data.putExtra("newItem", "false");
        } else {
            data.putExtra("newItem", "true");
        }
        // Pass relevant data back as a result
        data.putExtra("task", ser_task);
        data.putExtra("position", position);

        // Activity finishes OK, return the data
        setResult(RESULT_OK, data); // Set result code and bundle data for response
        finish(); // Close the activity, pass data to parent
    }

    /*
    Responsible for pop up dialog to check
    whetehr the user would like to cancel submit
    the task
     */
    public void onCancel(View v) {
        // create dialog
        AlertDialog.Builder cancelAlertDialog = new AlertDialog.Builder(TaskEditActivity.this);
        cancelAlertDialog.setCancelable(true);
        cancelAlertDialog.setMessage("Are you sure to cancel this edit? " +
                "Your unsaved edit will be discarded if you click YES");
        // if the user click "No" (positive button) then stady in current
        // activity
        cancelAlertDialog.setPositiveButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        // if the user click "Yes", then finish the current acitivy
        // and pass "RESULT_CANCELED" back to previous activity
        cancelAlertDialog.setNegativeButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setResult((RESULT_CANCELED));
                finish();
            }
        });

        cancelAlertDialog.show();
    }

    /*
    Responsible for picking time
     */
    public void onTime(View v) {
        Calendar c = Calendar.getInstance();
        // if the activity is not stated for creating new task,
        // then set calendar as the task's date
        if (newItemChecker.compareTo("false") == 0) {
            c.setTime(date);
        }
        // get time infomration from calendar
        int year = c.get(Calendar.YEAR);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        // get time from calendar
        Date d = c.getTime();
        // convert the date into string
        String d1 = timeToString(d);
        // set up time picker dialogue
        new TimePickerDialog(TaskEditActivity.this, AlertDialog.THEME_HOLO_LIGHT,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int min) {
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, min);
                        c.set(Calendar.SECOND, 0);
                        c.set(Calendar.MILLISECOND, 0);
                        Date d2 = c.getTime();
                        String d3 = timeToString(d2);
                /*
                check if the user picks a new time.
                if so, update the textview to show
                new time
                 */
                        if (d1.compareTo(d3) != 0) {
                            date = d2;
                            TextView editTime = (TextView) findViewById(R.id.editTextTime);
                            editTime.setText(d3);
                        }
                    }
                }, hour, minute, true).show();
        // setup date picker dialogue
        new DatePickerDialog(TaskEditActivity.this, AlertDialog.THEME_HOLO_LIGHT,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int timePickerYear,
                                          int timePickerMonth, int timePickerDay) {
                        c.set(Calendar.YEAR, timePickerYear);
                        c.set(Calendar.MONTH, timePickerMonth);
                        c.set(Calendar.DAY_OF_MONTH, timePickerDay);
                        Date d2 = c.getTime();
                        String d3 = timeToString(d2);
                        /*
                        check if the user picks a new time.
                        if so, update the textview to show
                        new time
                         */
                        if (d1.compareTo(d3) != 0) {
                            date = d2;
                            TextView editTime = (TextView) findViewById(R.id.editTextTime);
                            editTime.setText(d3);
                        }

                    }
                }, year, month, day).show();


    }

    /*
    Responsible for transform date to string
     */
    public String timeToString(Date date) {
        String pattern = "MM/dd/yyyy HH:mm";
        DateFormat df = new SimpleDateFormat(pattern);
        String todayAsString = df.format(date);
        return todayAsString;

    }


}