package com.driver.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class Driver {
    public Driver( String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int driverId;

    String mobile;

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
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

    String password;
    @OneToOne
    @JoinColumn
    Cab cab;

    public Cab getCab() {
        return cab;
    }

    public void setCab(Cab cab) {
        this.cab = cab;
    }

    public List<TripBooking> getTripBookingList() {
        return tripBookingList;
    }

    public void setTripBookingList(List<TripBooking> tripBookingList) {
        this.tripBookingList = tripBookingList;
    }

    @OneToMany(mappedBy = "driver",cascade = CascadeType.ALL)
    List<TripBooking> tripBookingList=new ArrayList<>();
}