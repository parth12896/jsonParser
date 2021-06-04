package com.example.jsonParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JsonParserApplication {

	public static void main(String args[]) {

		JsonParser jsonParser = new JsonParser();
		jsonParser.parse("{\"name\": \"Parth Sharma\",\"age\": 20,\"sec\": \"A\",\"student\": {\"name\":\"Piyush Sharma\",\"age\": 21,\"sec\": \"A\"} }",Student.class);
//		SpringApplication.run(JsonParserApplication.class, args);
	}

}
