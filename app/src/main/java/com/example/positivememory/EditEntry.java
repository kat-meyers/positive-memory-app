package com.example.positivememory;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditEntry extends AppCompatActivity {

    Button back, delete, save;
    EditText entry;
    TextView date;
    Intent intent, intent2, intent3;
    String str;
    DatabaseHelper mDatabaseHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entry);

        back = findViewById(R.id.returnButton);
        delete = findViewById(R.id.deleteButton);
        save = findViewById(R.id.saveEntryButton);
        entry = findViewById(R.id.editEntryEditText);
        date = findViewById(R.id.editEntryDateTextView);
        intent = new Intent(EditEntry.this, IndividualMemoryActivity.class);
        intent2 = getIntent();
        intent3 = new Intent(EditEntry.this, MemoryListActivity.class);
        mDatabaseHelper = new DatabaseHelper(this);

        str = intent2.getStringExtra("selectedEntry");

        entry.setText(str);
        date.setText(intent2.getStringExtra("selectedDate"));

        /*
            When clicked, the user is returned to the IndividualMemoryActivity without saving
         */
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("selectedDate", date.getText().toString());
                intent.putExtra("selectedEntry", str);
                startActivity(intent);
            }
        });

        /*
            When clicked, a confirmation message pops up and if 'yes' is selected, the entry is deleted
         */
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                mDatabaseHelper.deleteEntry(date.getText().toString());
                                startActivity(intent3);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        /*
            When clicked, the edited entry is saved
         */
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseHelper.updateEntry(date.getText().toString(), entry.getText().toString());
                intent.putExtra("selectedDate", date.getText().toString());
                intent.putExtra("selectedEntry", entry.getText().toString());
                startActivity(intent);
            }
        });
    }
}
