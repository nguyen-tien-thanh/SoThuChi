package com.example.cw_1.ui.trip;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.cw_1.MainActivity;
import com.example.cw_1.R;
import com.example.cw_1.databinding.FragmentTripBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class TripFragment extends Fragment {

    private FragmentTripBinding binding;
    private Date tripDate;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_trip, container, false);

        FirebaseFirestore firebase = FirebaseFirestore.getInstance();

        Calendar cal = Calendar.getInstance();

        long date = cal.getTimeInMillis();
        CalendarView calView = view.findViewById(R.id.editTripDate);
        calView.setDate(date);

        calView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                cal.set(year, month, dayOfMonth);
                tripDate = cal.getTime();
            }
        });

        // Create Trip
        Button btnSaveTrip = view.findViewById(R.id.btnSaveTrip);
        tripDate = cal.getTime();
        btnSaveTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tripName = view.findViewById(R.id.editTripName);
                TextView destination = view.findViewById(R.id.editDestination);
                TextView description = view.findViewById(R.id.editDescription);
                Boolean switchValue = ((Switch) view.findViewById(R.id.switchRiskAssessment)).isChecked();

                if (tripName.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "Trip name can not be null", Toast.LENGTH_SHORT).show();
                } else if (destination.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "Destination can not be null", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> trip = new HashMap<>();
                    trip.put("tripName", tripName.getText().toString());
                    trip.put("destination", destination.getText().toString());
                    trip.put("tripDate", tripDate);
                    trip.put("riskAssessment", switchValue);
                    trip.put("description", description.getText().toString());

                    firebase.collection("Trip")
                            .add(trip)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(getActivity(), "Create successful !!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Create failure !!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }


//            Connection connection = connectionClass();
//            try{
//                if(connection != null){
//                    String sqlScript = "Insert into Trip (TripName, Destination, TripDate, RiskAssessment, Description ) values ('"
//                            +tripName.getText().toString()+"','"
//                            +destination.getText().toString()+"','"
//                            +tripDate+"','"
//                            +switchValue+"','"
//                            +description.getText().toString()+"')";
//                    Statement st = connection.createStatement();
//                    st.executeUpdate(sqlScript);
//                    Toast.makeText(this, "Create successful !!", Toast.LENGTH_SHORT).show();
//                }
//            }
//            catch (Exception exception) {
//                Log.e("Error", exception.getMessage());
//            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}