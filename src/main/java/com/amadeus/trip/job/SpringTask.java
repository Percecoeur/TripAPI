package com.amadeus.trip.job;

import lombok.extern.log4j.Log4j2;

/**
 * This represents a notification message triggered by the spring scheduler
 */
@Log4j2
public class SpringTask implements Runnable {

  private String message;

  public SpringTask(String message) {
    this.message = message;
  }

  public void run() {
    // Replace that log with a notification mechanism
    log.info(message);
  }
}