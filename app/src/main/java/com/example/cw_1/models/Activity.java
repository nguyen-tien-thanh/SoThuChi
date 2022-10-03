package com.example.cw_1.models;

import java.util.ArrayList;
import java.util.Date;

public class Activity {
    private String category;
    private Integer amount;
    private Date issueDate;

    public Activity(String category, Integer amount) {
        this.category = category;
        this.amount = amount;
//        this.issueDate = issueDate;
    }

    public String getCategory(){
        return category;
    }

    public Integer getAmount(){
        return amount;
    }

    public Date getIssueDate(){
        return issueDate;
    }

    public static ArrayList<Activity> getActivities() {
        ArrayList<Activity> activities = new ArrayList<Activity>();
        activities.add(new Activity("Breakfast", 10000));
        activities.add(new Activity("Lunch", 30000));
        activities.add(new Activity("Dinner", 20000));
        return activities;
    }
}
