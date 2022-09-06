package com.amadeus.trip.model;

import com.amadeus.trip.utils.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.StringJoiner;

@Data
public class Bound {

  @NotBlank(message = "Destination cannot be missing or empty")

  private String destination;

  @NotBlank(message = "Origin cannot be missing or empty")
  private String origin;

  @NotBlank
  @JsonFormat(pattern = Constants.DATE_FORMAT)
  private LocalDateTime departureTime;

  @NotBlank
  @JsonFormat(pattern = Constants.DATE_FORMAT)
  private LocalDateTime arrivalTime;

  @Override public String toString() {
    return new StringJoiner(", ", Bound.class.getSimpleName() + "[", "]")
        .add("origin='" + origin + "'")
        .add("destination='" + destination + "'")
        .add("departureTime=" + departureTime.format(Constants.DATE_FORMATTER))
        .add("arrivalTime=" + arrivalTime.format(Constants.DATE_FORMATTER))
        .toString();
  }
}
