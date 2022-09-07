package com.amadeus.trip.service;

import com.amadeus.trip.job.SpringTask;
import com.amadeus.trip.model.Trip;
import com.amadeus.trip.utils.Constants;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * This implementation is based on a standard spring scheduler
 */
@Service
@Log4j2
public class SpringTaskService implements TaskService {

  @Autowired
  TaskScheduler taskScheduler;

  public void createAllNotifications(Trip trip) {
    this.creationNotifier(trip.getDestination());
    this.beforeFlightNotifier(trip.getDepartureTime());
    this.landingTimeNotifier(trip.getArrivalTime(), trip.getDestination());
  }

  public void creationNotifier(String destination) {
    log.debug("Initializing creation notif with {}", destination);

    String message = String.format(Constants.NOTIF_CREATION, destination);
    taskScheduler.schedule(new SpringTask(message), Instant.now());
  }

  public void beforeFlightNotifier(LocalDateTime departure) {
    log.debug("Initializing before flight notif with departure = {}", departure.format(Constants.DATE_FORMATTER));

    String message = String.format(Constants.NOTIF_TAXI_TO_AIRPORT, departure.format(Constants.DATE_FORMATTER));
    taskScheduler.schedule(new SpringTask(message),
        Date.from(departure.minusHours(3).atZone(ZoneId.systemDefault()).toInstant()));
  }

  public void landingTimeNotifier(LocalDateTime arrivalTime, String arrival) {
    log.debug("Initializing before flight notif with arrival = {}", arrivalTime.format(Constants.DATE_FORMATTER));

    String message = String.format(Constants.NOTIF_TAXI_TO_HOME, arrivalTime.format(Constants.DATE_FORMATTER), arrival);
    taskScheduler.schedule(new SpringTask(message), Date.from(arrivalTime.atZone(ZoneId.systemDefault()).toInstant()));
  }
}
