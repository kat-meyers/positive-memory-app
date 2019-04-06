package com.example.positivememory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class IndividualMemoryActivity extends AppCompatActivity {

    TextView entryText, dateText;
    Intent listIntent, intent, editIntent;
    String specificDate, specificEntry;
    DatabaseHelper mDatabaseHelper;
    Button btn1, btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inidividual_memory);

        entryText = findViewById(R.id.singleEntryTextView);
        dateText = findViewById(R.id.dateEntryTextView);
        intent = getIntent();
        listIntent = new Intent(IndividualMemoryActivity.this, MemoryListActivity.class);
        editIntent = new Intent(IndividualMemoryActivity.this, EditEntry.class);
        mDatabaseHelper = new DatabaseHelper(this);
        btn1 = findViewById(R.id.backButton);
        btn2 = findViewById(R.id.editButton);

        specificDate = intent.getStringExtra("selectedDate");
        specificEntry = intent.getStringExtra("selectedEntry");

        dateText.setText(specificDate);
        entryText.setText(specificEntry);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(listIntent);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editIntent.putExtra("selectedDate", specificDate);
                editIntent.putExtra("selectedEntry", specificEntry);
                startActivity(editIntent);
            }
        });
    }
}
