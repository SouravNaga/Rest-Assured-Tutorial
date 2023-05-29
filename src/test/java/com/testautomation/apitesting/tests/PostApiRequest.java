package com.testautomation.apitesting.tests;

import org.hamcrest.Matchers;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import com.testautomation.apitesting.utils.BaseTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class PostApiRequest extends BaseTest{
	
	@SuppressWarnings("unchecked")
	@Test
	public void CreateBooking() {
		
		//Prepare the request Body
		JSONObject booking=new JSONObject();
		JSONObject bookingDates=new JSONObject();
		booking.put("firstname", "Sourav");
		booking.put("lastname", "Naga");
		booking.put("totalprice", 40000);
		booking.put("depositpaid", true);
		booking.put("additionalneeds", "Super Bowls");
		booking.put("bookingdates", bookingDates);
		
		bookingDates.put("checkin", "2023-09-10");
		bookingDates.put("checkout", "2023-10-20");
		Response response = 
		RestAssured
		.given()
		.contentType(ContentType.JSON)
		.body(booking.toString())
		.baseUri("https://restful-booker.herokuapp.com/booking")
//		.log().body()
//		.log().headers()
//		.log().all()
		.when()
		.post()
		.then()
		.assertThat()
//		.log().headers()
//		.log().body()
//		.log().ifValidationFails()
		.statusCode(200)
		.body("booking.firstname", Matchers.equalTo("Sourav"))
		.body("booking.totalprice", Matchers.equalTo(40000))
		.body("booking.bookingdates.checkin", Matchers.equalTo("2023-09-10"))
		.extract()
		.response();
		
		int bookingid = response.path("bookingid");
		
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
		.body("firstname", Matchers.equalTo("Sourav"))
		.body("lastname", Matchers.equalTo("Naga"));
		
		
	}

}
