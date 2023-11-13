package com.pellapps.scientificcalculator;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class History extends AppCompatActivity {

    Button clearHistory;
    ListView list_view;

    TextView emptyView, history_text;

    View layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // set mapping
        layout = findViewById(R.id.layout);
        history_text = findViewById(R.id.history_text);
        clearHistory = findViewById(R.id.clear);
        list_view = findViewById(R.id.list_view);
        emptyView = findViewById(R.id.empty_view);


        //instances of database helper class
        CalculatorDatabaseHelper dbHelper = new CalculatorDatabaseHelper(History.this);
        Cursor cursor = dbHelper.getData();
        ArrayList<String> historyList = new ArrayList<>();

        // get data from database
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String expression = cursor.getString(cursor.getColumnIndex(CalculatorDatabaseHelper.HISTORY_COLUMN_EXPRESSION));
                @SuppressLint("Range") String result = cursor.getString(cursor.getColumnIndex(CalculatorDatabaseHelper.HISTORY_COLUMN_RESULT));
                String history = expression + " = " + result;
                historyList.add(history);
            } while (cursor.moveToNext());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, historyList);
        list_view.setAdapter(adapter);


        //check if list contains any data or not
        if (adapter.getCount() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            list_view.setVisibility(View.GONE);

        } else {
            emptyView.setVisibility(View.GONE);
            list_view.setVisibility(View.VISIBLE);
        }

        //copy result to clipboard
        list_view.setOnItemClickListener((parent, view, position, id) -> {
            String item = (String) parent.getItemAtPosition(position);
            String[] parts = item.split("=");
            String result = parts[1];
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("result", result);
            clipboard.setPrimaryClip(clip);
            Toast toast = Toast.makeText(History.this, "Result Copied", Toast.LENGTH_SHORT);
            toast.show();
        });

        // on click listener for clear history button
        clearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //check if list contains any data or not
                    if (list_view.getAdapter().getCount() > 0) {

                        //delete all data from database
                        dbHelper.deleteAll();

                        //clear list
                        list_view.setAdapter(null);

                        Toast toast = Toast.makeText(History.this, "History Cleared", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    emptyView.setVisibility(View.VISIBLE);
                    list_view.setVisibility(View.GONE);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });





        //check if system night mode is on
        if (isNightModeOn()) {
          //set background color
          getWindow().setStatusBarColor(getResources().getColor(R.color.layout_dark));
          getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE);
          layout.setBackgroundColor(ContextCompat.getColor(this, R.color.layout_dark));
          list_view.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.grey));
          history_text.setTextColor(ContextCompat.getColor(this, R.color.white));
          clearHistory.setTextColor(ContextCompat.getColor(this, R.color.black));
          clearHistory.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.white));
          emptyView.setTextColor(ContextCompat.getColor(this, R.color.white));

        }


    }

    //check if system night mode is on
    private boolean isNightModeOn() {
        int nightModeFlags = this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }

    
}