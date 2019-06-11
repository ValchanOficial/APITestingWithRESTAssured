package br.com.testing.restassured;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UserJSONTest {
	
	@Test
	public void deveFicarPrimeiroNivel() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/1")
		.then()
			.statusCode(200)
			.body("id", is(1))
			.body("name",containsString("Silva"))
			.body("age", greaterThan(1));
	}
	
	@Test
	public void deveVerificarPrimeiroNivelComOutrasFormas() {
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/users/1");
		
		assertEquals(new Integer(1), response.path("id"));
		assertEquals(new Integer(1), response.path("%s","id"));
		
		//jsonpath
		JsonPath jpath = new JsonPath(response.asString());
		assertEquals(1, jpath.getInt("id"));
		
		//from
		//int id = JsonPath.from(response.asString()).getInt("id");
		//assertEquals(1, id);
		assertEquals(1, JsonPath.from(response.asString()).getInt("id"));
	}
	
	@Test
	public void deveVerificarSegundoNivel() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/2")
		.then()
			.statusCode(200)
			.body("name",containsString("Joaquina"))
			.body("endereco.rua", is("Rua dos bobos"));
	}
	
	@Test
	public void deveVerificarLista() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/3")
		.then()
			.statusCode(200)
			.body("name",containsString("Ana"))
			.body("filhos", hasSize(2))
			.body("filhos[0].name", is("Zezinho"))
			.body("filhos[1].name", is("Luizinho"))
			.body("filhos.name", hasItem("Zezinho"))
			.body("filhos.name", hasItems("Zezinho","Luizinho"));
	}
	
	@Test
	public void deveVerificarErro() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users/4")
		.then()
			.statusCode(404)
			.body("error", is("Usuário inexistente"));
	}
	
	@Test
	public void deveVerificarListaRaiz() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)
			//.body("", hasSize(3))//raiz
			.body("$", hasSize(3))//$ = raiz
			.body("name", hasItems("João da Silva","Maria Joaquina","Ana Júlia"))
			.body("age[1]", is(25))
			.body("filhos.name", hasItem(Arrays.asList("Zezinho","Luizinho")))
			.body("salary", contains(1234.5678f,2500,null));
	}
	
	@Test
	public void deveFazerVerificacoesAvancadas() {
		given()
		.when()
			.get("http://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)
			.body("$", hasSize(3))
			.body("age.findAll{it <= 25}.size()",is(2))//idade menor ou igual a 25
			.body("age.findAll{it <= 25 && it > 20}.size()",is(1))
			.body("findAll{it.age <= 25 && it.age > 20}.name",hasItem("Maria Joaquina")) //retorna lista
			.body("findAll{it.age <= 25}[0].name",is("Maria Joaquina")) //trnasforma em object
			.body("findAll{it.age <= 25}[-1].name",is("Ana Júlia"))
			.body("find{it.age <= 25 && it.age > 20}.name",is("Maria Joaquina"))//pega 1 registro
			.body("findAll{it.name.contains('n')}.name", hasItems("Maria Joaquina","Ana Júlia"))
			.body("findAll{it.name.length() > 10}.name", hasItems("João da Silva","Maria Joaquina"))
			.body("name.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA"))
			.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA"))
			.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}", allOf(hasItem("MARIA JOAQUINA"), hasSize(1)))
			.body("age.collect{it * 2}", hasItems(60,50,40))
			.body("id.min()", is(1))
			.body("salary.max()", is(2500))
			.body("salary.min()", is(1234.5678f))
			.body("salary.findAll{it != null}.sum()", is(closeTo(3734.5678,0.001)))
			.body("salary.findAll{it != null}.sum()", allOf(greaterThan(3000d),lessThan(5000d)));
	}
	
	@Test
	public void devoUnirJsonPathComJava() {
		ArrayList<String> names = 
			given()
			.when()
				.get("http://restapi.wcaquino.me/users")
			.then()
				.statusCode(200)		
				.extract().path("name.findAll{it.startsWith('Maria')}");
		assertEquals(1, names.size());
		assertTrue(names.get(0).equalsIgnoreCase("Maria Joaquina"));
		assertEquals(names.get(0).toUpperCase(), "maria joaquina".toUpperCase());;
	}
}