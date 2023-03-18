package com.driver.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class Customer {


    Customer(String mobile,String password){
        this.mobile=mobile;
        this.password=password;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int customerId;
    String mobile;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<TripBooking> getTripBookingList() {
        return tripBookingList;
    }

    public void setTripBookingList(List<TripBooking> tripBookingList) {
        this.tripBookingList = tripBookingList;
    }

    String password;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    List<TripBooking> tripBookingList=new ArrayList<>();
}