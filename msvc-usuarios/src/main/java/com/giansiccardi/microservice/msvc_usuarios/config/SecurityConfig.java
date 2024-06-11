package com.giansiccardi.microservice.msvc_usuarios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

   @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity htpp) throws Exception {
        htpp.authorizeHttpRequests()
                .requestMatchers("/usuarios/eliminar-evento-usuario/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/usuarios/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/usuarios/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
                return htpp.build();
    }


}
