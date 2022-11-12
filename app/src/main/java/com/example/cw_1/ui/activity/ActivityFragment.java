package com.example.cw_1.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cw_1.R;
import com.example.cw_1.databinding.FragmentActivityBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityFragment extends Fragment {

    private FragmentActivityBinding binding;
    final List<String> listTripId = new ArrayList<>();
    public DatePickerDialog datePickerDialog;
    public Date activityDate;
    SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy");
    FirebaseFirestore firebase = FirebaseFirestore.getInstance();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_activity, container, false);

        Button btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> saveActivity(view));

        // Date first set
        Calendar cal = Calendar.getInstance();
        Date dt = activityDate = cal.getTime();
        Button editDate = (Button)view.findViewById(R.id.editDate);
        editDate.setText(format.format(dt));

        // Set spinner data
        Spinner spinner = (Spinner)view.findViewById(R.id.spinnerTrip);
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference tripsRef = rootRef.collection("Trip");
        List<String> trips = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, trips);
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

        // Choose date
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                Calendar cal1 = Calendar.getInstance();


                DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
                    cal1.set(year,month,day);
                    activityDate = cal1.getTime();

                    month = month + 1;
                    String date = getMonthFormat(month) + " " + day + " " + year;
                    editDate.setText(date);
                };
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                int style = AlertDialog.THEME_HOLO_LIGHT;
                new DatePickerDialog(getActivity(), style, dateSetListener, year, month, day).show();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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

    // Create activity
    public void saveActivity (View view){
        Spinner spinner = view.findViewById(R.id.spinnerTrip);
        TextView note = view.findViewById(R.id.editNote);
        TextView money = view.findViewById(R.id.editMoney);
        TextView category = view.findViewById(R.id.editCategory);
        String tripId = listTripId.get(spinner.getSelectedItemPosition());

        if(spinner.getSelectedItem() == null ){
            Toast.makeText(getActivity(), "You need to create Trip first", Toast.LENGTH_SHORT).show();
        } else if(money.getText().toString().length() == 0 ){
            Toast.makeText(getActivity(), "Money can not be null", Toast.LENGTH_SHORT).show();
        } else if (category.getText().toString().length() == 0) {
            Toast.makeText(getActivity(), "Category can not be null", Toast.LENGTH_SHORT).show();
        } else {
            Map<String, Object> activity = new HashMap<>();
            activity.put("category", category.getText().toString());
            activity.put("money", Integer.parseInt(money.getText().toString()));
            activity.put("issueDate", activityDate);
            activity.put("note", note.getText().toString());
            firebase.collection("Trip").document(tripId).collection("Activity")
                    .add(activity)
                    .addOnSuccessListener(documentReference -> {
                        try {
                            Connection connection = connectionClass();

                            if (connection != null) {
                                String activityDateString = format.format(activityDate);
                                String sqlScript = "Insert into Activity (Id, IssueDate, Note, Amount, Category, TripId) values ('"
                                        + documentReference.getId() + "','"
                                        + activityDateString + "','"
                                        + note.getText().toString() + "','"
                                        + money.getText().toString() + "','"
                                        + category.getText().toString() + "','"
                                        + tripId + "')";
                                Statement st = connection.createStatement();
                                st.executeUpdate(sqlScript);
                                Toast.makeText(getActivity(),"Add activity successful!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception exception) {
                            Log.e("Error", exception.getMessage());
                            Toast.makeText(getActivity(), "Save failed !!!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(getActivity(),"Add activity failure!", Toast.LENGTH_SHORT).show());
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