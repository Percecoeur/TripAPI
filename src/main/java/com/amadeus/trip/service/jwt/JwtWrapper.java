package com.amadeus.trip.service.jwt;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtWrapper {
  private static final long serialVersionUID = 1L;

  private String token;
}
