package com.example.cw_1.ui.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cw_1.R;
import com.example.cw_1.models.Activity;
import com.example.cw_1.models.Trip;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SearchViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is search fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}

class SearchAdapter extends ArrayAdapter<Trip> {
    public SearchAdapter(Context context, ArrayList<Trip> trip) {
        super(context, 0, trip);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_list_view, parent, false);
        }

        Trip trip = getItem(position);

        TextView resultName = convertView.findViewById(R.id.resultName);
        TextView resultDestination = convertView.findViewById(R.id.resultDestination);
        TextView resultDate = convertView.findViewById(R.id.resultDate);
        resultName.setText(trip.getTripName());
        resultDestination.setText("in " + trip.getDestination());
        resultDate.setText(""+trip.getTripDate());
        return convertView;
    }
}