package com.springSecurity.JWT.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(
                        authorizeRequest -> authorizeRequest
                                .requestMatchers("/test/home", "/test/login", "/test/register", "/test/tokenExpiration").permitAll()
                                .requestMatchers("/products/all").hasAuthority("seller")
                                .requestMatchers("/products/create").hasAuthority("seller")
                                .requestMatchers("/user/allInformation").hasAnyAuthority("seller", "buyer")
                                .requestMatchers("/test/seller").hasAuthority("seller")
                                .requestMatchers("/test/buyer").hasAuthority("buyer")
                                .requestMatchers("/products/listToBuy").hasAuthority("buyer")
                                .requestMatchers("/cart/add").hasAuthority("buyer")
                                .requestMatchers("/cart/view").hasAuthority("buyer")
                                .requestMatchers("/cart/deleteById").hasAuthority("buyer")
                                .requestMatchers("/order/make").hasAuthority("buyer")
                                .requestMatchers("/order/latest-order").hasAuthority("buyer")
                                .anyRequest().authenticated()

                )
                //.formLogin(Customizer.withDefaults())// This is for custom login page: form -> form.loginPage("/login")
                /*.formLogin(form -> form
                        .loginPage("/test/login") // Твоята HTML страница за вход
                        //.successHandler(successHandler) // Обработва успешното влизане
                )*/
                .logout(logout -> logout
                        .logoutSuccessUrl("/test/home")
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

