package com.amadeus.trip.controller;

import com.amadeus.trip.model.Trip;
import com.amadeus.trip.model.User;
import com.amadeus.trip.model.repository.RoleRepository;
import com.amadeus.trip.model.repository.UserRepository;
import com.amadeus.trip.model.request.TripDTO;
import com.amadeus.trip.service.TaskService;
import com.amadeus.trip.utils.Constants;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@RestController
@RequestMapping("/v0")
@Log4j2
public class NotifApi {

  private static final String OK = "Message sent !";
  @Autowired
  PasswordEncoder bCryptPasswordEncoder;
  @Autowired
  TaskService ts;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private RoleRepository roleRepository;

  @ApiOperation(value = "Will create a new trip for the user")
  @RolesAllowed({ Constants.USER, Constants.ADMIN })
  @PostMapping(value = "/addtrip", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<?> addtrip(@Valid @RequestBody TripDTO tripRequest, @RequestHeader("authorization") String authorization) {

    log.info(tripRequest);
    log.info(authorization);
    User user = userRepository.findByUsername(tripRequest.getPassengerName());
    if (user == null) {
      return ResponseEntity
          .badRequest()
          .body("This user is not found");
    }

    Trip trip = Trip.builder().bounds(tripRequest.getBounds()).passenger(user).build();
    ts.createNotifications(trip);

    return new ResponseEntity<Trip>(trip, HttpStatus.OK);
  }

}
