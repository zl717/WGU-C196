package com.example.wguc196;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TermDetailsActivity extends AppCompatActivity {

    fDatabase db;

    ListView coursesLV;
    TextView termTitleTV;
    TextView termStartTV;
    TextView termEndTV;

    Intent intent;
    int termId;

    Term selectedTerm;
    DateFormat formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);
        db = fDatabase.getInstance(getApplicationContext());
        //setTitle("  Term Details");

        Toolbar toolbar = (Toolbar) findViewById(R.id.termDetailsTB);
        toolbar.setTitle("    Term Details");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.inflateMenu(R.menu.menu_main);

        intent = getIntent();
        termId = intent.getIntExtra("termId", -1);
        //removed hh:mm
        formatter = new SimpleDateFormat("MM/dd/yyyy");

        coursesLV = findViewById(R.id.coursesLV);
        termTitleTV = findViewById(R.id.termTitleTV);
        termStartTV = findViewById(R.id.startDateTV);
        termEndTV = findViewById(R.id.endDateTV);

        updateView();

        coursesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), CourseDetailsActivity.class);
                int courseId = db.courseDAO().getCourseList(termId).get(i).getCourse_id();
                intent.putExtra("termId", termId);
                intent.putExtra("courseId", courseId);
                startActivity(intent);

            }
        });

        updateCourseList();

        FloatingActionButton addCourse = findViewById(R.id.addCourseBtn);
        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int num = db.courseDAO().getCourseList(termId).size()+1;
                Course course = new Course();
                course.setCourse_name("Course added: " + num);
                course.setCourse_start(c.getTime());
                c.add(Calendar.MONTH, 2);
                course.setCourse_end(c.getTime());
                course.setCourse_status("ACTIVE");
                course.setTerm_id_fk(termId);
                Calendar cal = Calendar.getInstance();
                c.add(Calendar.MONTH, 1);
                String newDate = formatter.format(cal.getTime());
                try {
                    course.setCourse_alert_date(formatter.parse(newDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                db.courseDAO().insertCourse(course);
                //db.mentorDAO().
                updateCourseList();
            }
        });

        FloatingActionButton editTerm = findViewById(R.id.editTermBtn);
        editTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Term placeholder = db.termDAO().getTerm(termId);
                Intent intent = new Intent(getApplicationContext(), EditTermActivity.class);
                intent.putExtra("termId", termId);
                startActivity(intent);
            }
        });

    }

    private void updateView(){

        selectedTerm = db.termDAO().getTerm(termId);

        Date start = selectedTerm.getTerm_start();
        Date end = selectedTerm.getTerm_end();

        String s = formatter.format(start);
        String e = formatter.format(end);

        termTitleTV.setText(selectedTerm.getTerm_name());
        termStartTV.setText(s + " - ");
        termEndTV.setText(e);

    }

    //back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(this, TermListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateCourseList(){
        List<Course> allCourses = new ArrayList<>();
        List<Mentor> mentors;// = db.mentorDAO().getMentorByCourse(courseId);


        try {
            allCourses = db.courseDAO().getCourseList(termId);
        }

        catch (Exception e){
            System.out.println("update failed");
        }

        String[] items = new String[allCourses.size()];
        for(int i = 0; i < allCourses.size(); i++) {
            items[i] = allCourses.get(i).getCourse_name();
        }

        double decider = (Math.random() *2);

        for(int i = 0; i < allCourses.size(); i++){
            mentors = db.mentorDAO().getMentorByCourse(allCourses.get(i).getCourse_id());
            if(mentors.isEmpty()){
                if (decider < 1){
                    Mentor mentor = new Mentor();
                    mentor.setCourse_id_fk(allCourses.get(i).getCourse_id());
                    mentor.setMentor_name("Steve Stevens");
                    mentor.setMentor_phone("555-555-9999");
                    mentor.setMentor_email("email@address.com");
                    db.mentorDAO().insertMentor(mentor);
                }

                else {
                    Mentor mentor1 = new Mentor();
                    mentor1.setCourse_id_fk(allCourses.get(i).getCourse_id());
                    mentor1.setMentor_name("Jeff Jefferson");
                    mentor1.setMentor_phone("111-111-4444");
                    mentor1.setMentor_email("JeffsEmail@address.com");
                    db.mentorDAO().insertMentor(mentor1);

                }
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        coursesLV.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

}