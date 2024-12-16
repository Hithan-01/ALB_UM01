package com.demo.alb_um.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Deshabilitar CSRF temporalmente
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/login", "/resources/**").permitAll()
                .requestMatchers("/portal/manager/**").hasRole("MANAGER")
                .requestMatchers("/portal/admin/**").hasRole("ADMIN")  // Permitir acceso a rutas de admin
                .requestMatchers("/portal/inicio").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/portal/inicio", true)
                .permitAll()
            )
            .logout(logout -> logout
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}