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
	@Autowired
	CabRepository cabRepository;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customerRepository2.save(customer);
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
		customerRepository2.deleteById(customerId);
	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE).
		// If no driver is available, throw "No cab available!" exception
		//Avoid using SQL query
		Customer customer=customerRepository2.findById(customerId).get();
		List<Cab> cabs=cabRepository.findAll();
		Collections.sort(cabs, new Comparator<Cab>() {
			@Override
			public int compare(Cab o1, Cab o2) {
				return Integer.compare(o1.getDriver().getDriverId(),o2.getDriver().getDriverId());
			}
		});
		Driver driver=null;
		Cab cab=null;
		for(Cab cab1: cabs){
			if(cab1.getAvailable()){
				driver=cab1.getDriver();
				cab=cab1;
				break;
			}
		}
		if(driver==null) throw new Exception("No cab available!");
		else {
			int bill=distanceInKm*cab.getPerKmRate();
			TripBooking tripBooking = new TripBooking(fromLocation, toLocation, distanceInKm, TripStatus.CONFIRMED, bill, customer, driver);
			customer.getTripBookingList().add(tripBooking);
			customerRepository2.save(customer);

			cab.setAvailable(false);
			cabRepository.save(cab);
			return tripBooking;
		}
	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking=tripBookingRepository2.findById(tripId).get();
		tripBooking.setBill(0);
		tripBooking.setStatus(TripStatus.CANCELED);
		tripBookingRepository2.save(tripBooking);

		Cab cab=tripBooking.getDriver().getCab();
		cab.setAvailable(true);
		cabRepository.save(cab);
	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking=tripBookingRepository2.findById(tripId).get();
		tripBooking.setStatus(TripStatus.COMPLETED);
		tripBookingRepository2.save(tripBooking);

		Cab cab=tripBooking.getDriver().getCab();
		cab.setAvailable(true);
		cabRepository.save(cab);
	}
}
