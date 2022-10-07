package com.example.cw_1.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.cw_1.ItemDetailActivity;
import com.example.cw_1.R;
import com.example.cw_1.models.Activity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class DashboardFragment extends Fragment {

    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_dashboard, container, false);

        Connection connection = connectionClass();

        TextView checkExistActivity = view.findViewById(R.id.checkExistActivity);
        TextView monthPickerTitle = view.findViewById(R.id.monthPickerTitle);
        String timeStamp = format.format(Calendar.getInstance().getTime());
        Spinner spinner = view.findViewById(R.id.spinnerTripActivity);
        monthPickerTitle.setText(timeStamp);

        //Set data activities
        ArrayList<Activity> arrayOfActivities = Activity.getActivities();
        ActivitiesAdapter adapter = new ActivitiesAdapter(getActivity().getBaseContext(), arrayOfActivities);
        ListView listView = (ListView)view.findViewById(R.id.listActivity);
        listView.setAdapter(adapter);
        if(arrayOfActivities.isEmpty())
        {
            checkExistActivity.setVisibility(View.GONE);
        }


        // Item in listview clicked
        listView.setOnItemClickListener((adapterView, view1, i, l) -> {
            Intent intent = new Intent(getActivity(), ItemDetailActivity.class);
            intent.putExtra("id", arrayOfActivities.get(i).getId());
            intent.putExtra("category", arrayOfActivities.get(i).getCategory());
            intent.putExtra("money", arrayOfActivities.get(i).getMoney());
            intent.putExtra("date", arrayOfActivities.get(i).getIssueDate());
            intent.putExtra("note", arrayOfActivities.get(i).getNote());
            startActivity(intent);
        });


        // Btn delete trip
        Button btnDeleteTrip = view.findViewById(R.id.btnDeleteTrip);
        btnDeleteTrip.setOnClickListener(v->{
            try{
                if(connection != null){

                    String selectedItem = spinner.getSelectedItem().toString();
                    String sqlScript = "Select * from Trip";
                    Statement st = connection.createStatement();
                    ResultSet rs = st.executeQuery(sqlScript);

                    HashMap<String, Integer> tripData = new HashMap<>();
                    while(rs.next()){
                        tripData.put(rs.getString("TripName") + " in " + rs.getString("Destination"), rs.getInt("TripId"));
                    }
                    Integer tripId = tripData.get(selectedItem);

                    String sqlScript2 = "DELETE FROM Trip Where TripId = " + tripId;
                    Statement st2 = connection.createStatement();
                    st2.executeUpdate(sqlScript2);

                    Toast.makeText(getActivity().getBaseContext(), "Delete trip successful !!!", Toast.LENGTH_SHORT).show();

                    Navigation.findNavController(view).navigate(R.id.navigation_dashboard);

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });

        // Btn delete activities
        Button btnDeleteActivities = view.findViewById(R.id.btnDeleteActivities);
        btnDeleteActivities.setOnClickListener(v->{
            try{
                if(connection != null){
                    String selectedItem = spinner.getSelectedItem().toString();
                    String sqlScript = "Select * from Trip";
                    Statement st = connection.createStatement();
                    ResultSet rs = st.executeQuery(sqlScript);
                    HashMap<String, Integer> tripData = new HashMap<>();
                    while(rs.next()){
                        tripData.put(rs.getString("TripName") + " in " + rs.getString("Destination"), rs.getInt("TripId"));
                    }
                    Integer tripId = tripData.get(selectedItem);
                    String sqlScript2 = "DELETE FROM Activity Where TripId = " + tripId;
                    Statement st2 = connection.createStatement();
                    st2.executeUpdate(sqlScript2);

                    checkExistActivity.setVisibility(View.VISIBLE);
                    //ListView list = view.findViewById(R.id.listActivity);
                    listView.setVisibility(View.GONE);

                    Toast.makeText(getActivity().getBaseContext(), "Delete activities successful !!!", Toast.LENGTH_SHORT).show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });


        //Btn prev, next date clicked
        Button btnPrev = view.findViewById(R.id.btnPrevDay);
        Button btnNext = view.findViewById(R.id.btnNextDay);
        btnPrev.setOnClickListener(v -> {
            arrayOfActivities.clear();
            try {
                if(connection != null){
                    Date dt = format.parse(monthPickerTitle.getText().toString());
                    Calendar cal = Calendar.getInstance();
                    assert dt != null;
                    cal.setTime(dt);
                    cal.add(Calendar.DAY_OF_MONTH, -1);
                    monthPickerTitle.setText(format.format(cal.getTime()));

                    String selectedItem = spinner.getSelectedItem().toString();
                    String sqlScript = "Select * from Trip";
                    Statement st = connection.createStatement();
                    ResultSet rs = st.executeQuery(sqlScript);

                    HashMap<String, Integer> tripData = new HashMap<>();
                    while(rs.next()){
                        tripData.put(rs.getString("TripName") + " in " + rs.getString("Destination"), rs.getInt("TripId"));
                    }
                    Integer tripId = tripData.get(selectedItem);

                    String sqlScript2 = "SELECT * FROM Activity Where Activity.TripId = "+ tripId +"and Activity.IssueDate='" + monthPickerTitle.getText().toString()+"'";
                    Statement st2 = connection.createStatement();
                    ResultSet rs2 = st2.executeQuery(sqlScript2);
                    while(rs2.next()){
                        arrayOfActivities.add(new Activity(Integer.parseInt(rs2.getString("Id")),rs2.getString("Category"),Integer.parseInt(rs2.getString("Amount")), rs2.getDate("IssueDate") ,rs2.getString("Note")));
                    }
                    listView.setAdapter(adapter);
                    checkExistActivity(arrayOfActivities);
                }
            } catch (ParseException | SQLException e) {
                e.printStackTrace();
            }
        });
        btnNext.setOnClickListener(v -> {
            arrayOfActivities.clear();
            try {
                if(connection != null){
                    Date dt = format.parse(monthPickerTitle.getText().toString());
                    Calendar cal = Calendar.getInstance();
                    assert dt != null;
                    cal.setTime(dt);
                    cal.add(Calendar.DAY_OF_MONTH, 1);
                    monthPickerTitle.setText(format.format(cal.getTime()));

                    String selectedItem = spinner.getSelectedItem().toString();
                    String sqlScript = "Select * from Trip";
                    Statement st = connection.createStatement();
                    ResultSet rs = st.executeQuery(sqlScript);

                    HashMap<String, Integer> tripData = new HashMap<>();
                    while(rs.next()){
                        tripData.put(rs.getString("TripName") + " in " + rs.getString("Destination"), rs.getInt("TripId"));
                    }
                    Integer tripId = tripData.get(selectedItem);

                    String sqlScript2 = "SELECT * FROM Activity Where Activity.TripId = "+ tripId +"and Activity.IssueDate='" + monthPickerTitle.getText().toString()+"'";
                    Statement st2 = connection.createStatement();
                    ResultSet rs2 = st2.executeQuery(sqlScript2);
                    while(rs2.next()){
                        arrayOfActivities.add(new Activity(Integer.parseInt(rs2.getString("Id")),rs2.getString("Category"),Integer.parseInt(rs2.getString("Amount")), rs2.getDate("IssueDate") ,rs2.getString("Note")));
                    }
                    listView.setAdapter(adapter);
                    checkExistActivity(arrayOfActivities);
                }
            } catch (ParseException | SQLException e) {
                e.printStackTrace();
            }
        });

        // Set data of trip
        try{
            if(connection != null){
                String sqlScript = "Select * from trip";
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sqlScript);

                ArrayList<String> data = new ArrayList<>();
                while (rs.next()){
                    String TripName = rs.getString("TripName") + " in " + rs.getString("Destination");
                    data.add(TripName);
                }

                ArrayAdapter<String> array = new ArrayAdapter<>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, data);
                array.setDropDownViewResource(android.R.layout
                        .simple_spinner_dropdown_item);
                spinner.setAdapter(array);

                int count = spinner.getAdapter() != null ? spinner.getAdapter().getCount() : 0;
                TextView checkExistList = view.findViewById(R.id.checkExistList);
                if (count == 0){
                    btnDeleteActivities.setEnabled(false);
                    btnDeleteTrip.setEnabled(false);
                    btnNext.setEnabled(false);
                    btnPrev.setEnabled(false);
                    checkExistList.setVisibility(View.VISIBLE);
                    checkExistActivity.setVisibility(View.GONE);
                }
            }
        }
        catch (Exception exception){
            Log.e("Error", exception.getMessage());
        }


        // Event on choosing trip from spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem =  parent.getItemAtPosition(position).toString();

                arrayOfActivities.clear();
                try{
                    if(connection != null){
                        String sqlScript = "Select * from Trip";
                        Statement st = connection.createStatement();
                        ResultSet rs = st.executeQuery(sqlScript);

                        HashMap<String, Integer> tripData = new HashMap<>();
                        while(rs.next()){
                            tripData.put(rs.getString("TripName") + " in " + rs.getString("Destination"), rs.getInt("TripId"));
                        }
                        Integer tripId = tripData.get(selectedItem);



                        String sqlScript1 = "select TripDate, TripId from Trip where TripId =" + tripId;
                        Statement st1 = connection.createStatement();
                        ResultSet rs1 = st1.executeQuery(sqlScript1);
                        HashMap<Integer, Date> tripDate = new HashMap<>();
                        while (rs1.next()){
                            tripDate.put(rs1.getInt("TripId"), rs1.getDate("TripDate"));
                        }
                        Date currentTripDate = tripDate.get(tripId);
                        assert currentTripDate != null;
                        monthPickerTitle.setText(format.format(currentTripDate));



                        String sqlScript2 = "SELECT * FROM Activity Where Activity.TripId = "+ tripId +"and Activity.IssueDate='" + monthPickerTitle.getText().toString()+"'";
                        Statement st2 = connection.createStatement();
                        ResultSet rs2 = st2.executeQuery(sqlScript2);
                        while(rs2.next()){
                            arrayOfActivities.add(new Activity(Integer.parseInt(rs2.getString("Id")),rs2.getString("Category"),Integer.parseInt(rs2.getString("Amount")), rs2.getDate("IssueDate") ,rs2.getString("Note")));
                        }


                        listView.setAdapter(adapter);
                    }
                }
                catch (Exception exception){
                    Log.e("Error", exception.getMessage());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void checkExistActivity(ArrayList arr){
        TextView check = getView().findViewById(R.id.checkExistActivity);
        if(arr.isEmpty())
        {
            check.setVisibility(View.VISIBLE);
        } else {
            check.setVisibility(View.GONE);
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