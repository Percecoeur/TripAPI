package com.amadeus.trip.config;

//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.amadeus.trip.service.CustomUserDetailsService;
import com.amadeus.trip.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserDetailsService customUserDetailsService;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager customAuthenticationManager() throws Exception {
    return authenticationManager();
  }

  @Override
  protected void configure(@Autowired AuthenticationManagerBuilder auth) throws Exception {

    auth
        .userDetailsService(customUserDetailsService)
        .passwordEncoder(passwordEncoder());

  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .authorizeRequests()
        .and()
        .httpBasic()
        .and()
        .authorizeRequests()
        .anyRequest()
        .permitAll()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

//    http
//        .authorizeRequests()
////        .antMatchers("/v0/**").permitAll()
//        .antMatchers("/v0/**").hasAuthority(Constants.ROLE_USER)
////        .antMatchers("/login").permitAll()
////        .antMatchers("/signup").permitAll()
////        .antMatchers("/dashboard/**").hasAuthority("ADMIN")
//        .anyRequest().authenticated()
//        .and().csrf().disable()
//        .formLogin().disable();
  }

  //  @Bean
  //  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
  //    http
  //        .authorizeHttpRequests((auth) -> auth
  //            .anyRequest().authenticated()
  //        )
  //        .httpBasic();
  //    return http.build();
  //  }

  // Create 2 users for demo
  //  @Override
  //  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
  //
  //    auth.inMemoryAuthentication()
  //        .withUser("user").password("password").roles("USER")
  //        .and()
  //        .withUser("admin").password("password").roles("USER", "ADMIN");
  //
  //  }
  //
  //  // Secure the endpoins with HTTP Basic authentication
  //  @Override
  //  protected void configure(HttpSecurity http) throws Exception {
  //
  //    http
  //        //HTTP Basic authentication
  //        .httpBasic()
  //        .and()
  //        .authorizeRequests()
  //        .antMatchers("/v0/**").hasAnyRole("USER", "ADMIN")
  //        .and()
  //        .csrf().disable()
  //        .formLogin().disable();
  //  }
}
