package com.example.cw_1;



import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cw_1.databinding.ActivityMainBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    final List<String> listTripId = new ArrayList<>();

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        connectionClass();

        //Spinner data
        FillSpinner();
        
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_activity, R.id.navigation_dashboard, R.id.navigation_trip, R.id.navigation_search)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    //Fill spinner
    public void FillSpinner(){
        Spinner spinner = findViewById(R.id.spinnerTrip);
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference tripsRef = rootRef.collection("Trip");
        List<String> trips = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, trips);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        tripsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String trip = document.getString("tripName") + " in " + document.getString("destination");
                    trips.add(trip);
                    listTripId.add(document.getId());
                }
                adapter.notifyDataSetChanged();
            }
        });
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
        LinearLayout layout = findViewById(id);
        int count = layout.getChildCount();
        View categoryItems = null;
        for(int i=0; i<count; i++) {
            categoryItems = layout.getChildAt(i);
            categoryItems.setBackgroundResource(0);
        }
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