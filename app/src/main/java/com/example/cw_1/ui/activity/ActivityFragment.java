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
import java.util.List;

public class ActivityFragment extends Fragment {

    private FragmentActivityBinding binding;
    final List<String> listTripId = new ArrayList<>();
    private DatePickerDialog datePickerDialog;
    private Date activityDate;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_activity, container, false);


        // Date first set
        Calendar cal = Calendar.getInstance();
        Date dt = cal.getTime();
        SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy");
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
                    month = month + 1;
                    String date = getMonthFormat(month) + " " + day + " " + year;
                    editDate.setText(date);

                    cal1.set(year,month,day);
                    activityDate = cal1.getTime();
                    System.out.println(activityDate + " DATEEEEEEEEEEEEEE");

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

}