package com.amadeus.trip.controller;

import com.amadeus.trip.utils.Constants;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/v0")
@Log4j2
public class NotifApi {

  private static final String OK = "Message sent !";

  @ApiOperation(value = "Very basic Hello world application")
  @RolesAllowed({ Constants.ROLE_USER })
  @PostMapping("/register")
  public String register(String body) {


    log.info("Received API CALL");
    return OK;
  }
}
