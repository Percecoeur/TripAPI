package com.amadeus.trip.utils;

import lombok.experimental.UtilityClass;

import java.time.format.DateTimeFormatter;

@UtilityClass
public class Constants {

  public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  public static final String NOTIF_CREATION = "We have received your next trip to %s. We'll accompany you along the way!";
  public static final String NOTIF_TAXI_TO_AIRPORT = "Your flight leaves today at %s. Would you like to order a taxi ?";
  public static final String NOTIF_TAXI_TO_HOME = "You have landed at %s in %s ! Welcome home ! Would you like to order a taxi ?";
  public static final String ADMIN = "ROLE_ADMIN";
  public static final String USER = "ROLE_USER";
  public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";
  public static final String BASIC = "Basic";
  public static final String BEARER = "Bearer";
  public static final String NOTIF_MESSAGE = "message";
}
