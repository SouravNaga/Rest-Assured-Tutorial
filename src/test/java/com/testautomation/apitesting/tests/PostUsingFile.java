package com.testautomation.apitesting.tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;
import com.testautomation.apitesting.utils.BaseTest;
import com.testautomation.apitesting.utils.FilePath;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;

public class PostUsingFile extends BaseTest {

	@Test
	public void createbooking()
	{
		try {
			String bodytxt = FileUtils.readFileToString(new File(FilePath.post_body),"UTF-8");
			Response response =
			RestAssured
			.given()
			.contentType(ContentType.JSON)
			.body(bodytxt)
			.baseUri("https://restful-booker.herokuapp.com/booking")
			.when()
			.post()
			.then()
			.assertThat()
//			.log().body()
			.statusCode(200)
			.extract()
			.response();
			JSONArray jsonarray=JsonPath.read(response.body().asString(),"$.booking..firstname");
			String fname = (String)jsonarray.get(0);
			System.out.println(fname);
			Assert.assertEquals(fname, "Mantu");
			
			int bookingId = JsonPath.read(response.body().asString(),"$.bookingid");
			RestAssured
			.given()
			.contentType(ContentType.JSON)
			.baseUri("https://restful-booker.herokuapp.com/booking")
			.when()
			.get("/{bookingId}",bookingId)
			.then()
			.assertThat()
			.statusCode(200);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
