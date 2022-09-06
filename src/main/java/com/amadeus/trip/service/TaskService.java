package com.amadeus.trip.service;

import com.amadeus.trip.job.Notif;
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

@Service
@Log4j2
public class TaskService {

  @Autowired
  TaskScheduler taskScheduler;

  public void createNotifications(Trip trip) {
    this.creationNotifier(trip.getDestination());
    this.beforeFlightNotifier(trip.getDepartureTime());
    this.landingTimeNotifier(trip.getArrivalTime(), trip.getDestination());
  }

  private void creationNotifier(String destination) {
    log.debug("Initializing creation notif with {}", destination);
    taskScheduler.schedule(new Notif(Constants.NOTIF_CREATION, destination), Instant.now());
  }

  private void beforeFlightNotifier(LocalDateTime departure) {
    log.debug("Initializing before flight notif with departure = {}", departure.format(Constants.DATE_FORMATTER));
    taskScheduler.schedule(new Notif(Constants.NOTIF_TAXI_TO_AIRPORT, departure.format(Constants.DATE_FORMATTER)),
        Date.from(departure.minusHours(3).atZone(ZoneId.systemDefault()).toInstant()));
  }

  private void landingTimeNotifier(LocalDateTime arrivalTime, String arrival) {
    log.debug("Initializing before flight notif with arrival = {}", arrivalTime.format(Constants.DATE_FORMATTER));
    taskScheduler.schedule(new Notif(Constants.NOTIF_TAXI_TO_HOME, arrival), Date.from(arrivalTime.atZone(ZoneId.systemDefault()).toInstant()));
  }
}
