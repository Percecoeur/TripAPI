package com.amadeus.trip.service;

import com.amadeus.trip.model.Role;
import com.amadeus.trip.model.User;
import com.amadeus.trip.model.UserDetailsImpl;
import com.amadeus.trip.model.repository.RoleRepository;
import com.amadeus.trip.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findUserByUsername(username);
    if (user != null) {
      return mapRoleToAuthentication(user, getUserAuthority(user.getRoles()));
    } else {
      throw new UsernameNotFoundException("user not found");
    }
  }

  private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
    Set<GrantedAuthority> roles = new HashSet<>();
    userRoles.forEach((role) -> {
      roles.add(new SimpleGrantedAuthority(role.getName()));
    });

    List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
    return grantedAuthorities;
  }

  private UserDetails mapRoleToAuthentication(User user, List<GrantedAuthority> authorities) {
    return new UserDetailsImpl(user, authorities) {
    };
  }
}