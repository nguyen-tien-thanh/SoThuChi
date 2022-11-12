package com.example.cw_1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class SearchResultActivity extends AppCompatActivity {

    FirebaseFirestore firebase = FirebaseFirestore.getInstance();

    TextView tripName;
    TextView destination;
    TextView date;
    TextView risk;
    TextView desc;
    Button btnBack;
    Button btnDelete;

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
        boolean riskCheck = intent.getBooleanExtra("riskAssessment", false);

        risk.setText(riskCheck ? "True" : "False");
        desc.setText(intent.getStringExtra("description") == null ? intent.getStringExtra("description") : "No description" );

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v-> finish());

        btnDelete = findViewById(R.id.btnDeleteActivity);
        btnDelete.setOnClickListener(v-> {
            String tripId = intent.getStringExtra("tripId");

            Connection connection = connectionClass();
            try{
                if(connection != null){
                    String sqlScript = "DELETE FROM Trip Where TripId = '" + tripId + "'";
                    Statement st = connection.createStatement();
                    st.executeUpdate(sqlScript);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            firebase.collection("Trip").document(tripId)
                .delete()
                .addOnSuccessListener(unused -> Toast.makeText(SearchResultActivity.this, "Delete trip " + intent.getStringExtra("tripname") +" successful", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(SearchResultActivity.this, "Delete trip failure !", Toast.LENGTH_SHORT).show());
            });
    }

    @SuppressLint("NewApi")
    public Connection connectionClass() {
        Connection con = null;
        String ip = "192.168.0.102", port = "1433", username = "sa", password = "123456", database = "CRUDAndroidDB";
        StrictMode.ThreadPolicy tp = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(tp);
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String connectionUrl = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";databasename=" + database + ";user=" + username + ";password=" + password + ";";
            con = DriverManager.getConnection(connectionUrl);
        } catch (Exception exception) {
            Log.e("Error", exception.getMessage());
        }
        return con;
    }
}