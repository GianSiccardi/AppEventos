package com.giansiccardi.springcloud.auth_eventos;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {


    //@LoadBalanced
    @Bean
    WebClient webClient(){
    return WebClient.builder().build();
}

}
