package com.amadeus.trip.controller;

import com.amadeus.trip.config.QuartzConfig;
import com.amadeus.trip.job.QrtzJob;
import com.amadeus.trip.model.Trip;
import com.amadeus.trip.model.User;
import com.amadeus.trip.model.dto.TripDTO;
import com.amadeus.trip.model.exception.AuthenticationException;
import com.amadeus.trip.model.repository.RoleRepository;
import com.amadeus.trip.model.repository.UserRepository;
import com.amadeus.trip.service.TaskService;
import com.amadeus.trip.utils.Constants;
import com.amadeus.trip.utils.Utils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;

@RestController
@RequestMapping("/notif")
@Log4j2
public class NotifApi {

  @Autowired
  TaskService springTaskService;

  @Autowired
  TaskService quartzTaskService;

  @Autowired
  Scheduler scheduler;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @ApiOperation(value = "Will create a new trip for the user" )
  @RolesAllowed({ Constants.USER, Constants.ADMIN })
  @PostMapping(value = "/trip", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<?> addTripNotif(@Valid @RequestBody TripDTO tripRequest,  @ApiParam(name = "is Quartz Enabled", value = "on / off", defaultValue = "off") @RequestParam(required = false) String quartz, @RequestHeader("authorization") String authenticationHeader) {

    try {
      String registeredUser = Utils.extractUserFromBasic(authenticationHeader);
      User user = userRepository.findByUsername(registeredUser);
      if (user == null) {
        throw new AuthenticationException("Your user is not found: " + registeredUser);
      }

      Trip trip = Trip.builder().bounds(tripRequest.getBounds()).passenger(user).build();
      // Using standard spring task service or quartz service scheduler
      // In a real live environment we won't have a double implementation but only one (probably quartz)
      if ("on".equalsIgnoreCase(quartz)){
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

  @GetMapping(value = "/test")
  public ResponseEntity<?> test() throws SchedulerException, IOException {
    JobDetail jobDetail = JobBuilder.newJob(QrtzJob.class).withIdentity("pouetid").build();

    Date triggerJobAt = Date.from(Instant.now().plusSeconds(20));

    SimpleTrigger trigger =
        TriggerBuilder.newTrigger().withIdentity("pouetid").startAt(triggerJobAt)
            .withSchedule(SimpleScheduleBuilder.simpleSchedule())
            .build();

    scheduler.scheduleJob(jobDetail, trigger);

    return ResponseEntity.ok("Launched");
  }
}
