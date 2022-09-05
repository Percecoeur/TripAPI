package com.amadeus.trip.job;

import com.amadeus.trip.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeforeFlightTask implements Runnable {

  private static Logger logger = LoggerFactory.getLogger(BeforeFlightTask.class);
  private String destination;

  public BeforeFlightTask(String destination) {
    this.destination = destination;
  }

  public void run() {
    logger.info(Constants.NOTIF_CREATION, destination);
  }
}