package com.testautomation.apitesting.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class particulargetdatapractice {

	@Test
	public void particular()
	{
		int bookingid = 1262;
		Response response = 
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
		.extract()
		.response();
		
		Assert.assertTrue(response.getBody().asString().contains("John"));
		String result = response.getBody().asString();
		System.out.println(result);
	}
}
