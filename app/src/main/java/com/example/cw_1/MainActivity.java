package com.example.cw_1;



import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // DATE PICKER
        initDatePicker();

        //Spinner data
        FillSpinner();



        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_trip)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    // Create activity
    public void saveActivity (View view){
        Button dateButton = findViewById(R.id.editDate);
        Spinner spinner = (Spinner)findViewById(R.id.spinnerTrip);
        TextView note = (TextView)findViewById(R.id.editNote);
        TextView money = (TextView)findViewById(R.id.editMoney);
        TextView category = (TextView)findViewById(R.id.editCategory);

        Connection connection = connectionClass();
        String compareValue = spinner.getSelectedItem().toString();

        if(money.getText().toString() == null || money.getText().toString().length() == 0 ){
            Toast.makeText(this, "Money can not be null", Toast.LENGTH_SHORT).show();
        } else if (category.getText().toString().length() == 0 || category.getText().toString() == null) {
            Toast.makeText(this, "Category can not be null", Toast.LENGTH_SHORT).show();
        } else {
            try {
                if (connection != null) {
                    String sqlScript = "Select * from Trip";
                    Statement st = connection.createStatement();
                    ResultSet rs = st.executeQuery(sqlScript);

                    HashMap<String, Integer> tripData = new HashMap<>();
                    while (rs.next()) {
                        tripData.put(rs.getString("TripName") + " in " + rs.getString("Destination"), rs.getInt("TripId"));
                    }
                    Integer tripId = tripData.get(compareValue);


                    String sqlScript2 = "Insert into Activity (IssueDate, Note, Amount, Category, TripId) values ('"
                            + dateButton.getText().toString() + "','"
                            + note.getText().toString() + "','"
                            + money.getText().toString() + "','"
                            + category.getText().toString() + "','"
                            + tripId.toString() + "')";
                    Statement st2 = connection.createStatement();
                    st2.executeUpdate(sqlScript2);

                    Toast.makeText(this, "Created successful!!!", Toast.LENGTH_SHORT).show();

                }
            } catch (Exception exception) {
                Log.e("Error", exception.getMessage());
                Toast.makeText(this, "Save failed !!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Create Trip
    public void saveTrip(View view){
        // Calendar view
        Intent incomingIntent = getIntent();
        String tripDate = incomingIntent.getStringExtra("tripDate");
        if(tripDate == null) {
            Calendar cal = Calendar.getInstance();
            Date dt = cal.getTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            tripDate = format.format(dt);
        }

        Connection connection = connectionClass();
        TextView tripName = findViewById(R.id.editTripName);
        TextView destination = findViewById(R.id.editDestination);
        TextView description = findViewById(R.id.editDescription);
        Boolean switchValue = ((Switch) findViewById(R.id.switchRiskAssessment)).isChecked();

        if(tripName.getText().toString() == null || tripName.getText().toString().length() == 0 ){
            Toast.makeText(this, "Trip name can not be null", Toast.LENGTH_SHORT).show();
        } else if (destination.getText().toString().length() == 0 || destination.getText().toString() == null) {
            Toast.makeText(this, "Destination can not be null", Toast.LENGTH_SHORT).show();
        }
        else {
            try{
                if(connection != null){
                    String sqlScript = "Insert into Trip (TripName, Destination, TripDate, RiskAssessment, Description ) values ('"
                            +tripName.getText().toString()+"','"
                            +destination.getText().toString()+"','"
                            +tripDate+"','"
                            +switchValue+"','"
                            +description.getText().toString()+"')";
                    Statement st = connection.createStatement();
                    st.executeQuery(sqlScript);
                }
            }
            catch (Exception exception) {
                Log.e("Error", exception.getMessage());
            }
        }
    }

    //Fill spinner
    public void FillSpinner(){
        Spinner spinner = (Spinner)findViewById(R.id.spinnerTrip);
        Connection connection = connectionClass();
        try{
            if(connection != null){
                String sqlScript = "Select * from trip";
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sqlScript);

                ArrayList<String> data = new ArrayList<String>();
                while (rs.next()){
                    String TripName = rs.getString("TripName") + " in " + rs.getString("Destination");
                    data.add(TripName);
                }

                ArrayAdapter<String> array = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
                array.setDropDownViewResource(android.R.layout
                                .simple_spinner_dropdown_item);
                spinner.setAdapter(array);
            }
        }
        catch (Exception exception){
            Log.e("Error", exception.getMessage());
        }
    }


    @SuppressLint("NonConstantResourceId")
    public void chooseCategory(View v){
        EditText editCategory = findViewById(R.id.editCategory);
        //Remove style all category
        removeBackgroundCategory(R.id.category_row_1);
        removeBackgroundCategory(R.id.category_row_2);
        removeBackgroundCategory(R.id.category_row_3);

        //Apply style to chosen category
        v.setBackground(getResources().getDrawable(R.drawable.border_item));

        switch (v.getId()){
            case R.id.category_breakfast:
                editCategory.setText(getResources().getString(R.string.title_item_breakfast));
                break;
            case R.id.category_lunch:
                editCategory.setText(getResources().getString(R.string.title_item_lunch));
                break;
            case R.id.category_dinner:
                editCategory.setText(getResources().getString(R.string.title_item_dinner));
                break;
            case R.id.category_contact_fee:
                editCategory.setText(getResources().getString(R.string.title_item_contact_fee));
                break;
            case R.id.category_exchange:
                editCategory.setText(getResources().getString(R.string.title_item_exchange));
                break;
            case R.id.category_medical:
                editCategory.setText(getResources().getString(R.string.title_item_medical));
                break;
            case R.id.category_abode:
                editCategory.setText(getResources().getString(R.string.title_item_abode));
                break;
            case R.id.category_printing:
                editCategory.setText(getResources().getString(R.string.title_item_printing));
                break;
            case R.id.category_transportation:
                editCategory.setText(getResources().getString(R.string.title_item_transportation));
                break;
            default:
                editCategory.setText(getResources().getString(R.string.hint_category));
                break;
        }
    }

    private void removeBackgroundCategory (int id){
        LinearLayout layout = (LinearLayout)findViewById(id);
        int count = layout.getChildCount();
        View categoryItems = null;
        for(int i=0; i<count; i++) {
            categoryItems = layout.getChildAt(i);
            categoryItems.setBackgroundResource(0);
        }
    }


    private void initDatePicker() {
        Button dateButton = (Button)findViewById(R.id.editDate);
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = getMonthFormat(month) + " " + day + " " + year;
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

    private String getMonthFormat(int month) {
        if (month == 1){return "Jan";}
        if (month == 2){return "Feb";}
        if (month == 3){return "Mar";}
        if (month == 4){return "Apr";}
        if (month == 5){return "May";}
        if (month == 6){return "Jun";}
        if (month == 7){return "Jul";}
        if (month == 8){return "Aug";}
        if (month == 9){return "Sep";}
        if (month == 10){return "Oct";}
        if (month == 11){return "Nov";}
        if (month == 12){return "Dec";}
        return "Jan";
    }

    public void openDatePicker(View view){
        datePickerDialog.show();
    }
    //public void openTripDatePicker(View view){ tripDatePickerDialog.show();}

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