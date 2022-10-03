package com.example.cw_1.ui.dashboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cw_1.R;
import com.example.cw_1.models.Activity;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DashboardFragment extends Fragment {

    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_dashboard, container, false);

        TextView monthPickerTitle = view.findViewById(R.id.monthPickerTitle);
        String timeStamp = format.format(Calendar.getInstance().getTime());
        monthPickerTitle.setText(timeStamp);


        //Btn prev, next clicked
        Button btnPrev = view.findViewById(R.id.btnPrevDay);
        Button btnNext = view.findViewById(R.id.btnNextDay);
        btnPrev.setOnClickListener(v -> setDate(true));
        btnNext.setOnClickListener(v -> setDate(false));

        //Set data
//        ListView listActivity = view.findViewById(R.id.listActivity);

        ArrayList<Activity> arrayOfActivities = Activity.getActivities();
        ActivitiesAdapter adapter = new ActivitiesAdapter(getActivity().getBaseContext(), arrayOfActivities);
        ListView listView = (ListView)view.findViewById(R.id.listActivity);
        listView.setAdapter(adapter);


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void setDate(Boolean isPrev){
        TextView monthPickerTitle = getView().findViewById(R.id.monthPickerTitle);
        try {
            Date dt = format.parse(monthPickerTitle.getText().toString());
            Calendar cal = Calendar.getInstance();
            assert dt != null;
            cal.setTime(dt);
            if(isPrev) {
                cal.add(Calendar.DAY_OF_MONTH, -1);
            } else {
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
            monthPickerTitle.setText(format.format(cal.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
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