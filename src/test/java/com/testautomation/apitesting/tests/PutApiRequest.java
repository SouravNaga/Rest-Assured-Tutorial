package com.testautomation.apitesting.tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;
import com.testautomation.apitesting.utils.FilePath;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;

public class PutApiRequest {

	@Test
	public void updateApi()
	{
		try {
			String postbody = FileUtils.readFileToString(new File(FilePath.post_body),"UTF-8");
			String tokenbody = FileUtils.readFileToString(new File(FilePath.token_body),"UTF-8");
			String putbody = FileUtils.readFileToString(new File(FilePath.put_body),"UTF-8");
			
			//Sending the post Request
			Response response = 
			RestAssured
			.given()
			.body(postbody)
			.contentType(ContentType.JSON)
			.baseUri("https://restful-booker.herokuapp.com/booking")
			.when()
			.post()
			.then()
			.assertThat()
			.statusCode(200)
			.extract()
			.response();
			int bookingid = JsonPath.read(response.body().asString(), "$.bookingid");
			JSONArray jsonarray = JsonPath.read(response.body().asString(), "$.booking..firstname");
			String fname = (String)jsonarray.get(0);
			System.out.println(fname);
			
			//Generate the token
			Response token_response =
			RestAssured
			.given()
			.body(tokenbody)
			.contentType(ContentType.JSON)
			.baseUri("https://restful-booker.herokuapp.com/auth?")
			.when()
			.post()
			.then()
			.assertThat()
			.statusCode(200)
			.extract()
			.response();
			
			String token_val = JsonPath.read(token_response.body().asString(), "$.token");
			System.out.println(token_val);
			
			//Update the API
			Response put_response =
			RestAssured
			.given()
			.body(putbody)
			.contentType(ContentType.JSON)
			.header("Cookie", "token="+token_val)
			.pathParam("bookingid", bookingid)
			.baseUri("https://restful-booker.herokuapp.com/booking")
			.when()
			.put("{bookingid}")
			.then()
			.assertThat()
			.statusCode(200)
			.body("firstname", Matchers.equalTo("Mantu"))
			.extract()
			.response();
			
			String update_fname = JsonPath.read(put_response.body().asString(), "$.firstname");
			System.out.println(update_fname);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
