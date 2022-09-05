package com.amadeus.trip.job;

import com.amadeus.trip.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreationTask implements Runnable {

  private static Logger logger = LoggerFactory.getLogger(CreationTask.class);
  private String destination;

  public CreationTask(String destination) {
    this.destination = destination;
  }

  public void run() {
    logger.info(Constants.NOTIF_CREATION, destination);
  }
}