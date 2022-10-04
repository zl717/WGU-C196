package com.example.wguc196;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.List;

public class PopulateDatabase extends AppCompatActivity {

    public static String LOG_TAG = "populate db";
    Term term = new Term();
    Course course = new Course();
    Assessment assessment = new Assessment();
    Mentor mentor = new Mentor();

    Term term1 = new Term();
    Course course1 = new Course();
    Assessment assessment1 = new Assessment();

    List<Course> courseList;
    List<Term> termList;
    fDatabase db;

    public void populate(Context context){

        db = fDatabase.getInstance(context);

        try {
            insertTerms();
            insertCourses();
            insertAssessments();
            insertMentors();

        }
        catch (Exception e){
            e.printStackTrace();
            Log.d(LOG_TAG, "Failed to populate");
        }

    }

    private void insertTerms(){

        Calendar start;
        Calendar end;

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, -2);
        end.add(Calendar.MONTH, 1);
        term.setTerm_name("Sample Term");
        term.setTerm_start(start.getTime());
        term.setTerm_end(end.getTime());

        db.termDAO().insertTerm(term);

        Calendar start1;
        Calendar end1;

        start1 = Calendar.getInstance();
        end1 = Calendar.getInstance();
        start1.add(Calendar.MONTH, -2);
        end1.add(Calendar.MONTH, 1);
        term1.setTerm_name("Sample Term 2");
        term1.setTerm_start(start1.getTime());
        term1.setTerm_end(end1.getTime());

        db.termDAO().insertTerm(term1);

    }

    private void insertCourses() {

        Calendar start;
        Calendar end;

        termList = db.termDAO().getTermList();
        if(termList == null) return;

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, -2);
        end.add(Calendar.MONTH, 1);
        course.setCourse_name("Sample Course");
        course.setCourse_start(start.getTime());
        course.setCourse_end(end.getTime());
        course.setCourse_notes("Note content");
        course.setCourse_status("COMPLETED");
        course.setCourse_alert_date(end.getTime());
        course.setTerm_id_fk(termList.get(0).getTerm_id());

        db.courseDAO().insertCourse(course);

        Calendar start1;
        Calendar end1;

        start1 = Calendar.getInstance();
        end1 = Calendar.getInstance();
        start1.add(Calendar.MONTH, -2);
        end1.add(Calendar.MONTH, 1);
        course1.setCourse_name("Sample Course 2");
        course1.setCourse_start(start1.getTime());
        course1.setCourse_end(end1.getTime());
        course1.setCourse_notes("Note content");
        course1.setCourse_status("IN PROGRESS");
        course1.setCourse_alert_date(Calendar.getInstance().getTime());
        course1.setTerm_id_fk(termList.get(0).getTerm_id());

        db.courseDAO().insertCourse(course1);

    }

    private void insertAssessments(){

       courseList = db.courseDAO().getCourseList(termList.get(0).getTerm_id());

        Calendar goal;
        goal = Calendar.getInstance();
        goal.add(Calendar.MONTH, 1);

        assessment.setAssessment_title("Sample Assessment");
        assessment.setAssessment_goal(goal.getTime());
        assessment.setAssessment_type("OBJECTIVE");
        assessment.setCourse_id_fk(courseList.get(0).getCourse_id());
        assessment.setAssessment_status("PASSED");
        Calendar alarmDate = Calendar.getInstance();
        alarmDate.add(Calendar.MONTH, 1);
        assessment.setAssessment_alarm(alarmDate.getTime());

        db.assessmentDAO().insertAssessment(assessment);

        Calendar goal1;
        goal1 = Calendar.getInstance();
        goal1.add(Calendar.MONTH, 1);

        assessment1.setAssessment_title("Sample Assessment 2");
        assessment1.setAssessment_goal(goal1.getTime());
        assessment1.setAssessment_type("PERFORMANCE");
        assessment1.setCourse_id_fk(courseList.get(0).getCourse_id());
        assessment1.setAssessment_status("FAILED");
        Calendar alarmDate1 = Calendar.getInstance();
        alarmDate1.add(Calendar.MONTH, 1);
        assessment1.setAssessment_alarm(alarmDate1.getTime());

        db.assessmentDAO().insertAssessment(assessment1);

    }

    private void insertMentors(){

        mentor.setMentor_name("John Smith");
        mentor.setMentor_email("Jsmith@email.com");
        mentor.setMentor_phone("123-456-7899");
        mentor.setCourse_id_fk(courseList.get(0).getCourse_id());

        db.mentorDAO().insertMentor(mentor);

    }



}
