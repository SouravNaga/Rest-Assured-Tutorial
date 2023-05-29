package com.testautomation.apitesting.tests;

import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testautomation.apitesting.pojos.Booking;
import com.testautomation.apitesting.pojos.BookingDates;

public class PostUsingPOJOs {
	
	@Test
	public void testPojos()
	{
		try {
			BookingDates bookingdates = new BookingDates("2023-09-10","2023-10-20");
			Booking booking = new Booking("Babai","Naga","Super Bowls",40000,true,bookingdates);
			
			ObjectMapper objectmapper = new ObjectMapper();
			String request = objectmapper.writerWithDefaultPrettyPrinter().writeValueAsString(booking);
			System.out.println(request);
			
			Booking bookingdetails = objectmapper.readValue(request, Booking.class);
			System.out.println(bookingdetails.getFirstname());
			System.out.println(bookingdetails.getTotalprice());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
