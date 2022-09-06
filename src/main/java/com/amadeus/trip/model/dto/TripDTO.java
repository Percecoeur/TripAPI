package com.amadeus.trip.model.dto;

import com.amadeus.trip.model.Bound;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.StringJoiner;

@Data
public class TripDTO {
  @NotEmpty(message = "A trip must contain at least one bound.")
  @ApiModelProperty(notes = "Name", example = "John", required = true)
  private List<Bound> bounds;

  @NotBlank(message = "A trip must contain a passenger.")
  @ApiModelProperty(notes = "Name", example = "John", required = true)
  private String passengerName;

  @Override public String toString() {
    return new StringJoiner(", ", TripDTO.class.getSimpleName() + "[", "]")
        .add("bounds=" + bounds)
        .add("passenger=" + passengerName)
        .toString();
  }
}
