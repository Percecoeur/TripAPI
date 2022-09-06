package com.amadeus.trip.job;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class Notif implements Runnable {

  private String message;
  private String[] parameters;

  public Notif(String message, String... parameters) {
    this.message = message;
    this.parameters = parameters;
  }

  public void run() {
    log.debug("A notif has been triggered with {} {}", message, parameters);

    // Replace that log with a notification mechanism
    log.info(message, parameters);
  }
}