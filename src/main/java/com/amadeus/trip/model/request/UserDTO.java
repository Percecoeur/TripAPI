package com.amadeus.trip.model.request;

import com.amadeus.trip.model.Role;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * Used to map the API request to the User object model
 */
@Data
public class UserDTO {
  private static final long serialVersionUID = 1L;

  private String username;
  private String email;
  private String password;
  private Set<Role> roles = new HashSet<>();

}