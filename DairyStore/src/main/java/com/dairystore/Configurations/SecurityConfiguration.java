package com.dairystore.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                                .requestMatchers("/order/check-availability").hasAuthority("buyer")
                                .requestMatchers("/products/all-for-current-user").hasAuthority("seller")
                                .requestMatchers("/products/all").hasAuthority("admin")
                                .requestMatchers("/products/create").hasAuthority("seller")
                                .requestMatchers("/user/allInformation").hasAnyAuthority("seller", "buyer")
                                .requestMatchers("/test/seller").hasAuthority("seller")
                                .requestMatchers("/test/buyer").hasAuthority("buyer")
                                .requestMatchers("/products/listToBuy").hasAuthority("buyer")
                                .requestMatchers("/cart/add").hasAuthority("buyer")
                                .requestMatchers("/cart/view").hasAuthority("buyer")
                                .requestMatchers(HttpMethod.DELETE, "/cart/{productId}").hasAuthority("buyer")
                                .requestMatchers("/order/delivery-companies").hasAuthority("buyer")
                                .requestMatchers("/order/make").hasAuthority("buyer")
                                .requestMatchers("/order/latest-order").hasAuthority("buyer")
                                /* .requestMatchers("/analytics/buyer").hasAuthority("buyer")*/
                                .requestMatchers("/analysis/buyer").hasAuthority("buyer")
                                .requestMatchers("/analysis/seller").hasAuthority("seller")
                                .requestMatchers("/user/edit").hasAnyAuthority("seller", "buyer")
                                .requestMatchers("/user/deleteCompany").hasAuthority("buyer")
                                .requestMatchers("/css/login.css").permitAll()
                                .requestMatchers("/html/login.html").permitAll()
                                .requestMatchers("/css/registerPage.css").permitAll()
                                .requestMatchers("/html/registerPage.html").permitAll()
                                .requestMatchers("/css/homePage.css").permitAll()
                                .requestMatchers("/html/homePage.html").permitAll()
                                .requestMatchers("/css/sellerPage.css").hasAuthority("seller")
                                .requestMatchers("/products/for-sale").hasAuthority("buyer")
                                .requestMatchers("/css/buyerPage.css").hasAuthority("buyer")
                                .requestMatchers(HttpMethod.DELETE, "/products/{id}").hasAnyAuthority("seller", "admin")
                                .requestMatchers(HttpMethod.GET, "/products/{id}").hasAuthority("seller")
                                .requestMatchers("/products/edit").hasAuthority("seller")
                                .requestMatchers("/api/payments/create-payment-intent").hasAuthority("buyer")
                                .requestMatchers("/delivery-company/all").hasAuthority("admin")
                                .requestMatchers("/user/view-all").hasAuthority("admin")
                                .requestMatchers(HttpMethod.POST, "/delivery-company/create").hasAuthority("admin")
                                .requestMatchers(HttpMethod.DELETE, "/delivery-company/deleteBy/{companyId}").hasAuthority("admin")
                                .requestMatchers(HttpMethod.DELETE, "/user/deleteBy/{userId}").hasAuthority("admin")
                                .requestMatchers("/js/adminPage.js").hasAuthority("admin")
                                .requestMatchers("/js/buyerPage.js").hasAuthority("buyer")
                                .requestMatchers("/js/sellerPage.js").hasAuthority("seller")
                                .requestMatchers("/js/login.js", "/js/userInformationPage.js", "/js/registerPage.js").permitAll()
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

