package br.com.testing.restassured;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

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
    	assertTrue(response.statusCode()==200);//forma 1
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
	
	@Test
	public void matchersComHamcrest() {
		//http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/Matchers.html
		assertThat("Maria", is("Maria"));
		assertThat(128, is(128));
		
		assertThat(128, isA(Integer.class));
		assertThat(128d, isA(Double.class));
		
		assertThat(128d, greaterThan(120d));
		assertThat(128d, lessThan(130d));
		
		List<Integer> impares = Arrays.asList(1,3,5,7,9);
		assertThat(impares, hasSize(5));
		assertThat(impares, contains(1,3,5,7,9)); //em ordem diferente dá problema
		assertThat(impares, containsInAnyOrder(9,3,7,5,1)); //testa em qualquer ordem
		assertThat(impares, hasItem(1));
		assertThat(impares, hasItems(1,5));
		
		assertThat("Maria", is(not("João")));
		assertThat("Maria", not("João"));
		assertThat("Maria", anyOf(is("Maria"), is("João")));//OU
		assertThat("Maria", allOf(startsWith("Mar"),endsWith("ia"),containsString("ari")));
	}
	
	@Test
	public void validandoBody() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/ola")//ação
		.then()
			.statusCode(200)
			.body(is("Ola Mundo!"))
			.body(containsString("Mundo"))
			.body(notNullValue());
	}
}