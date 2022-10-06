package com.example.cw_1.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.cw_1.R;
import com.example.cw_1.models.Activity;

import java.text.DecimalFormat;
import java.util.ArrayList;

class ActivitiesAdapter extends ArrayAdapter<Activity> {
    public ActivitiesAdapter(Context context, ArrayList<Activity> activities) {
        super(context, 0, activities);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_list_view, parent, false);
        }

        Activity activity = getItem(position);

        TextView tvCategory = convertView.findViewById(R.id.tvCategory);
        TextView tvAmount = convertView.findViewById(R.id.tvAmount);
        tvCategory.setText(activity.getCategory());
        tvAmount.setText(currencyFormat(activity.getMoney().toString()));
        return convertView;
    }

    public static String currencyFormat(String amount) {
        DecimalFormat formatter = new DecimalFormat("$ #,###");
        return formatter.format(Double.parseDouble(amount));
    }

}