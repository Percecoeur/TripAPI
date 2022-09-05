package com.amadeus.trip;

import com.amadeus.trip.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class TripApiApplication {

  @Autowired
  UserRepository UserRepo;

  public static void main(String[] args) {

    SpringApplication.run(TripApiApplication.class, args);
  }

}
