package com.example.cw_1.models;

import java.util.ArrayList;
import java.util.Date;

public class Activity {
    private String category;
    private Integer money;
    private Date issueDate;
    private String note;

    public Activity(String category, Integer money, Date issueDate, String note) {
        this.category = category;
        this.money = money;
        this.issueDate = issueDate;
        this.note = note;
    }

    public String getCategory(){return category;}

    public Integer getMoney(){
        return money;
    }

    public Date getIssueDate(){
        return issueDate;
    }

    public String getNote(){return note;}

    public static ArrayList<Activity> getActivities() {
        ArrayList<Activity> activities = new ArrayList<Activity>();
        return activities;
    }
}
