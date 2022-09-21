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
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Button dateButton;
    private DatePickerDialog datePickerDialog;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // DATE PICKER
        initDatePicker();
        dateButton = findViewById(R.id.editDate);
        dateButton.setText(getTodayDate());

        Button date = (Button)findViewById(R.id.editDate);
        TextView note = (TextView)findViewById(R.id.editNote);
        TextView money = (TextView)findViewById(R.id.editMoney);
        TextView category = (TextView)findViewById(R.id.editCategory);
        Button btnSave = (Button)findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection connection = connectionClass();
                try{
                    if(connection != null){
                        String sqlScript = "Insert into Transaction_Table (IssueDate, Note, Money, Category) values ('"
                                +date.getText().toString()+"','"
                                +note.getText().toString()+"','"
                                +money.getText().toString()+"','"
                                +category.getText().toString()+"')";
                        Statement st = connection.createStatement();
                        ResultSet rs = st.executeQuery(sqlScript);

                    }
                }
                catch (Exception exception){
                    Log.e("Error", exception.getMessage());
                    Toast.makeText(MainActivity.this,
                            "Save successful !", Toast.LENGTH_LONG).show();
                }}
        });


        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    public void chooseCategory(View v){
        EditText editCategory = (EditText)findViewById(R.id.editCategory);
        //Remove style all category
        removeBackgroundCategory(R.id.category_row_1);
        removeBackgroundCategory(R.id.category_row_2);
        removeBackgroundCategory(R.id.category_row_3);
        removeBackgroundCategory(R.id.category_row_4);

        //Apply style to chosen category
        v.setBackground(getResources().getDrawable(R.drawable.border_item));

        switch (v.getId()){
            case R.id.category_food:
                editCategory.setText(getResources().getString(R.string.title_item_food));
                break;
            case R.id.category_houseware:
                editCategory.setText(getResources().getString(R.string.title_item_houseware));
                break;
            case R.id.category_clothes:
                editCategory.setText(getResources().getString(R.string.title_item_clothes));
                break;
            case R.id.category_cosmetic:
                editCategory.setText(getResources().getString(R.string.title_item_cosmetic));
                break;
            case R.id.category_exchange:
                editCategory.setText(getResources().getString(R.string.title_item_exchange));
                break;
            case R.id.category_medical:
                editCategory.setText(getResources().getString(R.string.title_item_medical));
                break;
            case R.id.category_education:
                editCategory.setText(getResources().getString(R.string.title_item_education));
                break;
            case R.id.category_electric_bill:
                editCategory.setText(getResources().getString(R.string.title_item_electric_bill));
                break;
            case R.id.category_transportation:
                editCategory.setText(getResources().getString(R.string.title_item_transportation));
                break;
            case R.id.category_contact_fee:
                editCategory.setText(getResources().getString(R.string.title_item_contact_fee));
                break;
            case R.id.category_house_expense:
                editCategory.setText(getResources().getString(R.string.title_item_house_expense));
                break;
            case R.id.category_repair:
                editCategory.setText(getResources().getString(R.string.title_item_repair));
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
        return year + "-" + getMonthFormat(month) + "-" + day;
        //return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if (month == 1){
            return "01";
        }
        if (month == 2){
            return "02";
        }
        if (month == 3){
            return "03";
        }
        if (month == 4){
            return "04";
        }
        if (month == 5){
            return "05";
        }
        if (month == 6){
            return "06";
        }
        if (month == 7){
            return "07";
        }
        if (month == 8){
            return "08";
        }
        if (month == 9){
            return "09";
        }
        if (month == 10){
            return "10";
        }
        if (month == 11){
            return "11";
        }
        if (month == 12){
            return "12";
        }
        return "01";
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
        } catch (Exception exception) {
            Log.e("Error", exception.getMessage());
        }
        return con;
    }


}