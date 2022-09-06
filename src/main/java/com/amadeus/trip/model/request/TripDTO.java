package com.amadeus.trip.model.request;

import com.amadeus.trip.model.Bound;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.StringJoiner;

@Data
public class TripDTO {
  @NotNull(message = "A trip must contain at least one bound.")
  private List<Bound> bounds;
  @NotNull(message = "A trip must contain a passenger.")
  private String passengerName;

  @Override public String toString() {
    return new StringJoiner(", ", TripDTO.class.getSimpleName() + "[", "]")
        .add("bounds=" + bounds)
        .add("passenger=" + passengerName)
        .toString();
  }
}
