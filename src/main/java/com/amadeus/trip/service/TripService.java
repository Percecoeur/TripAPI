package com.amadeus.trip.service;

import com.amadeus.trip.model.Trip;
import com.amadeus.trip.model.User;
import com.amadeus.trip.model.repository.UserRepository;
import com.amadeus.trip.model.request.TripDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TripService {
  @Autowired
  UserRepository userRepository;



}
