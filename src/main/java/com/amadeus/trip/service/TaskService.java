package com.amadeus.trip.service;

import com.amadeus.trip.model.Trip;
import com.amadeus.trip.model.exception.TaskException;

import java.time.LocalDateTime;

/**
 * This interface can be used to generate different implementations for notifications
 */
public interface TaskService {

  /**
   * Will generate all notifications (creation, before flight, after flight)
   *
   * @param trip The trip to be notified
   * @throws TaskException in case that task cannot be triggered
   */
  void createAllNotifications(Trip trip) throws TaskException;

  /**
   * Will generate a notification at trip creation
   *
   * @param destination trip destination
   * @throws TaskException in case that task cannot be triggered
   */
  void creationNotifier(String destination) throws TaskException;

  /**
   * Will generate a notification 3h before the trip leaves
   *
   * @param departure the departure of the trip
   * @throws TaskException in case that task cannot be triggered
   */
  void beforeFlightNotifier(LocalDateTime departure) throws TaskException;

  /**
   * Will generate a notification just when the trip arrives at the final airport
   *
   * @param arrivalTime arrival to the final airport
   * @param arrival     airport name
   * @throws TaskException in case that task cannot be triggered
   */
  void landingTimeNotifier(LocalDateTime arrivalTime, String arrival) throws TaskException;

}
