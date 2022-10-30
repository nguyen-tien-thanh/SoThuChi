package com.example.cw_1.ui.search;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cw_1.ItemDetailActivity;
import com.example.cw_1.R;
import com.example.cw_1.SearchResultActivity;
import com.example.cw_1.databinding.FragmentSearchBinding;
import com.example.cw_1.models.Activity;
import com.example.cw_1.models.Trip;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    ArrayList<Trip> tripList = Trip.getTrips();
    final List<String> listTripId = new ArrayList<>();

    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_search, container, false);

        ListView searchItemView = view.findViewById(R.id.searchItemView);

        // Set data of trip
//        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
//        CollectionReference tripsRef = rootRef.collection("Trip");
//        tripsRef.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                for (QueryDocumentSnapshot document : task.getResult()) {
//                    Trip trip = new Trip(document.getString("tripId"),
//                            document.getString("tripName"),
//                            document.getString("destination"),
//                            document.getDate("tripDate"),
//                            document.getBoolean("riskAssessment"),
//                            document.getString("description"));
//                    tripList.add(trip);
//                    listTripId.add(document.getId());
//                }
//                SearchAdapter adapter = new SearchAdapter(getActivity().getBaseContext(), tripList);
//                searchItemView.setAdapter(adapter);
//            }
//        });

        Connection connection = connectionClass();
        try{
            if(connection != null){
                String sqlScript = "Select * from Trip";
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sqlScript);

                while(rs.next()){
                    Trip trip = new Trip(rs.getString("TripId"),
                            rs.getString("TripName"),
                            rs.getString("Destination"),
                            rs.getDate("TripDate"),
                            rs.getBoolean("RiskAssessment"),
                            rs.getString("Description"));
                    tripList.add(trip);
                }
                SearchAdapter adapter = new SearchAdapter(getActivity().getBaseContext(), tripList);
                searchItemView.setAdapter(adapter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        // Item in listview clicked
        searchItemView.setOnItemClickListener((adapterView, view1, i, l) -> {
            Intent intent = new Intent(getActivity(), SearchResultActivity.class);
            intent.putExtra("tripId", tripList.get(i).getTripId());
            intent.putExtra("tripName", tripList.get(i).getTripName());
            intent.putExtra("destination", tripList.get(i).getDestination());
            intent.putExtra("tripDate", format.format(tripList.get(i).getTripDate()));
            intent.putExtra("riskAssessment", tripList.get(i).getRiskAssessment());
            intent.putExtra("description", tripList.get(i).getDescription());
            startActivity(intent);
        });

        return view;
    }

    private void filterList(String text) {
        ArrayList<Trip> filteredTrip = new ArrayList<>();
        ListView searchItemView = getView().findViewById(R.id.searchItemView);
        for (Trip trip : tripList){
            if(trip.getTripName().toLowerCase().contains(text.toLowerCase()) ||
                    trip.getDestination().contains(text.toLowerCase()) ||
                    trip.getTripDate().toString().contains(text.toLowerCase())){
                filteredTrip.add(trip);

                SearchAdapter filteredAdapter = new SearchAdapter(getActivity().getBaseContext(), filteredTrip);
                searchItemView.setAdapter(filteredAdapter);
            }
        }

        if(tripList.isEmpty()){
            Toast.makeText(getActivity(), "There is no trips / activities to search", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @SuppressLint("NewApi")
    public Connection connectionClass() {
        Connection con = null;
        String ip = "192.168.0.104", port = "1433", username = "sa", password = "123456", database = "CRUDAndroidDB";
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