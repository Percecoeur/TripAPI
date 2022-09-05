package com.amadeus.trip.service;

import com.amadeus.trip.job.CreationTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TaskService {

  @Autowired
  TaskScheduler taskScheduler;

  public void creationNotifier(String destination) {
    taskScheduler.schedule(new CreationTask(destination), Instant.now().plusSeconds(10));
  }

  public void beforeFlightNotifier() {
    //    taskScheduler.schedule(new CreationTask(), Instant.now().plusSeconds(10));
  }

  public void landingTimeNotifier() {
    //    taskScheduler.schedule(new CreationTask(), Instant.now().plusSeconds(10));
  }
}
