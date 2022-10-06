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

import com.example.cw_1.models.Activity;

import java.sql.Connection;
import java.sql.DriverManager;
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
    TextView title;

    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);

        setTitle("Update activity");


        category = findViewById(R.id.updateCategory);
        money = findViewById(R.id.updateMoney);
        note = findViewById(R.id.updateNote);
        date = findViewById(R.id.updateIssueDate);
        title = findViewById(R.id.titleItemDetail);

        Intent intent = getIntent();
        String activityId = String.valueOf(intent.getIntExtra("id", 0));

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
            assert dt != null;
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
            assert dt != null;
            cal.setTime(dt);
            cal.add(Calendar.DAY_OF_MONTH, 1);
            date.setText(format.format(cal.getTime()));
        });

        category.setText(intent.getStringExtra("category"));
        money.setText(String.valueOf(intent.getIntExtra("money", 0)));
        note.setText(intent.getStringExtra("note"));
        date.setText(intent.getSerializableExtra("date").toString());
        title.setText("Update " + intent.getStringExtra("category"));

        // Update activity
        Button btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(v -> {
            Connection connection = connectionClass();
            try{
                if(connection != null){
                    if(category.getText().toString().length() == 0){
                        Toast.makeText(this, "The category must not be null !!!", Toast.LENGTH_SHORT).show();
                    } else if (money.getText().toString().length() == 0) {
                        Toast.makeText(this, "The money must not be null !!!", Toast.LENGTH_SHORT).show();
                    } else {
                        String sqlScript = "UPDATE Activity SET Category = '" + category.getText().toString() +
                                "',Amount = " + money.getText().toString() +
                                ",IssueDate = '" + date.getText().toString() +
                                "',Note = '" + note.getText().toString() +
                                "' Where Id =" + activityId;
                        Statement st = connection.createStatement();
                        st.executeUpdate(sqlScript);
                        Toast.makeText(this, "Update successful !!!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
            catch (Exception exception) {
                Log.e("Error", exception.getMessage());
                Toast.makeText(this, "Update failed !!!", Toast.LENGTH_SHORT).show();
            }
        });

        // Cancel activity
        Button btnCancelActivity = findViewById(R.id.btnCancelUpdate);
        btnCancelActivity.setOnClickListener(v -> {
            finish();
        });

    }

    @SuppressLint("NewApi")
    public Connection connectionClass() {
        Connection con = null;
        String ip = "192.168.0.106", port = "1433", username = "sa", password = "123456", database = "CRUDAndroidDB";
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