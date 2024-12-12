package com.springSecurity.JWT.Configurations;

import com.springSecurity.JWT.Security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration  {
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    public SecurityConfiguration(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                //.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                //TODO: I have to view how to use JWT
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(
                        authorizeRequest -> authorizeRequest
                                .requestMatchers("/test/login").hasAuthority("seller")
                                .requestMatchers("/test/login").hasAuthority("admin")
                                .anyRequest().permitAll()
                ).formLogin(Customizer.withDefaults())// This is for custom login page: form -> form.loginPage("/login")
                .logout(logout -> logout
                        //.logoutUrl("/custom-logout")
                        //.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                        .logoutSuccessUrl("/test/home")
                        //.invalidateHttpSession(true)
                        //.deleteCookies("JSESSIONID")
                );
                /*.logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                        .logoutSuccessUrl("/login?logout")
                );*/
               /* .logout(logout -> logout
                        .logoutUrl("/logout") // URL за logout
                        .logoutSuccessUrl("/test/home") // Пренасочване към homePage
                        .invalidateHttpSession(true) // Инвалидиране на сесията
                        .deleteCookies("JSESSIONID") // Изтриване на сесийните бисквитки
                )
                .sessionManagement(session -> session
                        .sessionConcurrency(concurrency -> concurrency
                                .maximumSessions(1) // Ограничаване до 1 активна сесия
                                .expiredUrl("/login?expired=true") // Пренасочване при изтичане на сесията
                        )
                );
                *//*.headers()
                .frameOptions().sameOrigin() // Защита от clickjacking
                .xssProtection().block(true) // Защита от XSS
                .addHeaderWriter(new StaticHeadersWriter("X-Custom-Header", "Value")); */// Пример за добавяне на потребителски хедър// Пример: добавяне на потребителски хедър
                //.logout().logoutSuccessUrl("/test/home").permitAll();
        return httpSecurity.build();
    }

}