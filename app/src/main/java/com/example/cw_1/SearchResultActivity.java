package com.example.cw_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


public class SearchResultActivity extends AppCompatActivity {

    TextView tripName;
    TextView destination;
    TextView date;
    TextView risk;
    TextView desc;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);

        setTitle("Search result");

        tripName = findViewById(R.id.searchTripName);
        destination = findViewById(R.id.searchDestination);
        date = findViewById(R.id.searchDate);
        risk = findViewById(R.id.searchRisk);
        desc = findViewById(R.id.searchDesc);

        Intent intent = getIntent();
        tripName.setText(intent.getStringExtra("tripName"));
        destination.setText(intent.getStringExtra("destination"));
        date.setText(intent.getSerializableExtra("tripDate").toString());
        Boolean riskCheck = intent.getBooleanExtra("riskAssessment", false);

        risk.setText(riskCheck ? "True" : "False");
        desc.setText(intent.getStringExtra("description") == null ? intent.getStringExtra("description") : "No description" );

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v-> finish());
    }
}