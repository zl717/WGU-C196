package com.example.wguc196;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    fDatabase db;
    PopulateDatabase pop = new PopulateDatabase();
    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = fDatabase.getInstance(getApplicationContext());

        populateCourseCompleted();
        populateAssessmentsCompleted();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("    Progress Tracking");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            checkNotificationsAtLaunch();
            checkAssessmentNotifications();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Button btn = findViewById(R.id.sampleDataButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pop.populate(getApplicationContext());
                Log.d("POPULATE BUTTON", "POPULATE BUTTON PRESSED");
                Toast.makeText(MainActivity.this, "populate pressed", Toast.LENGTH_SHORT).show();
                populateAssessmentsCompleted();
                populateCourseCompleted();
            }
        });

        Button clear = findViewById(R.id.clearButton);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.clearAllTables();

                Log.d("CLEAR BUTTON", "CLEAR BUTTON PRESSED");
                Toast.makeText(MainActivity.this, "clear pressed", Toast.LENGTH_SHORT).show();
                populateAssessmentsCompleted();
                populateCourseCompleted();
            }
        });

        Button viewTerms = findViewById(R.id.viewTermsBtn);
        viewTerms.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), TermListActivity.class);
                startActivity(intent);

            }
        });

    }

    private void checkAssessmentNotifications(){
        final NotificationManagerCompat nm = NotificationManagerCompat.from(this);

        final NotificationCompat.Builder assessmentBuilder = new NotificationCompat.Builder(this, "assessments")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Assessment Notification")
                .setContentText("One of your assessments has a target set today.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "assessmentAlarms";
            String description = "Notification Channel: assessments";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("assessments", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


        List<Assessment> allAssessments = db.assessmentDAO().getAllAssessments();
        Date now = new Date();
        String n = formatter.format(now);

        for(int i = 0; i < allAssessments.size(); i++){

            Date checkDate = allAssessments.get(i).getAssessment_alarm();
            String checkAgainst = formatter.format(checkDate);
            if(checkAgainst.equals(n)){
                nm.notify(60, assessmentBuilder.build());
            }

        }


    }

    private void checkNotificationsAtLaunch() throws ParseException {
        final NotificationCompat.Builder courseStart = new NotificationCompat.Builder(this, "courses")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Course Notification")
                .setContentText("One of your courses has a START target set today.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        final NotificationCompat.Builder courseEnd = new NotificationCompat.Builder(this, "courses")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Course Notification")
                .setContentText("One of your courses has an END target set today.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        final NotificationCompat.Builder assessment = new NotificationCompat.Builder(this, "courses")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Assessment Notification")
                .setContentText("Assessment alarm set for today.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "courseAlarms";
            String description = "Notification Channel: courses";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("courses", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        final NotificationManagerCompat nm = NotificationManagerCompat.from(this);

        List<Course> allCourses = db.courseDAO().getAllCourses();
        List<Assessment> allAssessments = db.assessmentDAO().getAllAssessments();
        Date now = new Date();
        String n = formatter.format(now);

        for(int i = 0; i < allAssessments.size(); i++){
            Date checkDate = allAssessments.get(i).getAssessment_alarm();
            String checkAgainst = formatter.format(checkDate);

            if(checkAgainst.equals(n)){
                nm.notify(53, assessment.build());
            }
        }


        for(int i = 0; i < allCourses.size(); i++){

            Date checkDate = allCourses.get(i).getCourse_alert_date();
            String checkAgainst = formatter.format(checkDate);

            Date startDate = allCourses.get(i).getCourse_start();
            String startDateCheck = formatter.format(startDate);

            Date endDate = allCourses.get(i).getCourse_end();
            String endDateCheck = formatter.format(endDate);

            System.out.println(checkAgainst + " " + startDateCheck + " " + endDateCheck);

            if(checkAgainst.equals(startDateCheck)){
            if(checkAgainst.equals(n)){
                nm.notify(50, courseStart.build());
                }
            }

            if(checkAgainst.equals(endDateCheck)){
                if(checkAgainst.equals(n)){
                    nm.notify(51, courseEnd.build());
                }
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    private void populateCourseCompleted(){

        List<Course> courses = db.courseDAO().getAllCourses();
        double comp = 0;
        double incomp = 0;

        for(int i = 0; i < courses.size(); i++){
            if(courses.get(i).getCourse_status().contains("COMPLETE")){
                comp++;
            }
            else {
                incomp++;
            }
        }


        TextView tvC = findViewById(R.id.completed_TV);
        tvC.setText(String.valueOf((int)comp));
        TextView tvC_PERCENT = findViewById(R.id.completePercent_TV);
        tvC_PERCENT.setText(String.valueOf((int)(comp/courses.size() * 100)));

        TextView remaining = findViewById(R.id.remaining_TV);
        remaining.setText(String.valueOf((int)incomp));
        TextView remainingPercent = findViewById(R.id.remainingPercent_TV);
        remainingPercent.setText(" " + String.valueOf((int)(incomp/courses.size() * 100)));

    }


    private void populateAssessmentsCompleted(){

        List<Assessment> assessments = db.assessmentDAO().getAllAssessments();
        double passed = 0;
        double remaining = 0;

        for(int i = 0; i < assessments.size(); i++){

            if(assessments.get(i).getAssessment_status().contains("PASS")){
                passed++;
            }

            else {
                remaining++;
            }

        }

        TextView completed = findViewById(R.id.assCompleted_TV);
        completed.setText(String.valueOf((int)passed));
        TextView completedPercent = findViewById(R.id.assCompletePercent_TV);
        completedPercent.setText(String.valueOf((int)(passed/assessments.size() * 100)));

        TextView remainder = findViewById(R.id.assRemaining_TV);
        remainder.setText(String.valueOf((int) remaining));
        TextView remainingPercent = findViewById(R.id.assRemainingPercent_TV);
        remainingPercent.setText(" " + String.valueOf((int)(remaining/assessments.size() * 100)));

    }

}