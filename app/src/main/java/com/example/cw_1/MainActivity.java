package com.example.cw_1;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cw_1.databinding.ActivityMainBinding;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Button dateButton;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initDatePicker();
        dateButton = findViewById(R.id.datePicker);
        dateButton.setText(getTodayDate());

        TextView id = findViewById(R.id.editId);
        TextView date = findViewById(R.id.editDate);
        TextView note = findViewById(R.id.editNote);
        TextView money = findViewById(R.id.editMoney);
        TextView category = findViewById(R.id.editCategory);
        Button btnSave = findViewById(R.id.btnSave);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection connection = connectionClass();
                try{
                    if(connection != null){
                        String sqlinsert = "Insert into Transaction_Tab values ('"
                                +id.getText().toString()+"','"
                                +date.getText().toString()+"','"
                                +note.getText().toString()+"','"
                                +money.getText().toString()+"','"
                                +category.getText().toString()+"')";
                        Statement st = connection.createStatement();
                        ResultSet rs = st.executeQuery(sqlinsert);
                    }
                }
                catch (Exception exception){
                    Log.e("Error", exception.getMessage());
                }}
        });

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }
    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if (month == 1){
            return "JAN";
        }
        if (month == 2){
            return "FEB";
        }
        if (month == 3){
            return "MAR";
        }
        if (month == 4){
            return "APR";
        }
        if (month == 5){
            return "MAY";
        }
        if (month == 6){
            return "JUN";
        }
        if (month == 7){
            return "JUL";
        }
        if (month == 8){
            return "AUG";
        }
        if (month == 9){
            return "SEP";
        }
        if (month == 10){
            return "OCT";
        }
        if (month == 11){
            return "NOV";
        }
        if (month == 12){
            return "DEC";
        }
        return "JAN";
    }


    public void openDatePicker(View view){
        datePickerDialog.show();
    }

    @SuppressLint("NewApi")
    public Connection connectionClass() {
        Connection con = null;
        String ip = "192.168.0.106", port = "1433", username = "sa", password = "123456", databasename = "CRUDAndroidDB";
        StrictMode.ThreadPolicy tp = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(tp);
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String connectionUrl = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";databasename=" + databasename + ";user=" + username + ";password=" + password + ";";
            con = DriverManager.getConnection(connectionUrl);
            System.out.println("THANH CONGGGGGGGGGGGGGGGGGGGGGGGGG");
        } catch (Exception exception) {
            Log.e("Error", exception.getMessage());
        }
        return con;
    }

}