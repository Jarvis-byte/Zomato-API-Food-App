package com.example.zomatoapiapp;

import android.util.Log;

public class ListItem {
    private String name,cuisines,timings,user_rating,phone_number,url,No_Rating,address,city;


    public String getNo_Rating() {
        return No_Rating;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public ListItem(String name, String cuisines, String timings, String user_rating, String phone_number, String url, String No_Rating, String address, String city) {
        this.name = name;
        this.cuisines = cuisines;
        this.timings = timings;
        this.user_rating = user_rating;
        this.phone_number = phone_number;
        this.url = url;
        this.No_Rating=No_Rating;
        this.address=address;
        this.city=city;
    }

    public String getName() {
        return name;
    }

    public String getCuisines() {
        return cuisines;
    }

    public String getTimings() {
        return timings;
    }

    public String getUser_rating() {
        return user_rating;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getUrl() {

        return url;
    }
}
