package com.example.positivememory;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.Calendar;

public class MemoryListActivity extends AppCompatActivity {

    Intent intent, intent2;
    Button search, wordSearch, random, entry;
    EditText keyword;
    ListView entries;
    DatabaseHelper mDatabaseHelper;
    Calendar calendar;
    Cursor cursor, tempCursor;
    SimpleCursorAdapter adapter, adapter2;
    DatePickerDialog datePicker;
    String[] columns;
    int[] to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_list);

        intent = new Intent(MemoryListActivity.this, MainActivity.class);
        intent2 = new Intent(MemoryListActivity.this, IndividualMemoryActivity.class);
        search = findViewById(R.id.searchButton);
        wordSearch = findViewById(R.id.searchWordButton);
        random = findViewById(R.id.randomButton);
        entry = findViewById(R.id.newEntryButton);
        keyword = findViewById(R.id.searchEditText);
        entries = findViewById(R.id.entriesList);
        mDatabaseHelper = new DatabaseHelper(this);
        cursor = mDatabaseHelper.fetchAllEntries();

        columns = new String[]{
                mDatabaseHelper.COLUMN_NAME_TITLE, mDatabaseHelper.COLUMN_NAME_SUBTITLE
        };

        to = new int[]{
                android.R.id.text1
        };

        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, columns, to, 0);

        entries.setAdapter(adapter);

        /*
            When clicked, the entry from that date is shown
         */
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!adapter.isEmpty()) {

                    calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    datePicker = new DatePickerDialog(MemoryListActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String date = (month + 1) + "/" + dayOfMonth + "/" + year;
                            tempCursor = mDatabaseHelper.fetchEntryByDate(date);

                            if(tempCursor.getCount() != 0) {
                                String selectedDate = tempCursor.getString(tempCursor.getColumnIndexOrThrow("date"));
                                String selectedEntry = tempCursor.getString(tempCursor.getColumnIndexOrThrow("entry"));

                                intent2.putExtra("selectedDate", selectedDate);
                                intent2.putExtra("selectedEntry", selectedEntry);
                                startActivity(intent2);

                            }else{
                                Toast.makeText(MemoryListActivity.this, "You have no entry for that day!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, year, month, day);
                    datePicker.show();
                }else{
                    Toast.makeText(MemoryListActivity.this, "You have no entries!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*
            When clicked, entries containing the keyword are shown
         */
        wordSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!adapter.isEmpty()) {
                    String str = keyword.getText().toString();
                    str.toLowerCase();
                    tempCursor = mDatabaseHelper.fetchEntriesByWord(str);
                    adapter2 = new SimpleCursorAdapter(MemoryListActivity.this, android.R.layout.simple_list_item_1, tempCursor, columns, to, 0);
                    entries.setAdapter(adapter2);

                }else if(keyword.getText().toString().isEmpty()){
                    entries.setAdapter(adapter);
                } else{
                    Toast.makeText(MemoryListActivity.this, "You have no entries with that word!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*
            When clicked, a random entry is shown
         */
        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!adapter.isEmpty()){
                    tempCursor = mDatabaseHelper.fetchRandomEntry();
                    String selectedDate = tempCursor.getString(tempCursor.getColumnIndexOrThrow("date"));
                    String selectedEntry = tempCursor.getString(tempCursor.getColumnIndexOrThrow("entry"));
                    intent2.putExtra("selectedDate", selectedDate);
                    intent2.putExtra("selectedEntry", selectedEntry);
                    startActivity(intent2);
                }else {
                    Toast.makeText(MemoryListActivity.this, "You have no entries!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*
            When clicked, the user is returned to the MainActivity
         */
        entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        /*
            When clicked, the entry corresponding to the date is shown
         */
        entries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tempCursor = (Cursor) entries.getItemAtPosition(position);
                String selectedDate = tempCursor.getString(tempCursor.getColumnIndexOrThrow("date"));
                String selectedEntry = tempCursor.getString(tempCursor.getColumnIndexOrThrow("entry"));
                intent2.putExtra("selectedDate", selectedDate);
                intent2.putExtra("selectedEntry", selectedEntry);
                startActivity(intent2);
            }
        });

    }


}
