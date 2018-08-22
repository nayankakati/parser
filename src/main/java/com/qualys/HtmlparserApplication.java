package com.qualys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class HtmlparserApplication {

	public static void main(String[] args) {
		SpringApplication.run(HtmlparserApplication.class, args);
	}
}
