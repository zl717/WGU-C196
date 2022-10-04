package com.example.wguc196;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CourseDetailsActivity extends AppCompatActivity {

    int termId;
    int courseId;
    Intent intent;
    Course c;
    fDatabase db;

    TextView title;
    TextView start;
    TextView end;
    TextView status;
    TextView mentor;

    FloatingActionButton addAssessment;
    FloatingActionButton editCourse;
    ListView assessments;
    Course selectedCourse;
    Button notesBtn;
    Button mentorBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        db = fDatabase.getInstance(getApplicationContext());

        intent = getIntent();
        termId = intent.getIntExtra("termId", -1);
        courseId = intent.getIntExtra("courseId", -1);

        selectedCourse = db.courseDAO().getCourse(termId, courseId);

        List<Mentor> mentors = db.mentorDAO().getMentorByCourse(courseId);


        Toolbar toolbar = (Toolbar) findViewById(R.id.courseDetailsTB);
        toolbar.setTitle("    Course Details");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.inflateMenu(R.menu.menu_main);

        //populateAssessmentList();

        c = db.courseDAO().getCourse(termId, courseId);
//        populateAssessmentList();
        title = findViewById(R.id.courseDetails_Title);
        start = findViewById(R.id.courseDetails_Start);
        end = findViewById(R.id.courseDetails_end);
        status = findViewById(R.id.courseDetails_Status);
        mentor = findViewById(R.id.courseDetails_Mentor);
        assessments = findViewById(R.id.assessmentsLV);
        mentorBtn = findViewById(R.id.mentorBtn);

        //populateAssessmentList();
        //
        title.setText(c.getCourse_name());
        start.setText(c.getCourse_start().toString());
        end.setText(c.getCourse_end().toString());
        status.setText(c.getCourse_status());
        List<Mentor> mentorList = db.mentorDAO().getMentorByCourse(courseId);
        mentor.setText(mentorList.get(0).getMentor_name() + "\n" + mentorList.get(0).getMentor_phone() + "\n" + mentorList.get(0).getMentor_email());
        //mentor.setText(db.mentorDAO().getMentorByCourse(courseId).get(0).toString());

        populateAssessmentList();

        mentorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditMentor.class);
                intent.putExtra("termId", termId);
                intent.putExtra("courseId",courseId);
                startActivity(intent);
            }
        });

        assessments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), EditAssessmentActivity.class);
                //int courseId = db.courseDAO().getCourseList(termId).get(i).getCourse_id();
                //List<Assessment> assList = db.assessmentDAO().getAssessmentsByCourseId(i);
                List<Assessment> assList = db.assessmentDAO().getAssessmentsByCourseId(courseId);
                int assessmentId = assList.get(i).getAssessment_id();
                //int assessmentId = assList.get(i).getAssessment_id();
                intent.putExtra("assId", assessmentId);
                intent.putExtra("courseId", courseId);
                intent.putExtra("termId", termId);
                startActivity(intent);
            }
        });

        editCourse = findViewById(R.id.editCourseBtn);
        editCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditCourseActivity.class);
                intent.putExtra("termId", termId);
                intent.putExtra("courseId", courseId);

                startActivity(intent);

            }
        });

        addAssessment = findViewById(R.id.addAssessmentBtn);
        addAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat fmt = new SimpleDateFormat("MM:dd:yyyy");
                Calendar goal = Calendar.getInstance();
                goal.add(Calendar.MONTH, 1);

                Assessment a = new Assessment();
                a.setAssessment_title("New Assessment");
                a.setAssessment_status("NOT PASSED");
                a.setAssessment_type("not specified");
                //
                a.setCourse_id_fk(courseId);
                a.setAssessment_goal(goal.getTime());
                String defaultDate = "00/00/0000";
                Calendar alarmDate = Calendar.getInstance();
                alarmDate.add(Calendar.MONTH, 1);
                a.setAssessment_alarm(alarmDate.getTime());

                db.assessmentDAO().insertAssessment(a);

                populateAssessmentList();
            }
        });

        notesBtn = findViewById(R.id.courseDetails_CourseNotesBtn);
        notesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send courseId, termId to courseNoteActivity
                Intent intent = new Intent(getApplicationContext(), CourseNotesActivity.class);
                intent.putExtra("courseId", courseId);
                intent.putExtra("termId", termId);
                startActivity(intent);
            }
        });

    }

    private void updateView(){

        String title = selectedCourse.getCourse_name();
        String status = selectedCourse.getCourse_status();
        String notes = selectedCourse.getCourse_notes();
        Date start = selectedCourse.getCourse_start();
        Date end = selectedCourse.getCourse_end();
        Date alertDate = selectedCourse.getCourse_alert_date();

    }

    public void populateAssessmentList(){

        //assessments = findViewById(R.id.assessmentsLV);

        List<Assessment> assList = db.assessmentDAO().getAssessmentsByCourseId(courseId);
        String[] assNames = new String[assList.size()];

        for(int i = 0; i < assList.size(); i++){
            assNames[i] = assList.get(i).getAssessment_title();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, assNames);
        assessments.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(this, TermDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("termId", termId);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}