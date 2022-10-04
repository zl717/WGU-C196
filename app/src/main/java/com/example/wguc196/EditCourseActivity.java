package com.example.wguc196;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditCourseActivity extends AppCompatActivity {

    fDatabase db;
    Intent intent;

    String sDate;
    String eDate;

    Course c;
    int termId;
    int courseId;

    TextView titleTV;
    TextView startTV;
    TextView endTV;
    TextView statusTV;
    Button saveBtn;
    Button deleteCourseBtn;


    Button startNotify;
    Button endNotify;

    //Intent buttonClick;
    SimpleDateFormat formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        formatter =  new SimpleDateFormat("MM-dd-yyyy");

        db = fDatabase.getInstance(getApplicationContext());
        intent = getIntent();
        termId = intent.getIntExtra("termId", -1);
        courseId = intent.getIntExtra("courseId", -1);
        c = db.courseDAO().getCourse(termId, courseId);

        Toolbar toolbar = findViewById(R.id.editCourse_TB);
        toolbar.setTitle("    Edit Course");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.inflateMenu(R.menu.menu_main);

        titleTV = findViewById(R.id.editCourse_TitleTV);
        startTV = findViewById(R.id.editCourse_StartTV);
        endTV = findViewById(R.id.editCourse_EndTV);
        statusTV = findViewById(R.id.editCourse_StatusTV);
        saveBtn = findViewById(R.id.editCourse_SaveBtn);
        deleteCourseBtn = findViewById(R.id.editCourse_DeleteBtn);
        startNotify = findViewById(R.id.startNotifyBtn);
        endNotify = findViewById(R.id.endNotifyBtn);
        populateFields();

        createNotificationChannel();

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "courses")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Course Notification")
                .setContentText("One of your courses has a target set today.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        final NotificationManagerCompat nm = NotificationManagerCompat.from(this);


        startNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                Date d = cal.getTime();
                String dateToday = formatter.format(d);

                String inputDate = endTV.getText().toString();
                Date inputEnd = new Date();
                //Date inputDate = formatter.parse();
                try {
                    inputEnd = formatter.parse(String.valueOf(startTV.getText()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                inputDate = formatter.format(inputEnd);

                if(dateToday.equals( inputDate)){
                    nm.notify(100, builder.build());
                    //System.out.println(inputDate);
                }

                try {
                    c.setCourse_alert_date(formatter.parse(inputDate));
                    db.courseDAO().updateCourse(c);
                    Toast.makeText(getApplicationContext(), "Start Date Notification Set", Toast.LENGTH_LONG);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });


        endNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //System.out.println("999999999999999999999999999999999999999999999999999999999999");

                Calendar cal = Calendar.getInstance();
                Date d = cal.getTime();
                String dateToday = formatter.format(d);

                String inputDate = endTV.getText().toString();
                Date inputEnd = new Date();
                //Date inputDate = formatter.parse();
                try {
                    inputEnd = formatter.parse(String.valueOf(endTV.getText()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                inputDate = formatter.format(inputEnd);

                if(dateToday.equals( inputDate)){
                    nm.notify(100, builder.build());
                    //System.out.println("YEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEET");
                }

                try {
                    c.setCourse_alert_date(formatter.parse(inputDate));
                    db.courseDAO().updateCourse(c);
                    Toast.makeText(getApplicationContext(), "End Date Notification Set", Toast.LENGTH_LONG);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(), "Press save to set alarm", Toast.LENGTH_LONG);

            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //changed from c
                Course c2 = db.courseDAO().getCourse(termId,courseId);
                c2.setCourse_name(titleTV.getText().toString());
                c2.setCourse_status(statusTV.getText().toString());

                sDate = startTV.getText().toString();
                eDate = endTV.getText().toString();

                //sDate = formatter.format(sDate);
                //eDate = formatter.format(eDate);

                Date start = new Date();
                Date end = new Date();

                try {
                    start = formatter.parse(sDate);
                    end = formatter.parse(eDate);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                c2.setCourse_start(start);
                c2.setCourse_end(end);
                db.courseDAO().updateCourse(c2);
                Intent intent1 = new Intent(getApplicationContext(), CourseDetailsActivity.class);
                intent1.putExtra("termId", termId);
                intent1.putExtra("courseId", courseId);
                startActivity(intent1);
                //startActivity(buttonClick);

            }
        });

        deleteCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.courseDAO().deleteCourse(c);
                Intent intent2 = new Intent(getApplicationContext(), TermDetailsActivity.class);
                intent2.putExtra("termId", termId);
                startActivity(intent2);
                //startActivity(buttonClick);
            }
        });

    }

    private void createNotificationChannel(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "courseAlarms";
            String description = "Notification Channel: courses";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("courses", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }

    private void populateFields(){

        //c = db.courseDAO().getCourse(termId, courseId);
        Course c = db.courseDAO().getCourse(termId,courseId);

        Date s = c.getCourse_start();
        Date e = c.getCourse_end();
        String start = formatter.format(s);
        String end = formatter.format(e);

        titleTV.setText(c.getCourse_name());
        startTV.setText(start);
        endTV.setText(end);
        statusTV.setText(c.getCourse_status());

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(this, CourseDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("termId", termId);
                intent.putExtra("courseId", courseId);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}