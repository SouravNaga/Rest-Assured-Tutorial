package com.testautomation.apitesting.tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.testautomation.apitesting.pojos.Booking;
import com.testautomation.apitesting.pojos.BookingDates;
import com.testautomation.apitesting.utils.FilePath;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;

public class createPOJOsPractice {

	@Test
	public void pojocApicreate() throws JsonProcessingException
	{
		try {
			String schema = FileUtils.readFileToString(new File(FilePath.schema_body),"UTF-8");
		BookingDates bookingdates = new BookingDates("2023-09-10","2023-10-20");
		Booking booking = new Booking("Babai","Naga","Super Bowls",40000,true,bookingdates);
		
		ObjectMapper objectmapper = new ObjectMapper();
		String request = objectmapper.writerWithDefaultPrettyPrinter().writeValueAsString(booking);
		System.out.println(request);
		Booking bookingdetails = objectmapper.readValue(request, Booking.class);
		System.out.println(bookingdetails.getFirstname());
		Response response =
		RestAssured
		.given()
		.contentType(ContentType.JSON)
		.body(request)
		.baseUri("https://restful-booker.herokuapp.com/booking")
		.when()
		.post()
		.then()
		.assertThat()
		.statusCode(200)
		.extract()
		.response();
		
		int bookingid = JsonPath.read(response.body().asString(),"$.bookingid");
		JSONArray jsonarray = JsonPath.read(response.body().asString(),"$.booking..firstname");
		String exfname = (String)jsonarray.get(0);
		System.out.println(bookingid);
		Response response2 =
		RestAssured
		.given()
		.contentType(ContentType.JSON)
		.pathParam("bookingid", bookingid)
		.baseUri("https://restful-booker.herokuapp.com/booking")
		.when()
		.get("{bookingid}")
		.then()
		.assertThat()
		.statusCode(200)
		.body(JsonSchemaValidator.matchesJsonSchema(schema))
		.extract()
		.response()
		;
		
		String res = response2.getBody().asString();
		System.out.println(res);
		
		String actfname = JsonPath.read(response2.body().asString(), "$.firstname");
		System.out.println(actfname);
		System.out.println(exfname);
		Assert.assertEquals(actfname, exfname);
		System.out.println(schema);
	}
	 catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}
