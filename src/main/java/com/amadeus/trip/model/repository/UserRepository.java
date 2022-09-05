package com.amadeus.trip.model.repository;

import com.amadeus.trip.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User, String> {

  @Query("{username:'?0'}")
  User findUserByUsername(String username);

}