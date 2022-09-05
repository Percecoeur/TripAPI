package com.amadeus.trip.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("roles")
public class Role {

  @Id
  private String id;
  private String name;

  public Role() {
  }

  public Role(String name) {
    this.name = name;
  }
}
