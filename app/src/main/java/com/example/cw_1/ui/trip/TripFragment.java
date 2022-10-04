package com.example.cw_1.ui.trip;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CalendarView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.cw_1.MainActivity;
import com.example.cw_1.R;
import com.example.cw_1.databinding.FragmentTripBinding;

import java.util.Calendar;


public class TripFragment extends Fragment {

    private FragmentTripBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_trip, container, false);

        Calendar cal = Calendar.getInstance();

        long date = cal.getTimeInMillis();
        CalendarView calView = view.findViewById(R.id.editTripDate);
        calView.setDate(date);

        calView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = year + "-" + month + "-" + dayOfMonth;

                Intent intent = new Intent(getActivity().getBaseContext(), TripFragment.class);
                intent.putExtra("tripDate", date);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}