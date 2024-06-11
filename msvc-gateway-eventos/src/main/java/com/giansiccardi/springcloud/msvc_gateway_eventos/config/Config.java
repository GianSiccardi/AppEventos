package com.giansiccardi.springcloud.msvc_gateway_eventos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {
@Bean
public RestTemplate template(){
return  new RestTemplate();
}
}


