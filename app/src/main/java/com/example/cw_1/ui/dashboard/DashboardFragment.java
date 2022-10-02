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
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public int getDay(String s){
        return Integer.parseInt(s.substring(0,1));
    }

    public int getMonth(String s){
        String month = s.substring(3, 6);
        if (month == "Jan"){
            return 1;
        }
        else if (month == "Feb"){
            return 2;
        }
        else if (month == "Mar"){
            return 3;
        }
        else if (month == "Apr"){
            return 4;
        }
        else if (month == "May"){
            return 5;
        }
        else if (month == "Jun"){
            return 6;
        }
        else if (month == "Jul"){
            return 7;
        }
        else if (month == "Aug"){
            return 8;
        }
        else if (month == "Sep"){
            return 9;
        }
        else if (month == "Oct"){
            return 10;
        }
        else if (month == "Nov"){
            return 11;
        }
        else {
            return 12;
        }
    }

    public int getYear(String i){
        return Integer.parseInt(i.substring(7,10));
    }
}