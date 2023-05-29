package com.testautomation.apitesting.tests;

import org.hamcrest.Matchers;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class createAPIpractice {

	@SuppressWarnings("unchecked")
	@Test
	public void createApi()
	{
		JSONObject booking = new JSONObject();
		JSONObject bookingdates = new JSONObject();
		booking.put("firstname", "Sourav");
		booking.put("lastname", "Naga");
		booking.put("totalprice", 12345);
		booking.put("depositpaid", true);
		booking.put("additionalneeds", "Breakfast");
		booking.put("bookingdates", bookingdates);
		
		bookingdates.put("checkin", "2018-01-01");
		bookingdates.put("checkout", "2019-01-01");
		Response response =
		RestAssured
		.given()
		.contentType(ContentType.JSON)
		.baseUri("https://restful-booker.herokuapp.com/booking")
		.body(booking)
		.when()
		.post()
		.then()
		.assertThat()
		.statusCode(200)
		.body("booking.firstname", Matchers.equalTo("Sourav"))
		.extract()
		.response();

//		int bookingid = response.path("bookingid");
		int bookingid = JsonPath.read(response.body().asString(),"$.bookingid");
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
		.extract()
		.response();
		
		
		String res = response2.getBody().asString();
		System.out.println(res);
		
		
		String firstn = JsonPath.read(response2.body().asString(), "$.firstname");
		String lastn = JsonPath.read(response2.body().asString(),"$.lastname");
		Assert.assertEquals(firstn, "Sourav");
		System.out.println(firstn);
		System.out.println(lastn);
		
	}
}
