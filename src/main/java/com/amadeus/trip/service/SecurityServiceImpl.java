package com.amadeus.trip.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

  @Autowired
  private final AuthenticationManager authenticationManager;

  @Autowired
  private final UserDetailsService customUserDetailsService;

  public SecurityServiceImpl(AuthenticationManager authenticationManager, UserDetailsService customUserDetailsService) {
    this.authenticationManager = authenticationManager;
    this.customUserDetailsService = customUserDetailsService;
  }

  @Override
  public boolean login(String username, String password) {
    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
        new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

    authenticationManager.authenticate(usernamePasswordAuthenticationToken);

    if (usernamePasswordAuthenticationToken.isAuthenticated()) {
      SecurityContextHolder.getContext()
          .setAuthentication(usernamePasswordAuthenticationToken);

      return true;
    }

    return false;
  }
}