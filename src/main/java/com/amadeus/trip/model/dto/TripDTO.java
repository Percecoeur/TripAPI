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
  private List<Bound> bounds;

  @Override public String toString() {
    return new StringJoiner(", ", TripDTO.class.getSimpleName() + "[", "]")
        .add("bounds=" + bounds)
        .toString();
  }
}
