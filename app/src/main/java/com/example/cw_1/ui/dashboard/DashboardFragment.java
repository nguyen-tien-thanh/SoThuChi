package com.example.cw_1.ui.dashboard;

import static java.lang.Integer.parseInt;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cw_1.R;
import com.example.cw_1.databinding.FragmentDashboardBinding;


import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_dashboard, container, false);

        TextView monthPickerTitle = (TextView) view.findViewById(R.id.monthPickerTitle);
        String timeStamp = format.format(Calendar.getInstance().getTime());
        monthPickerTitle.setText(timeStamp);


        //Btn prev, next clicked
        Button btnPrev = (Button)view.findViewById(R.id.btnPrevDay);
        Button btnNext = (Button)view.findViewById(R.id.btnNextDay);

        btnPrev.setOnClickListener(v -> {
            setDate(true);
        });

        btnNext.setOnClickListener(v -> {
            setDate(false);
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void setDate(Boolean isPrev){
        TextView monthPickerTitle = (TextView)getView().findViewById(R.id.monthPickerTitle);
        try {
            Date dt = format.parse(monthPickerTitle.getText().toString());
            Calendar cal = Calendar.getInstance();
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
}