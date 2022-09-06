package com.amadeus.trip.utils;

import java.time.format.DateTimeFormatter;

public final class Constants {

  public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  public static final String NOTIF_CREATION = "We have received your next trip to {}. We'll accompany you along the way!";
  public static final String NOTIF_TAXI_TO_AIRPORT = "Your flight leaves today at {}. Would you like to order a taxi ?";
  public static final String NOTIF_TAXI_TO_HOME = "You have landed at {} ! Welcome home ! Would you like to order a taxi ?";
  public static final String ADMIN = "ROLE_ADMIN";
  public static final String USER = "ROLE_USER";
  public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";

  private Constants() {
    // Util class
  }
}
