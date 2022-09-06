package com.amadeus.trip.service;

import com.amadeus.trip.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TripService {
  @Autowired
  UserRepository userRepository;



}
