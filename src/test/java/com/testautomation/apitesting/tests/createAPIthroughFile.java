package com.testautomation.apitesting.tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
//import org.json.simple.JSONArray;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;
import com.testautomation.apitesting.utils.FilePath;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;

public class createAPIthroughFile {

	@Test
	public void createthroughFile()
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
			.statusCode(200)
			.extract()
			.response();
			
			JSONArray jsonarray = JsonPath.read(response.body().asString(),"$.booking..firstname");
			String fname1 = (String) jsonarray.get(0);
			System.out.println("FirstNmae : "+fname1);
			
			
			int bookingid = JsonPath.read(response.body().asString(),"$.bookingid");
			Response response2=
			RestAssured
			.given()
			.pathParam("bookingid", bookingid)
			.baseUri("https://restful-booker.herokuapp.com/booking")
			.when()
			.get("{bookingid}")
			.then()
			.assertThat()
			.statusCode(200)
			.extract()
			.response();
			
			String totalres = response2.getBody().asString();
			System.out.println(totalres);
			String fname = JsonPath.read(response2.body().asString(),"$.firstname");
			
			String exfname = "Babai";
			
			Assert.assertEquals(fname, exfname);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
