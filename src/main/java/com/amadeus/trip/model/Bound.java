package com.amadeus.trip.model;

import com.amadeus.trip.utils.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.StringJoiner;

@Data
public class Bound {

  @NotBlank(message = "Destination cannot be missing or empty")
  @ApiModelProperty(notes = "destination", example = "Paris CDG", required = true, position = 1)
  private String destination;

  @NotBlank(message = "Origin cannot be missing or empty")
  @ApiModelProperty(notes = "origin", example = "Nice", required = true, position = 2)
  private String origin;

  @NotBlank
  @JsonFormat(pattern = Constants.DATE_FORMAT)
  @ApiModelProperty(notes = "departureTime", example = "2022-09-06 15:00", required = true, position = 3)
  private LocalDateTime departureTime;

  @NotBlank
  @JsonFormat(pattern = Constants.DATE_FORMAT)
  @ApiModelProperty(notes = "arrivalTime", example = "2022-09-06 22:00", required = true, position = 4)
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
