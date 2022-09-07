package com.amadeus.trip.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
@Document("users")
public class User {
  private static final long serialVersionUID = 1L;

  @Id
  @JsonIgnore
  private String id;

  @NotBlank
  @ApiModelProperty(notes = "Name", example = "John", required = true, position = 1)
  private String username;

  @NotBlank
  @ApiModelProperty(notes = "Email", example = "john@deuf.com", required = true, position = 2)
  private String email;

  @JsonIgnore
  @NotBlank
  @ApiModelProperty(notes = "Password", example = "qwertyuiop", required = true, position = 3)
  private String password;

  @DBRef
  private Set<Role> roles = new HashSet<>();

  public User() {
  }

  public User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }

  @Builder
  public User(String username, String email, String password, Set<Role> roles) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.roles = roles;
  }

}