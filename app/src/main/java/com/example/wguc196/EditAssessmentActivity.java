package com.example.wguc196;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditAssessmentActivity extends AppCompatActivity {

    fDatabase db;
    Intent intent;
    int courseId;
    int assId;
    int termId;

    EditText title;
    EditText type;
    EditText dueDate;
    EditText info;
    EditText alarmDate;

    Button save;
    Button deleteAssessmentBtn;

    Assessment a;
    SimpleDateFormat formatter;

    DatePickerDialog.OnDateSetListener dateSetListener;
    String datePickerIn;
    //goal is due date for no reason


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assessment);
        db = fDatabase.getInstance(getApplicationContext());
        formatter = new SimpleDateFormat("MM/dd/yyyy");

        intent = getIntent();
        courseId = intent.getIntExtra("courseId", -1);
        assId = intent.getIntExtra("assId", -1);
        termId = intent.getIntExtra("termId", -1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.editAssTB);
        toolbar.setTitle("    Edit Assessment");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.inflateMenu(R.menu.menu_main);

        title = findViewById(R.id.editAss_titleET);
        type = findViewById(R.id.editAss_EditTypeET);
        dueDate = findViewById(R.id.editAss_editDateET);
        info = findViewById(R.id.editAss_EditInfoET);
        alarmDate = findViewById(R.id.editAss_Alarm);
        save = findViewById(R.id.editAss_SaveBtn);
        deleteAssessmentBtn = findViewById(R.id.editAss_DeleteBtn);


        a = db.assessmentDAO().getSingleAssessment(assId);
        title.setText(a.getAssessment_title());
        type.setText(a.getAssessment_type());
        info.setText(a.getAssessment_status());
        //dueDate.setText(formatter.format((a.getAssessment_goal().toString())));
        dueDate.setText(formatter.format(a.getAssessment_goal()));
        String alarmDateText = alarmDate.getText().toString();
        if(alarmDateText == "" || alarmDateText == null){
            alarmDate.setText("00/00/0000");
        }
        alarmDate.setText(formatter.format(a.getAssessment_alarm()));
        //System.out.println(a.getAssessment_alarm().toString());



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Assessment a = new Assessment();
                a.setAssessment_id(assId);
                a.setCourse_id_fk(courseId);
                a.setAssessment_title(title.getText().toString());
                a.setAssessment_type(type.getText().toString());
                a.setAssessment_status(info.getText().toString());
                try {
                    a.setAssessment_goal(formatter.parse(dueDate.getText().toString()));
                    a.setAssessment_alarm(formatter.parse(alarmDate.getText().toString()));
                    db.assessmentDAO().updateAssessment(a);
                    Intent intent = new Intent(getApplicationContext(), CourseDetailsActivity.class);
                    intent.putExtra("courseId", courseId);
                    intent.putExtra("termId", termId);
                    startActivity(intent);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        alarmDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int year = cal.get(Calendar.YEAR);

                DatePickerDialog dialog = new DatePickerDialog(EditAssessmentActivity.this,
                        android.R.style.Theme_Black,
                        dateSetListener,
                        year, month, day);
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month += 1;
                datePickerIn = month + "/" + day + "/" + year;
                alarmDate.setText(datePickerIn);
            }
        };


        //may b a problem for unknown reason
        deleteAssessmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Assessment a = db.assessmentDAO().getSingleAssessment(assId);
                db.assessmentDAO().deleteAssessment(a);
                //db.courseDAO().deleteCourse(c);
                Intent intent2 = new Intent(getApplicationContext(), CourseDetailsActivity.class);
                intent2.putExtra("termId", termId);
                intent2.putExtra("courseId", courseId);
                startActivity(intent2);
                //startActivity(buttonClick);
            }
        });
    }


    //back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(this, CourseDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("termId", termId);
                intent.putExtra("courseId" ,courseId);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}