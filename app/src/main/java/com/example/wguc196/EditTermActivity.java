package com.example.wguc196;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditTermActivity extends AppCompatActivity {

    Intent intent;
    int termId;
    fDatabase db;
    EditText title;
    EditText start;
    EditText end;

    DateFormat formatter;

    Button save;
    Button cancel;
    Button delete;

    Converters c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_term);
        db = fDatabase.getInstance(getApplicationContext());
        intent = getIntent();
        termId = intent.getIntExtra("termId", -1);
        delete = findViewById(R.id.editTerm_DeleteBtn);

        Toolbar toolbar = (Toolbar) findViewById(R.id.editTermTB);
        toolbar.setTitle("    Edit Term");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.inflateMenu(R.menu.menu_main);

        populateFields();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Course> courses = db.courseDAO().getCourseList(termId);
                Intent intent = new Intent(getApplicationContext(), TermListActivity.class);

                if(courses.isEmpty()){
                    db.termDAO().deleteTerm(db.termDAO().getTerm(termId));
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Term delete failed: term contains 1 or more courses", Toast.LENGTH_LONG).show();
                }
            }
        });

        save = findViewById(R.id.saveBtn_editTerm);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat fmt = new SimpleDateFormat("MM-dd-yyyy");
                Date newStart = new Date();
                Date newEnd = new Date();
                try {
                    newStart = fmt.parse(String.valueOf(start.getText()));
                    newEnd = fmt.parse(String.valueOf(end.getText()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //System.out.println(newStart + "*&&^*&^*&^*&^*&^*^&*^^^^&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& " + newEnd);

                Term t = db.termDAO().getTerm(termId);
                t.setTerm_name(String.valueOf(title.getText()));
                t.setTerm_start(newStart);
                t.setTerm_end(newEnd);
                db.termDAO().updateTerm(t);

                Intent intent = new Intent(getApplicationContext(), TermDetailsActivity.class);
                intent.putExtra("termId", termId);
                startActivity(intent);
            }
        });

        cancel = findViewById(R.id.cancelBtn_editTerm);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TermDetailsActivity.class);
                intent.putExtra("termId", termId);
                startActivity(intent);
            }
        });
    }


    private void populateFields(){

        title = findViewById(R.id.edit_TermTitle);
        start = findViewById(R.id.edit_TermStart);
        end = findViewById(R.id.edit_TermEnd);

        //formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        formatter = new SimpleDateFormat("MM-dd-yyyy");

        Term term = db.termDAO().getTerm(termId);
        String n = term.getTerm_name();
        Date s = term.getTerm_start();
        Date e = term.getTerm_end();

        title.setText(term.getTerm_name());
        start.setText(formatter.format(s));
        end.setText(formatter.format(e));
        //start.setText(s.toString());
        //end.setText(e.toString());

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
}