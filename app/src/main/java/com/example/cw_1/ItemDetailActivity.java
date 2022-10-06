package com.example.cw_1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.cw_1.models.Activity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ItemDetailActivity extends AppCompatActivity {
    TextView category;
    TextView money;
    TextView note;
    TextView date;
    TextView tripId;

    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);

        category = findViewById(R.id.updateCategory);
        money = findViewById(R.id.updateMoney);
        note = findViewById(R.id.updateNote);
        date = findViewById(R.id.updateIssueDate);

        Intent intent = getIntent();

        //Btn prev, next date clicked
        Button btnPrev = findViewById(R.id.btnPrevUpdate);
        Button btnNext = findViewById(R.id.btnNextUpdate);
        btnPrev.setOnClickListener(v -> {
            Date dt = null;
            try {
                dt = format.parse(date.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);
            cal.add(Calendar.DAY_OF_MONTH, -1);
            date.setText(format.format(cal.getTime()));
        });
        btnNext.setOnClickListener(v -> {
            Date dt = null;
            try {
                dt = format.parse(date.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);
            cal.add(Calendar.DAY_OF_MONTH, 1);
            date.setText(format.format(cal.getTime()));
        });

        category.setText(intent.getStringExtra("category"));
        money.setText(String.valueOf(intent.getIntExtra("money", 0)));
        note.setText(intent.getStringExtra("note"));
        date.setText(intent.getSerializableExtra("date").toString());
    }
}