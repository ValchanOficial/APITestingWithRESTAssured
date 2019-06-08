package br.com.testing.restassured;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class HelloWorldTest {
	
	//forma1
	@Test
	public void TestHelloWorld() {
    	Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
    	assertTrue(response.getBody().asString().equals("Ola Mundo!"));
    	assertTrue("O status code deve ser 200",response.statusCode()==200);//forma 1
    	assertEquals(200, response.statusCode());//forma 2
    	assertTrue(response.statusCode()==200);//forma 3
    	ValidatableResponse validacao = response.then();
    	validacao.statusCode(200);
	}
	
	//forma2
	@Test
	public void TestRestAssured() {
    	get("http://restapi.wcaquino.me/ola").then().statusCode(200);
	}
	
	//forma3
	@Test
	public void TestFormaFluente() {
		given()//precondições
		.when().get("http://restapi.wcaquino.me/ola")//ação
		.then()
		//.assertThat()
		.statusCode(200);//assertions
	}
}