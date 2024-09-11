package com.example.android_sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final String LOG_TAG = "myLogs";
    Button btnAdd, btnRead, btnClear;
    EditText etName, etEmail;
    ListView listView;
    TextView actionTextView;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAdd = findViewById(R.id.btnAdd);
        btnRead = findViewById(R.id.btnRead);
        btnClear = findViewById(R.id.btnClear);

        btnAdd.setOnClickListener(this);
        btnRead.setOnClickListener(this);
        btnClear.setOnClickListener(this);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        listView = findViewById(R.id.listView);
        actionTextView = findViewById(R.id.actionTextView);

        dbHelper = new DBHelper(this);
    }

    @Override
    public void onClick(View v) {
        ContentValues cv = new ContentValues();
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (v.getId() == R.id.btnAdd) {
            actionTextView.setText("Insert in mytable");
            //Log.d(LOG_TAG, "----- Insert in mytable: ---");
            cv.put("name", name);
            cv.put("email", email);
            long rowId = db.insert("mytable", null, cv);
            //Log.d(LOG_TAG, "----- row inserted, ID = " + rowId + ", name = " + name);
            actionTextView.append("\nID = " + rowId + ", name = " + name);

            // После вставки новой записи, обновляем список
            readDataFromDatabase(db);
        } else if (v.getId() == R.id.btnRead) {
            actionTextView.setText("Rows in mytable");
            //Log.d(LOG_TAG, "----- Rows in mytable: ---");
            readDataFromDatabase(db);
        } else if (v.getId() == R.id.btnClear) {
            actionTextView.setText("Clear mytable");
            //Log.d(LOG_TAG, "---- Clear mytable: ---");
            int clearCount = db.delete("mytable", null, null);
            //Log.d(LOG_TAG, "deleted rows count = " + clearCount);
            actionTextView.append("\nDeleted rows count = " + clearCount);

            // После очистки базы данных, обновляем список
            readDataFromDatabase(db);
        }
        dbHelper.close();
    }

    private void readDataFromDatabase(SQLiteDatabase db) {
        Cursor c = db.query("mytable", null, null, null, null, null, null);
        ArrayList<String> dataList = new ArrayList<>();
        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("name");
            int emailColIndex = c.getColumnIndex("email");

            do {
                String row = "ID = " + c.getInt(idColIndex) +
                        ", name = " + c.getString(nameColIndex) +
                        ", email = " + c.getString(emailColIndex);
                //Log.d(LOG_TAG, row);
                dataList.add(row);
            } while (c.moveToNext());
        } else {
            //Log.d(LOG_TAG, "0 rows");
            actionTextView.append("\n0 rows");
        }
        c.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
    }
}