package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.CabRepository;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customerRepository2.save(customer);
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
		Customer customer =customerRepository2.findById(customerId).get();
		customerRepository2.delete(customer);
	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE).
		// If no driver is available, throw "No cab available!" exception
		//Avoid using SQL query
		List<Driver> drivers=driverRepository2.findAll();
		Collections.sort(drivers, new Comparator<Driver>() {
			@Override
			public int compare(Driver o1, Driver o2) {
				return Integer.compare(o1.getDriverId(),o2.getDriverId());
			}
		});
		Driver driver=null;
		for(Driver driver1: drivers){
			if(driver1.getCab().getAvailable()){
				driver=driver1;
				break;
			}
		}
		if(driver==null) throw new Exception("No cab available!");
		else {
			Customer customer=customerRepository2.findById(customerId).get();
			int bill=distanceInKm*driver.getCab().getPerKmRate();
			TripBooking tripBooking = new TripBooking(fromLocation, toLocation, distanceInKm, TripStatus.CONFIRMED, bill, customer, driver);
			customer.getTripBookingList().add(tripBooking);
			driver.getCab().setAvailable(false);
			driver.getTripBookingList().add(tripBooking);
			customerRepository2.save(customer);
			driverRepository2.save(driver);
			return tripBooking;
		}
	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking=tripBookingRepository2.findById(tripId).get();
		tripBooking.setBill(0);
		tripBooking.setStatus(TripStatus.CANCELED);
		tripBooking.getDriver().getCab().setAvailable(true);
		tripBookingRepository2.save(tripBooking);
	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking=tripBookingRepository2.findById(tripId).get();
		tripBooking.setStatus(TripStatus.COMPLETED);
		int bill=tripBooking.getDriver().getCab().getPerKmRate()*tripBooking.getDistanceInKm();
		tripBooking.setBill(bill);
		tripBooking.getDriver().getCab().setAvailable(true);
		tripBookingRepository2.save(tripBooking);
	}
}
