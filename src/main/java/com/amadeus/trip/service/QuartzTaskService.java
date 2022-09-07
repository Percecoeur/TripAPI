package com.amadeus.trip.service;

import com.amadeus.trip.job.QrtzJob;
import com.amadeus.trip.model.Trip;
import com.amadeus.trip.model.exception.TaskException;
import com.amadeus.trip.utils.Constants;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * This implementation is based on a Quartz scheduler
 */
@Service
@Log4j2
public class QuartzTaskService implements TaskService {

  @Autowired
  Scheduler quartzScheduler;

  public void createAllNotifications(Trip trip) {
    this.creationNotifier(trip.getDestination());
    this.beforeFlightNotifier(trip.getDepartureTime());
    this.landingTimeNotifier(trip.getArrivalTime(), trip.getDestination());
  }

  public void creationNotifier(String destination) {

    String message = String.format(Constants.NOTIF_CREATION, destination);
    Date triggerJobAt = Date.from(Instant.now());
    log.debug("Initializing creation notif with message = {} and trigger = {}", message, triggerJobAt);

    scheduleMyJob(message, triggerJobAt);
  }

  public void beforeFlightNotifier(LocalDateTime departure) {

    String message = String.format(Constants.NOTIF_TAXI_TO_AIRPORT, departure.format(Constants.DATE_FORMATTER));
    Date triggerJobAt = Date.from(departure.minusHours(3).atZone(ZoneId.systemDefault()).toInstant());
    log.debug("Initializing before flight notif with message = {} and trigger = {}", message, triggerJobAt);

    scheduleMyJob(message, triggerJobAt);
  }

  public void landingTimeNotifier(LocalDateTime arrivalTime, String arrival) {

    String message = String.format(Constants.NOTIF_TAXI_TO_HOME, arrivalTime.format(Constants.DATE_FORMATTER), arrival);
    Date triggerJobAt = Date.from(arrivalTime.atZone(ZoneId.systemDefault()).toInstant());
    log.debug("Initializing landing flight notif with message = {} and trigger = {}", message, triggerJobAt);

    scheduleMyJob(message, triggerJobAt);
  }

  private void scheduleMyJob(String message, Date triggerJobAt) throws TaskException {
    try {
      quartzScheduler.scheduleJob(getJobDetail(message), getTrigger(triggerJobAt));
    } catch (SchedulerException e) {
      throw new TaskException("Quartz is not able to launch this task");
    }
  }

  private JobDetail getJobDetail(String message) {
    return JobBuilder.newJob(QrtzJob.class)
        .usingJobData(Constants.NOTIF_MESSAGE, message)
        .build();
  }

  private SimpleTrigger getTrigger(Date triggerJobAt) {
    return TriggerBuilder.newTrigger().startAt(triggerJobAt)
        .withSchedule(SimpleScheduleBuilder.simpleSchedule())
        .build();
  }
}
