package com.giansiccardi.microservice.msvc_eventos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@SpringBootApplication
public class MsvcEventosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcEventosApplication.class, args);
	}

}
