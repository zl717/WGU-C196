package com.example.wguc196;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CourseNotesActivity extends AppCompatActivity {

    Intent intent;
    fDatabase db;

    EditText notesHolder;
    int courseId;
    int termId;

    Button save;
    Button cancel;
    Button share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_notes);

        db = fDatabase.getInstance(getApplicationContext());
        notesHolder = findViewById(R.id.notesHolder);
        intent = getIntent();
        courseId = intent.getIntExtra("courseId", -1);
        termId = intent.getIntExtra("termId", -1);

        share = findViewById(R.id.share);


        Toolbar toolbar = (Toolbar) findViewById(R.id.courseNotes_TB);
        toolbar.setTitle("    Course Notes");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.inflateMenu(R.menu.menu_main);

        fillNotes();

        saveButton();
        cancelButton();

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = db.courseDAO().getCourse(termId, courseId).getCourse_notes();
                String shareSub = "Course Notes";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        });

    }

    private void saveButton(){

        save = findViewById(R.id.courseNotes_SaveBtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newNotes = notesHolder.getText().toString();
                Course c = db.courseDAO().getCourse(termId, courseId);
                c.setCourse_notes(newNotes);
                db.courseDAO().updateCourse(c);
                Intent intent = new Intent(getApplicationContext(), CourseDetailsActivity.class);
                intent.putExtra("termId", termId);
                intent.putExtra("courseId", courseId);
                startActivity(intent);
            }
        });

    }

    private void cancelButton(){
        cancel = findViewById(R.id.courseNotes_CancelBtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CourseDetailsActivity.class);
                intent.putExtra("termId", termId);
                intent.putExtra("courseId", courseId);
                startActivity(intent);
            }
        });
    }

    private void fillNotes(){
        notesHolder = findViewById(R.id.notesHolder);
        //System.out.println(termId + " " + courseId);
        Course c = db.courseDAO().getCourse(termId, courseId);
        notesHolder.setText(c.getCourse_notes());

    }

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