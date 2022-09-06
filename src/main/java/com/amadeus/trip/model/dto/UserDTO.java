package com.amadeus.trip.model.dto;

import com.amadeus.trip.model.Role;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

/**
 * Used to map the API request to the User object model
 */
@Data
public class UserDTO {
  private static final long serialVersionUID = 1L;

  @NotBlank
  @ApiModelProperty(notes = "Name", example = "John", required = true, position = 1)
  private String username;

  @NotBlank
  @ApiModelProperty(notes = "Email", example = "john@deuf.com", required = true, position = 2)
  private String email;

  @NotBlank
  @ApiModelProperty(notes = "Password", example = "qwertyuiop", required = true, position = 3)
  private String password;

  private Set<Role> roles = new HashSet<>();

}