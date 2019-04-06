package com.example.positivememory;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;
    EditText entryText;
    TextView text;
    Button btn1, btn2, btn3;
    Intent intent;
    Calendar calendar, calendar2;
    DatePickerDialog datePicker;
    String date;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        entryText = findViewById(R.id.entryEditText);
        text = findViewById(R.id.dateTextView);
        btn1 = findViewById(R.id.saveButton);
        btn2 = findViewById(R.id.dateButton);
        btn3 = findViewById(R.id.listButton);
        mDatabaseHelper = new DatabaseHelper(this);
        intent = new Intent(MainActivity.this, MemoryListActivity.class);
        date = null;
        sp = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        editor = sp.edit();

        if(sp.contains("myDate")){
            text.setText(sp.getString("myDate", "0"));
        }
        if(sp.contains("myEntry")){
            entryText.setText(sp.getString("myEntry", "0"));
        }

        /*
            When clicked, the entry is saved to a local database
         */
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String entry = entryText.getText().toString();

                if(entry.length() != 0 && date != null){
                    addEntry(date, entry);
                    date = null;
                    text.setText("");
                    entryText.setText("");
                    editor.clear();
                } else if(entry.length() == 0){
                    Toast.makeText(MainActivity.this, "Enter your thoughts first!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Enter a date!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*
            When clicked, a DatePickerDialog pops up and the user sets the date of the entry
         */
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                calendar2 = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                datePicker = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String tempDate = (month+1) + "/" + dayOfMonth + "/" + year;
                        calendar2.set(year, month, dayOfMonth);

                        if(calendar2.after(calendar)){
                            Toast.makeText(MainActivity.this, "That day hasn't happened yet!", Toast.LENGTH_SHORT).show();
                        }else if(mDatabaseHelper.fetchEntryByDate(tempDate).getCount() == 0){
                            text.setText((month+1) + "/" + dayOfMonth + "/" + year);
                            date = text.getText().toString();
                        }else{
                            Toast.makeText(MainActivity.this, "You already entered something for that day!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, year, month, day);
                datePicker.show();
            }
        });

        /*
            When clicked, the MemoryList Activity is started
         */
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text.getText() != null) {
                    editor.putString("myDate", date);
                    editor.putString("myEntry", entryText.getText().toString());
                    editor.apply();
                }
                startActivity(intent);
            }
        });

    }

    /*
        Adds an entry to the database
     */
    public void addEntry(String date, String entry){
        boolean insertData = mDatabaseHelper.addData(date, entry);

        if(insertData){
            Toast.makeText(this, "Entry saved!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("myDate", date);
        savedInstanceState.putString("myEntry", entryText.getText().toString());

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        date = savedInstanceState.getString("myDate");
        entryText.setText(savedInstanceState.getString("myEntry"));
    }
}
