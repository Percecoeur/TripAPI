package com.amadeus.trip.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("roles")
public class Role {

  @Id
  @JsonIgnore
  private String id;

  @ApiModelProperty(notes = "Name", example = "ROLE_USER", required = false)
  private String name;

  public Role() {
  }

  @Builder
  public Role(String name) {
    this.name = name;
  }
}
