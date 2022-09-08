package com.amadeus.trip.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ConnectDTO {
  private static final long serialVersionUID = 1L;

  @NotBlank(message = "A username is required")
  @ApiModelProperty(notes = "Name", example = "John", required = true, position = 1)
  private String username;

  @NotBlank(message = "A password is required")
  @ApiModelProperty(notes = "Password", example = "qwertyuiop", required = true, position = 3)
  private String password;

}
