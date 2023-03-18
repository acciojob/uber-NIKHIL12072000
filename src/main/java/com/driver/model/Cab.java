package com.driver.model;

import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

public class Cab {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    public Cab(int perKmRate, boolean available) {
        this.perKmRate = perKmRate;
        this.available = available;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPerKmRate() {
        return perKmRate;
    }

    public void setPerKmRate(int perKmRate) {
        this.perKmRate = perKmRate;
    }

    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    int perKmRate;
    boolean available;

    @OneToOne(mappedBy = "cab", cascade = CascadeType.ALL)
    Driver driver;

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public List<TripBooking> getTripBookingList() {
        return tripBookingList;
    }

    public void setTripBookingList(List<TripBooking> tripBookingList) {
        this.tripBookingList = tripBookingList;
    }

    List<TripBooking> tripBookingList=new ArrayList<>();
}