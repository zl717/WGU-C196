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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.List;

public class TermListActivity extends AppCompatActivity {

    fDatabase db;
    ListView lv;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
        db = fDatabase.getInstance(getApplicationContext());
        lv = findViewById(R.id.termListView);
        //Toolbar tb = findViewById(R.id.termListActivityTB);
        //tb = findViewById(R.id.toolbar);

        //setSupportActionBar(tb);
        //getSupportActionBar().setTitle("    Term View");

        Toolbar toolbar = (Toolbar) findViewById(R.id.termListActivityTB);
        toolbar.setTitle("    Term View");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.inflateMenu(R.menu.menu_main);
        //getSupportActionBar().setTitle("    Progress Tracking");

        populateTermList();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                Intent intent = new Intent(getApplicationContext(), TermDetailsActivity.class);
                List<Term> termsList = db.termDAO().getTermList();
                int termID = termsList.get(pos).getTerm_id();
                intent.putExtra("termId", termID);

                startActivity(intent);
            }
        });

        fab = findViewById(R.id.addTermBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                Term term = new Term();
                term.setTerm_name("Term Added");
                term.setTerm_start(c.getTime());
                c.add(Calendar.MONTH, 2);
                term.setTerm_end(c.getTime());
                db.termDAO().insertTerm(term);
                populateTermList();
            }
        });
    }

    public void populateTermList(){

        List<Term> terms = db.termDAO().getTermList();
        String[] termNames = new String[terms.size()];

        for(int i = 0; i < terms.size(); i++){
            termNames[i] = terms.get(i).getTerm_name();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, termNames);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(TermListActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}