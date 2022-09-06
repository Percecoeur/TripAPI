package com.amadeus.trip.model.repository;

import com.amadeus.trip.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

  User findByUsername(String username);
  Boolean existsByUsername(String username);

}