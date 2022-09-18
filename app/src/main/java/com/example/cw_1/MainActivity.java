package com.example.cw_1;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView id = (TextView)findViewById(R.id.editId);
        TextView date = (TextView)findViewById(R.id.editDate);
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