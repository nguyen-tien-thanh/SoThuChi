package com.example.cw_1.ui.trip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cw_1.R;
import com.example.cw_1.databinding.FragmentTripBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TripFragment extends Fragment {

    private FragmentTripBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_trip, container, false);

        Calendar cal = Calendar.getInstance();
        Date dt = cal.getTime();

        SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy");
        Button editTripDate = (Button)view.findViewById(R.id.editTripDate);

        editTripDate.setText(format.format(dt));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}