package com.example.wguc196;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class EditMentor extends AppCompatActivity {

    Intent intent;
    fDatabase db;

    int courseId;
    int termId;

    EditText name;
    EditText phone;
    EditText email;
    EditText scrollableBio;
    Button save;

    List<Mentor> m;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mentor);
        db = fDatabase.getInstance(getApplicationContext());
        intent = getIntent();
        courseId = intent.getIntExtra("courseId", -1);
        termId = intent.getIntExtra("termId", -1);

        m = db.mentorDAO().getMentorByCourse(courseId);

        name = findViewById(R.id.mentorName);
        phone = findViewById(R.id.mentorPhone);
        email = findViewById(R.id.mentorEmail);
        scrollableBio = findViewById(R.id.scrollableBio);
        save = findViewById(R.id.mentor_SaveBtn);

        name.setText(m.get(0).getMentor_name());
        phone.setText(m.get(0).getMentor_phone());
        email.setText(m.get(0).getMentor_email());
        setScrollableBio();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mentor mentor = new Mentor();
                mentor.setMentor_name(name.getText().toString());
                mentor.setMentor_phone(phone.getText().toString());
                mentor.setMentor_email(email.getText().toString());
                mentor.setMentor_id(m.get(0).getMentor_id());
                mentor.setCourse_id_fk(courseId);
                db.mentorDAO().updateMentor(mentor);

                Intent intent = new Intent(getApplicationContext(), CourseDetailsActivity.class);
                intent.putExtra("courseId", courseId);
                intent.putExtra("termId", termId);
                startActivity(intent);
            }
        });


    }

    private void setScrollableBio(){

        for(int i = 0; i < 10; i++){
            scrollableBio.append("This mentor has a long bio that isn't actually filled out\nSCROLL DOWN to see the rest\n");
        }

    }
}