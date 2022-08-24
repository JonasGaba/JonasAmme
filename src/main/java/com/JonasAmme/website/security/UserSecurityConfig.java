package com.JonasAmme.website.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Order(2)
public class UserSecurityConfig {
    @Bean
    public SecurityFilterChain filterChain2(HttpSecurity http) throws Exception {

        http.antMatcher("/user/**")
                .authorizeRequests().anyRequest().hasAnyAuthority("USER")
                .and()
                .formLogin()
                .loginPage("/user/login")
                .usernameParameter("username")
                .loginProcessingUrl("/user/login")
                .defaultSuccessUrl("/user/home")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/user/logout")
                .logoutSuccessUrl("/");

        return http.build();
    }

    @Bean
    public SecurityFilterChain filterChain3(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                //.antMatchers("/","../static/index","/winereviews", "/memories", "/recipes", "/recipes").permitAll()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated();

        return http.build();
    }


}