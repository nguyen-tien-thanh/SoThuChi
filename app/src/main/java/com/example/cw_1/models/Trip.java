package com.example.cw_1.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Trip {
    private String tripId;
    private String tripName;
    private String destination;
    private Date tripDate;
    private Boolean riskAssessment;
    private String description;

    public Trip(String tripId, String tripName, String destination, Date tripDate, Boolean riskAssessment, String description) {
        this.tripId = tripId;
        this.tripName = tripName;
        this.destination = destination;
        this.tripDate = tripDate;
        this.riskAssessment = riskAssessment;
        this.description = description;
    }

    public String getTripId(){return tripId;}

    public String getTripName(){return tripName;}

    public String getDestination(){
        return destination;
    }

    public Date getTripDate(){return tripDate;}

    public Boolean getRiskAssessment() {return riskAssessment;}

    public String getDescription(){return description;}

    public static ArrayList<Trip> getTrips() {
        ArrayList<Trip> trip = new ArrayList<Trip>();
        return trip;
    }
}
