/**
 * @author higor.robinn on 08/06/2025.
 */

// Em: br.edu.mouralacerda.app.config.SecurityConfig.java
package br.edu.mouralacerda.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Futuramente, injetaremos o nosso filtro de token aqui.
     @Autowired
     private FirebaseTokenFilter firebaseTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Desabilitar CSRF, pois nossa API é stateless (não usa sessões/cookies)
                .csrf(csrf -> csrf.disable())

                // 2. Definir a política de sessão como STATELESS. O servidor não guarda estado de autenticação.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Configurar as regras de autorização para os endpoints
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.POST, "/api/students/create").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/professores/registrar").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(firebaseTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
