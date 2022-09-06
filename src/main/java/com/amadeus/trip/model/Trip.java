package com.amadeus.trip.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;

@Data
@Builder
public class Trip {
  @NotEmpty(message = "A trip must contain at least one bound.")
  private List<Bound> bounds;
  @NotNull(message = "A trip must contain a passenger.")
  private User passenger;

  public String getDestination() {
    return bounds.get(bounds.size() - 1).getDestination();
  }

  public String getOrigin() {
    return bounds.get(0).getOrigin();
  }

  public LocalDateTime getDepartureTime() {
    return bounds.get(0).getDepartureTime();
  }

  public LocalDateTime getArrivalTime() {
    return bounds.get(bounds.size() - 1).getArrivalTime();
  }

  public Boolean isRoundTrip() {
    return this.getDestination().equalsIgnoreCase(this.getOrigin());
  }

  @Override public String toString() {
    return new StringJoiner(", ", Trip.class.getSimpleName() + "[", "]")
        .add("bounds=" + bounds)
        .add("passenger=" + passenger)
        .toString();
  }
}
