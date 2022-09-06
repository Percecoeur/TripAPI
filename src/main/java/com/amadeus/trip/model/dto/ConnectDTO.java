package com.amadeus.trip.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

//import javax.validation.constraints.NotBlank;

@Data
public class ConnectDTO {
//  @NotBlank
  @ApiModelProperty(notes = "username", example = "John", required = true)
  private String username;

//  @NotBlank
  @ApiModelProperty(notes = "password", example = "qwertyuiop", required = true)
  private String password;

}
