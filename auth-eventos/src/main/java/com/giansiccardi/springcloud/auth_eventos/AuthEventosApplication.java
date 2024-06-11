package com.giansiccardi.springcloud.auth_eventos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@SpringBootApplication
public class AuthEventosApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthEventosApplication.class, args);
	}

}
