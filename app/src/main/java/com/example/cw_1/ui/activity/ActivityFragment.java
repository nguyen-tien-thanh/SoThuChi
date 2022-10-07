package com.example.cw_1.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cw_1.R;
import com.example.cw_1.databinding.FragmentActivityBinding;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ActivityFragment extends Fragment {

    private FragmentActivityBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_activity, container, false);

        // Date first set
        Calendar cal = Calendar.getInstance();
        Date dt = cal.getTime();
        SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy");
        Button editTripDate = (Button)view.findViewById(R.id.editDate);
        editTripDate.setText(format.format(dt));

        Spinner spinner = (Spinner)view.findViewById(R.id.spinnerTrip);
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

                ArrayAdapter<String> array = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, data);
                array.setDropDownViewResource(android.R.layout
                        .simple_spinner_dropdown_item);
                spinner.setAdapter(array);
            }
        }
        catch (Exception exception){
            Log.e("Error", exception.getMessage());
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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