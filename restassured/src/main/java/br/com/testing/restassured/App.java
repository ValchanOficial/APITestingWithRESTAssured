package br.com.testing.restassured;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class App 
{
    public static void main( String[] args ){
    	Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
    	System.err.println(response.getBody().asString().equals("Ola Mundo!"));
    	System.err.println(response.statusCode()==200);
    	
    	ValidatableResponse validacao = response.then();
    	validacao.statusCode(200);
    }
}
