package com.poc.controller.integrationtest;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.poc.SpringBootGenerateFile;
import com.poc.model.JwtRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootGenerateFile.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class TestGenerateFileController {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testGenerateFile() throws JSONException {
		JwtRequest authenticationRequest = new JwtRequest();
		// hardcoding the username and password
		authenticationRequest.setUsername("pocuser");
		authenticationRequest.setPassword("pocpassword");
		// create headers
		HttpHeaders headers = new HttpHeaders();
		// set `content-type` header
		headers.setContentType(MediaType.APPLICATION_JSON);
		// set `accept` header
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		// build the request
		HttpEntity<JwtRequest> entity = new HttpEntity<>(authenticationRequest, headers);
		// call to the /authenticate end point to get the token.(After validating the
		// credentials it generates the token)
		ResponseEntity<String> tokenAsString = restTemplate.postForEntity("http://localhost:" + port + "/authenticate",
				entity, String.class);

		// add jsonString to the constructor
		JSONObject tokenAsJsonObject = new JSONObject(tokenAsString.getBody());

		// now we can access the values
		String token = tokenAsJsonObject.getString("token");

		// add the `Authorization` header
		headers.add("Authorization", "Bearer " + token);

		// build the request
		HttpEntity<String> generatefileEntity = new HttpEntity<>(token, headers);
		// call to the /generatefile endpoint.
		ResponseEntity<String> result = restTemplate.postForEntity("http://localhost:" + port + "/generatefile",
				generatefileEntity, String.class);

		assertEquals("Successfully created the file", 200, result.getStatusCodeValue());
	}

}
