package com.amadeus.trip.controller;

import com.amadeus.trip.config.JwtConfig;
import com.amadeus.trip.model.Trip;
import com.amadeus.trip.model.User;
import com.amadeus.trip.model.dto.TripDTO;
import com.amadeus.trip.model.exception.AuthenticationException;
import com.amadeus.trip.model.repository.RoleRepository;
import com.amadeus.trip.model.repository.UserRepository;
import com.amadeus.trip.service.TaskService;
import com.amadeus.trip.utils.Constants;
import com.amadeus.trip.utils.Utils;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RestController
@RequestMapping("/notif")
@Log4j2
public class NotifApi {

  @Autowired
  JwtConfig jwtConfig;

  @Autowired
  TaskService springTaskService;

  @Autowired
  TaskService quartzTaskService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @ApiOperation(value = "Will create a new trip for the user")
  @RolesAllowed({ Constants.USER, Constants.ADMIN })
  @PostMapping(value = "/trip", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<?> addTripNotif(@Valid @RequestBody TripDTO tripRequest,
      @ApiParam(name = "is Quartz Enabled", value = "on / off", defaultValue = "off") @RequestParam(required = false) String quartz,
      @RequestHeader("authorization") String authenticationHeader) {

    try {
      String token = Utils.extractTokenFromJwt(authenticationHeader);
      String tokenUser = Jwts.parser()
          .setSigningKey(jwtConfig.getSecret())
          .parseClaimsJws(token)
          .getBody()
          .getSubject();

      User user = userRepository.findByUsername(tokenUser);
      if (user == null) {
        throw new AuthenticationException("Your user is not found: " + tokenUser);
      }

      Trip trip = Trip.builder().bounds(tripRequest.getBounds()).passenger(user).build();
      // Using standard spring task service or quartz service scheduler
      // In a real live environment we won't have a double implementation but only one (probably quartz)
      if ("on".equalsIgnoreCase(quartz)) {
        quartzTaskService.createAllNotifications(trip);
      } else {
        springTaskService.createAllNotifications(trip);
      }
    } catch (AuthenticationException ae) {
      return ResponseEntity
          .badRequest()
          .body(ae.getMessage());
    }

    return new ResponseEntity<>("Your trip was correctly registered for notification", HttpStatus.OK);
  }

}
